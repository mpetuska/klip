package dev.petuska.klip.plugin

import com.google.auto.service.AutoService
import dev.petuska.klip.plugin.config.KOTLIN_PLUGIN_ID
import dev.petuska.klip.plugin.util.KlipOption
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * Command line processor responsible for registering and retrieving values passed by gradle plugin
 */
@AutoService(CommandLineProcessor::class)
class KlipCommandLineProcessor : CommandLineProcessor {
  override val pluginId: String = KOTLIN_PLUGIN_ID

  override val pluginOptions: Collection<CliOption> =
      setOf(
          CliOption(
              optionName = KlipOption.Enabled.name,
              valueDescription = KlipOption.Enabled.valueDescription,
              description = KlipOption.Enabled.description,
              required = true),
          CliOption(
              optionName = KlipOption.Update.name,
              valueDescription = KlipOption.Update.valueDescription,
              description = KlipOption.Update.description,
              required = true),
          CliOption(
              optionName = KlipOption.KlipAnnotation.name,
              valueDescription = KlipOption.KlipAnnotation.valueDescription,
              description = KlipOption.KlipAnnotation.description,
              allowMultipleOccurrences = true,
              required = false,
          ),
          CliOption(
              optionName = KlipOption.ScopeAnnotation.name,
              valueDescription = KlipOption.ScopeAnnotation.valueDescription,
              description = KlipOption.ScopeAnnotation.description,
              allowMultipleOccurrences = true,
              required = false,
          ),
          CliOption(
              optionName = KlipOption.ScopeFunction.name,
              valueDescription = KlipOption.ScopeFunction.valueDescription,
              description = KlipOption.ScopeFunction.description,
              allowMultipleOccurrences = true,
              required = false,
          ),
      )

  override fun processOption(
      option: AbstractCliOption,
      value: String,
      configuration: CompilerConfiguration
  ) {
    when (option.optionName) {
      KlipOption.Enabled.name -> configuration.put(KlipOption.Enabled.key, value.toBoolean())
      KlipOption.Update.name -> configuration.put(KlipOption.Update.key, value.toBoolean())
      KlipOption.KlipAnnotation.name ->
          configuration.appendList(KlipOption.KlipAnnotation.key, value)
      KlipOption.ScopeAnnotation.name ->
          configuration.appendList(KlipOption.ScopeAnnotation.key, value)
      KlipOption.ScopeFunction.name -> configuration.appendList(KlipOption.ScopeFunction.key, value)
      else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
    }
  }
}
