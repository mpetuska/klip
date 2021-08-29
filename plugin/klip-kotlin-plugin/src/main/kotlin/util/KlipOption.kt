package dev.petuska.klip.plugin.util

import org.jetbrains.kotlin.config.CompilerConfigurationKey

/**
 * Internal-use class containing information about command line options passed by gradle plugin to kotlin plugin
 */
sealed class KlipOption<T>(
  val name: String,
  val valueDescription: String,
  val description: String,
) {
  val key: CompilerConfigurationKey<T> = CompilerConfigurationKey(name)

  /**
   * Toggles the compiler processing on/off
   */
  object Enabled : KlipOption<Boolean>(
    name = "enabled",
    valueDescription = "<true|false>",
    description = "whether the plugin is enabled",
  )

  /**
   * Value passed to "klippable" functions to indicate that klips should be updated
   */
  object Update : KlipOption<Boolean>(
    name = "update",
    valueDescription = "<true|false>",
    description = "whether the klips should be updated",
  )

  /**
   * Registers an annotation to be used to identify "klippable" functions
   */
  object KlipAnnotation : KlipOption<List<String>>(
    name = "klipAnnotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register for compiler processing",
  )

  /**
   * Registers an annotation to be used to identify "scope" functions
   * under which "klippable" annotation detection should happen
   */
  object ScopeAnnotation : KlipOption<List<String>>(
    name = "scopeAnnotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register function scope for compiler klip detection and processing",
  )
}