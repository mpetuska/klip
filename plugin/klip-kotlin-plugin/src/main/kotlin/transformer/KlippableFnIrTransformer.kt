package dev.petuska.klip.plugin.transformer

import dev.petuska.klip.plugin.util.KlipSettings
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.putArgument
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getArgumentsWithIr
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isNullConst
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.util.Logger
import java.io.File

/**
 * The main worker-bee of the plugin, actually transforming the "klippable" function calls to pass in required parameters
 */
class KlippableFnIrTransformer(
  private val context: IrPluginContext,
  private val logger: Logger,
  private val settings: KlipSettings,
  private val klipContextClass: IrClassSymbol,
) : IrElementTransformerVoidWithContext(), FileLoweringPass {
  private var index: Int = 0
  private lateinit var klipPath: String
  private var function: IrFunction? = null

  private val newDeclarations = mutableListOf<IrDeclaration>()

  override fun lower(irFile: IrFile) {
    val filePath = File(irFile.path)
    klipPath = filePath.parentFile.resolve("__klips__/${filePath.name}.klip").canonicalPath
    index = 0

    irFile.transformChildrenVoid()
    irFile.declarations.addAll(newDeclarations.filter { it.parent == irFile })
  }

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    if (settings.scopeAnnotations.any { declaration.hasAnnotation(it) }) {
      function = declaration
      index = 0
    }
    return super.visitFunctionNew(declaration)
  }

  override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
    val fn = function
    val path = klipPath

    if (fn != null && settings.klipAnnotations.any { expression.symbol.owner.hasAnnotation(it) }) {
      val klipKey = "${fn.kotlinFqName.asString()}#${index++}"

      val irBuilder = DeclarationIrBuilder(
        context,
        expression.symbol
      )

      val param: IrValueParameter? =
        expression.symbol.owner.valueParameters.find { it.type.classOrNull == klipContextClass }
      if (param != null && expression.getArgumentsWithIr().none { (p, v) -> p == param && !v.isNullConst() }) {
        val klipContextConstructorCall = irBuilder.irCall(klipContextClass.constructors.first()).apply {
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
