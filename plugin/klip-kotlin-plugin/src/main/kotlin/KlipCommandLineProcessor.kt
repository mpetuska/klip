package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.NAME
import dev.petuska.klip.plugin.util.KlipOption
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * Command line processor responsible for registering and retrieving values passed by gradle plugin
 */
class KlipCommandLineProcessor : CommandLineProcessor {
  override val pluginId: String = "$GROUP.$NAME-kotlin-plugin"

  override val pluginOptions: Collection<CliOption> = setOf(
    CliOption(
      optionName = KlipOption.Debug.name,
      valueDescription = KlipOption.Debug.valueDescription,
      description = KlipOption.Debug.description,
      required = false
    ),
    CliOption(
      optionName = KlipOption.ServerUrl.name,
      valueDescription = KlipOption.ServerUrl.valueDescription,
      description = KlipOption.ServerUrl.description,
      required = true
    ),
    CliOption(
      optionName = KlipOption.Enabled.name,
      valueDescription = KlipOption.Enabled.valueDescription,
      description = KlipOption.Enabled.description,
      required = true
    ),
    CliOption(
      optionName = KlipOption.Update.name,
      valueDescription = KlipOption.Update.valueDescription,
      description = KlipOption.Update.description,
      required = true
    ),
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
      KlipOption.Debug.name -> configuration.putIfNotNull(KlipOption.Debug.key, value.takeIf(String::isNotBlank))
      KlipOption.ServerUrl.name -> configuration.put(KlipOption.ServerUrl.key, value)
      KlipOption.Enabled.name -> configuration.put(KlipOption.Enabled.key, value.toBoolean())
      KlipOption.Update.name -> configuration.put(KlipOption.Update.key, value.toBoolean())
      KlipOption.KlipAnnotation.name -> configuration.appendList(KlipOption.KlipAnnotation.key, value)
      KlipOption.ScopeAnnotation.name -> configuration.appendList(KlipOption.ScopeAnnotation.key, value)
      KlipOption.ScopeFunction.name -> configuration.appendList(KlipOption.ScopeFunction.key, value)
      else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
    }
  }
}
