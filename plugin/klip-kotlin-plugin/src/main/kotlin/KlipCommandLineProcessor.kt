package dev.petuska.klip.plugin

import com.google.auto.service.AutoService
import dev.petuska.klip.plugin.util.KlipKeys
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class KlipCommandLineProcessor : CommandLineProcessor {
  override val pluginId: String = KlipMap.kotlinPluginId

  override val pluginOptions: Collection<CliOption> = setOf(
    CliOption(
      optionName = KlipOption.Enabled.name,
      valueDescription = KlipOption.Enabled.valueDescription,
      description = KlipOption.Enabled.description,
    ),
    CliOption(
      optionName = KlipOption.Root.name,
      valueDescription = KlipOption.Root.valueDescription,
      description = KlipOption.Root.description,
    ),
    CliOption(
      optionName = KlipOption.Update.name,
      valueDescription = KlipOption.Update.valueDescription,
      description = KlipOption.Update.description,
    ),
    CliOption(
      optionName = KlipOption.Function.name,
      valueDescription = KlipOption.Function.valueDescription,
      description = KlipOption.Function.description,
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
      KlipOption.Enabled.name -> configuration.put(KlipKeys.KEY_ENABLED, value.toBoolean())
      KlipOption.Root.name -> configuration.put(KlipKeys.KEY_ROOT, value)
      KlipOption.Update.name -> configuration.put(KlipKeys.KEY_UPDATE, value.toBoolean())
      KlipOption.Function.name -> configuration.appendList(KlipKeys.KEY_FUNCTION, value)
      else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
    }
  }
}
