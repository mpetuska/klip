package dev.petuska.klip.plugin.transformer

import dev.petuska.klip.plugin.util.KlipLogger
import dev.petuska.klip.plugin.util.hasAll
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.copyAttributes
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.isNullConst
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class KlipContextIrTransformer(
  private val context: IrPluginContext,
  private val logger: KlipLogger,
  private val klipContextClass: IrClassSymbol,
  private val klipContextConstructorCall: IrConstructorCall,
) : IrElementTransformerVoid() {
  override fun visitCall(expression: IrCall): IrExpression {
    val (param, fn) = findTarget(expression)
    logger { "pick: $fn" }

    val irBuilder = DeclarationIrBuilder(context, expression.symbol)
    val rewrite =
      if (param != null) {
        expression.apply {
          if (getValueArgument(param.index)?.isNullConst() == true) putArgument(param, klipContextConstructorCall)
        }
      } else {
        irBuilder.irCall(fn).also {
          it.copyAttributes(expression)
          it.copyTypeArgumentsFrom(expression)
          it.dispatchReceiver = expression.dispatchReceiver
          it.extensionReceiver = expression.extensionReceiver
          it.putValueArgument(0, klipContextConstructorCall)
          for (i in 0 until expression.valueArgumentsCount) {
            it.putValueArgument(i + 1, expression.getValueArgument(i))
          }
        }
      }
    logger {
      """
      |================== ORIGINAL ==================
      |${expression.dump()}============================================
      """.trimMargin()
    }
    logger {
      """
      |================== REWRITE ==================
      |${rewrite.dump()}=============================================
      """.trimMargin()
    }
    return rewrite
  }

  private fun findTarget(expression: IrFunctionAccessExpression): Pair<IrValueParameter?, IrFunctionSymbol> {
    val param = expression.symbol.owner.findContextParam()
    return if (param != null) {
      param to expression.symbol
    } else {
      val candidates = context.referenceFunctions(expression.symbol.owner.kotlinFqName).mapNotNull { fn ->
        fn.owner.findContextParam()?.takeIf { it.index == 0 }?.let { fn }
      }
      logger { "candidates: ${candidates.size}" }
      null to candidates.first { fn ->
        val (expect, actual) = fn.owner to expression.symbol.owner
        val hasValueParameters = expect.valueParameters.hasAll(actual.valueParameters)
        val hasTypeParameters = expect.typeParameters.hasAll(actual.typeParameters)
        val sameDispatch =
          expect.dispatchReceiverParameter?.type?.classOrNull == actual.dispatchReceiverParameter?.type?.classOrNull
        val sameExtension =
          expect.extensionReceiverParameter?.type?.classOrNull == actual.extensionReceiverParameter?.type?.classOrNull
        logger {
          """
            candidate: $fn
            hasValueParameters: $hasValueParameters
            hasTypeParameters: $hasTypeParameters
            sameDispatch: $sameDispatch
            sameExtension: $sameExtension
          """.trimIndent()
        }
        hasValueParameters && hasTypeParameters && sameDispatch && sameExtension
      }
    }
  }

  private fun IrFunction.findContextParam() = valueParameters.find {
    it.type.classOrNull == klipContextClass
  }
}
