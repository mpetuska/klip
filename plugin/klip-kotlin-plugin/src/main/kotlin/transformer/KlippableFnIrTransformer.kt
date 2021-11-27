package dev.petuska.klip.plugin.transformer

import dev.petuska.klip.plugin.util.KlipSettings
import java.io.File
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
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.putArgument
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getArgumentsWithIr
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isNullConst
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.util.Logger
import org.jetbrains.kotlin.utils.addToStdlib.cast

/**
 * The main worker-bee of the plugin, responsible for actually transforming the "klippable" function
 * calls to pass in required parameters
 */
class KlippableFnIrTransformer(
    private val context: IrPluginContext,
    private val logger: Logger,
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
    klipPath = filePath.parentFile.resolve("__klips__/${filePath.name}.klip").canonicalPath
    index = 0
    scope = null
    scopeName = null

    irFile.transformChildrenVoid()
    irFile.declarations.addAll(newDeclarations.filter { it.parent == irFile })
  }

  private fun findScopeFunction(declaration: IrFunction): IrFunction? {
    if (settings.scopeFunctions.any {
      declaration.kotlinFqName == it || declaration.realOverrideTarget.kotlinFqName == it
    } ||
        settings.scopeAnnotations.any {
          declaration.hasAnnotation(it) || declaration.realOverrideTarget.hasAnnotation(it)
        }) {
      scope = declaration
      index = 0
    }
    return scope
  }

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    findScopeFunction(declaration)
    return super.visitFunctionNew(declaration)
  }

  override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
    val fn = scope ?: findScopeFunction(expression.symbol.owner)
    fn?.let { _ ->
      val foundParam =
          fn.valueParameters.find { it.name.asString() == "name" && it.type.isString() }
              ?: fn.extensionReceiverParameter?.takeIf { it.type.isString() }
      val foundArg =
          foundParam?.let { nameArg ->
            expression.getArgumentsWithIr().find { (arg, _) -> arg == nameArg }?.second
          }

      foundArg?.takeIf { it is IrConst<*> }?.cast<IrConst<*>>()?.value?.toString()?.let {
        scopeName = it
        index = 0
      }
    }
    val path = klipPath

    if (fn != null && settings.klipAnnotations.any { expression.symbol.owner.hasAnnotation(it) }) {
      val tName = scopeName?.let { "($it)" } ?: ""
      val klipKey = "${fn.kotlinFqName.asString()}${tName}#${index++}"
      val irBuilder = DeclarationIrBuilder(context, expression.symbol)

      val param: IrValueParameter? =
          expression.symbol.owner.valueParameters.find { it.type.classOrNull == klipContextClass }
      if (param != null &&
          expression.getArgumentsWithIr().none { (p, v) -> p == param && !v.isNullConst() }) {
        val klipContextConstructorCall =
            irBuilder.irCall(klipContextClass.constructors.first()).apply {
              putValueArgument(0, irBuilder.irString(path))
              putValueArgument(1, irBuilder.irString(klipKey))
              putValueArgument(2, irBuilder.irBoolean(settings.update))
            }
        expression.putArgument(param, klipContextConstructorCall)
      }
    }
    return super.visitFunctionAccess(expression)
  }
}
