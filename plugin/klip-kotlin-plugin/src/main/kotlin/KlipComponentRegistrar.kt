package dev.petuska.klip.plugin

import com.google.auto.service.AutoService
import dev.petuska.klip.plugin.util.KlipOption
import dev.petuska.klip.plugin.util.KlipSettings
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.js.messageCollectorLogger
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.name.FqName

/**
 * Component Registrar responsible for validating command line inputs and registering required IR
 * extensions
 */
@AutoService(ComponentRegistrar::class)
class KlipComponentRegistrar : ComponentRegistrar {
  override fun registerProjectComponents(
      project: MockProject,
      configuration: CompilerConfiguration
  ) {
    val messageCollector =
        configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
    IrGenerationExtension.registerExtension(
        project,
        KlipIrGenerationExtension(
            settings =
                KlipSettings(
                    enabled = configuration[KlipOption.Enabled.key] == true,
                    update = configuration[KlipOption.Update.key] == true,
                    klipAnnotations =
                        configuration[KlipOption.KlipAnnotation.key]?.map { FqName(it) }
                            ?: listOf(),
                    scopeAnnotations =
                        configuration[KlipOption.ScopeAnnotation.key]?.map { FqName(it) }
                            ?: listOf(),
                ),
            logger = messageCollectorLogger(messageCollector),
        ))
  }
}
