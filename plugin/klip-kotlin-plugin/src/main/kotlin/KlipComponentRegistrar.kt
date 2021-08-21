package dev.petuska.klip.plugin

import com.google.auto.service.AutoService
import dev.petuska.klip.plugin.util.KlipKeys
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

/**
 * Component Registrar responsible for validating command line inputs and registering required IR extensions
 */
@AutoService(ComponentRegistrar::class)
class KlipComponentRegistrar : ComponentRegistrar {
  override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
    val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
    if (configuration[KlipKeys.KEY_ENABLED] == false) {
      return
    }

    IrGenerationExtension.registerExtension(
      project,
      KlipIrGenerationExtension(
        settings = KlipSettings(
          root = File(
            configuration[KlipKeys.KEY_ROOT] ?: error("klip plugin requires source root option passed to it")
          ),
          update = configuration[KlipKeys.KEY_UPDATE] ?: KlipOption.Update.default,
          klipAnnotations = configuration[KlipKeys.KEY_KLIP_ANNOTATION] ?: listOf(),
          scopeAnnotations = configuration[KlipKeys.KEY_SCOPE_ANNOTATION] ?: listOf(),
        ),
        messageCollector = messageCollector,
      )
    )
  }
}