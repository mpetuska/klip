package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.transformer.KlippableFnIrTransformer
import dev.petuska.klip.plugin.util.KlipLogger
import dev.petuska.klip.plugin.util.KlipSettings
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.FqName

/** An orchestrator to manage processing flows via transformers. */
class KlipIrGenerationExtension(
  private val settings: KlipSettings,
  private val logger: KlipLogger,
) : IrGenerationExtension {
  private val contextFqName = FqName("dev.petuska.klip.api.KlipContext")

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    logger { settings.toString() }
    val klipContextClass = pluginContext.referenceClass(contextFqName)
    if (settings.enabled && klipContextClass != null) {
      logger { "KlipContext detected. Running..." }
      val transformer = KlippableFnIrTransformer(
        context = pluginContext,
        logger = logger,
        settings = settings,
        klipContextClass = klipContextClass,
      )
      for (file in moduleFragment.files) {
        transformer.runOnFileInOrder(file)
      }
    } else {
      logger.log(
        "Klip plugin disabled [enabled=${settings.enabled}, KlipContextClass=$klipContextClass]"
      )
    }
  }

  private fun FileLoweringPass.runOnFileInOrder(irFile: IrFile) {
    irFile.acceptVoid(
      object : IrElementVisitorVoid {
        override fun visitElement(element: IrElement) {
          element.acceptChildrenVoid(this)
        }

        override fun visitFile(declaration: IrFile) {
          logger { "Inspecting ${declaration.path}" }
          lower(declaration)
          super.visitFile(declaration)
        }
      }
    )
  }
}
