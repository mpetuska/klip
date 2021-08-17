package dev.petuska.klip.plugin

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class KlipIrGenerationExtension(
  private val settings: KlipSettings,
  private val messageCollector: MessageCollector,
) : IrGenerationExtension {

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    for (file in moduleFragment.files) {
      KlipIrTransformer(
        context = pluginContext,
        messageCollector = messageCollector,
        settings = settings,
      ).runOnFileInOrder(file)
    }
  }

  private fun FileLoweringPass.runOnFileInOrder(irFile: IrFile) {
    irFile.acceptVoid(object : IrElementVisitorVoid {
      override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
      }

      override fun visitFile(declaration: IrFile) {
        lower(declaration)
        super.visitFile(declaration)
      }
    })
  }
}
