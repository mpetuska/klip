package dev.petuska.klip.plugin.transformer

import dev.petuska.klip.plugin.util.KlipLogger
import dev.petuska.klip.plugin.util.KlipSettings
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.utils.realOverrideTarget
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getArgumentsWithIr
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.io.File

/**
 * The main worker-bee of the plugin, responsible for actually transforming the "klippable" function
 * calls to pass in required parameters
 */
class KlippableFnIrTransformer(
  private val context: IrPluginContext,
  private val logger: KlipLogger,
  private val settings: KlipSettings,
  private val klipContextClass: IrClassSymbol,
) : IrElementTransformerVoidWithContext(), FileLoweringPass {
  private var index: Int = 0
  private lateinit var klipPath: String
  private var scope: IrFunction? = null
  private var scopeName: String? = null

  private val newDeclarations = mutableListOf<IrDeclaration>()

  override fun lower(irFile: IrFile) {
    val filePath = File(irFile.path)
    klipPath = filePath.parentFile.resolve("__klips__/${filePath.name}.json").canonicalPath
    index = 0
    scope = null
    scopeName = null

    irFile.transformChildrenVoid()
    irFile.declarations.addAll(newDeclarations.filter { it.parent == irFile })
  }

  private fun detectScopeFn(declaration: IrFunction): IrFunction? {
    val insideScopeFunction = settings.scopeFunctions.any {
      declaration.kotlinFqName == it || declaration.realOverrideTarget.kotlinFqName == it
    }
    val insideScopeAnnotation = settings.scopeAnnotations.any {
      declaration.hasAnnotation(it) || declaration.realOverrideTarget.hasAnnotation(it)
    }
    if (insideScopeFunction || insideScopeAnnotation) {
      scope = declaration
      index = 0
    }
    return scope
  }

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    logger { "Visiting function ${declaration.kotlinFqName}" }
    detectScopeFn(declaration)
    return super.visitFunctionNew(declaration)
  }

  override fun visitCall(expression: IrCall): IrExpression {
    logger { "Visiting call [scope: ${scope?.kotlinFqName}] ${expression.symbol.owner.kotlinFqName}" }
    val scopeFn = scope
    val isKlippable = settings.klipAnnotations.any { expression.symbol.owner.hasAnnotation(it) }
    logger { "isKlippable: $isKlippable" }
    if (scopeFn == null || !isKlippable) return super.visitCall(expression)

    logger { "scope: ${scope?.name}" }
    detectScopeName(scopeFn, expression)
    val path = klipPath
    val klipContextConstructorCall = run {
      val tName = scopeName?.let { "($it)" } ?: ""
      val klipKey = "${scopeFn.kotlinFqName.asString()}$tName#${index++}"
      val irBuilder = DeclarationIrBuilder(context, expression.symbol)

      irBuilder.irCall(klipContextClass.constructors.first()).apply {
        putValueArgument(0, irBuilder.irString(path))
        putValueArgument(1, irBuilder.irString(klipKey))
        putValueArgument(2, irBuilder.irBoolean(settings.update))
        putValueArgument(3, irBuilder.irString(settings.serverUrl))
      }
    }
    logger { "fn: ${expression.symbol.owner.name}" }
    logger { "fnAnnotations: ${expression.symbol.owner.annotations}" }
    return expression.transform(
      KlipContextIrTransformer(context, logger, klipContextClass, klipContextConstructorCall),
      null
    )
  }

  private fun detectScopeName(scopeFn: IrFunction, expression: IrFunctionAccessExpression) {
    val foundParam = scopeFn.valueParameters.find { it.name.asString() == "name" && it.type.isString() }
      ?: scopeFn.extensionReceiverParameter?.takeIf { it.type.isString() }
    val foundArg = foundParam?.let { nameArg ->
      expression.getArgumentsWithIr().find { (arg, _) -> arg == nameArg }?.second
    }

    foundArg?.takeIf { it is IrConst<*> }?.cast<IrConst<*>>()?.value?.toString()?.let {
      scopeName = it
      index = 0
    }
  }
}
