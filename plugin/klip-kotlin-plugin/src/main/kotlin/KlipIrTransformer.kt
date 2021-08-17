package dev.petuska.klip.plugin

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.js.messageCollectorLogger
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.util.kotlinFqName
import java.io.File

class KlipIrTransformer(
  private val context: IrPluginContext,
  messageCollector: MessageCollector,
  private val settings: KlipSettings,
) : IrElementTransformerVoidWithContext(), FileLoweringPass {
  companion object {
    private val sourceSetSplitRegex = "(src[\\\\/].+[\\\\/])kotlin".toRegex()
  }

  private val logger = messageCollectorLogger(messageCollector)
  private var index: Int = 0
  private lateinit var file: IrFile
  private var function: IrFunction? = null

  private val newDeclarations = mutableListOf<IrDeclaration>()

  override fun lower(irFile: IrFile) {
    file = irFile
    index = 0

    irFile.transformChildrenVoid()
    irFile.declarations.addAll(newDeclarations.filter { it.parent == irFile })
  }

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    if (declaration.annotations.any {
      it.symbol.owner.parent.kotlinFqName.asString().contains("Test")
    }
    ) {
      function = declaration
      index = 0
    }
    return super.visitFunctionNew(declaration)
  }

  private fun IrFunctionAccessExpression.setIfNull(index: Int, value: IrExpression?) {
    if (getValueArgument(index) == null) {
      putValueArgument(index, value)
    }
  }

  private fun buildKlipPath(): String {
    val filePath = File(file.path).canonicalPath
    val rootPath = settings.root.canonicalPath
    val rawKlipFile: File = if (filePath.startsWith(rootPath)) {
      File(settings.root.resolve("../klips"), filePath.removePrefix(rootPath))
    } else {
      rootPath.split(sourceSetSplitRegex).let { (root) ->
        File(root, sourceSetSplitRegex.find(rootPath)!!.groupValues[1])
          .resolve("klips")
          .resolve(filePath.split(sourceSetSplitRegex)[1].removePrefix("/"))
      }
    }

    return rawKlipFile.canonicalPath + ".klip"
  }

  override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
    val functionFqName = expression.symbol.owner.kotlinFqName.asString()
    val fn = function
    if (fn != null && functionFqName in settings.functions) {
      val klipPath = buildKlipPath()
      val klipKey = "${fn.kotlinFqName.asString()}#${index++}"

      val irBuilder = DeclarationIrBuilder(
        context,
        expression.symbol
      )
      expression.setIfNull(expression.valueArgumentsCount - 3, irBuilder.irString(klipPath))
      expression.setIfNull(expression.valueArgumentsCount - 2, irBuilder.irString(klipKey))
      expression.setIfNull(expression.valueArgumentsCount - 1, irBuilder.irBoolean(settings.update))
    }
    return super.visitFunctionAccess(expression)
  }
}
