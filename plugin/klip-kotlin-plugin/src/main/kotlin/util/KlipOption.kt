package dev.petuska.klip.plugin.util

import org.jetbrains.kotlin.config.CompilerConfigurationKey

/**
 * Internal-use class containing information about command line options passed by gradle plugin to
 * kotlin plugin
 */
sealed class KlipOption<T>(
  val name: String,
  val valueDescription: String,
  val description: String,
) {
  val key: CompilerConfigurationKey<T> = CompilerConfigurationKey(name)

  object ServerUrl : KlipOption<String>(
    name = "server-url",
    valueDescription = "<scheme>://<host>:<port>",
    description = "Klip server url",
  )

  object Debug : KlipOption<String>(
    name = "debug",
    valueDescription = "<path>",
    description = "Debug log file path",
  )

  /** Toggles the compiler processing on/off */
  object Enabled : KlipOption<Boolean>(
    name = "enabled",
    valueDescription = "<true|false>",
    description = "whether the plugin is enabled",
  )

  /** Value passed to "klippable" functions to indicate that klips should be updated */
  object Update : KlipOption<Boolean>(
    name = "update",
    valueDescription = "<true|false>",
    description = "whether the klips should be updated",
  )

  /** Registers an annotation to be used to identify "klippable" functions */
  object KlipAnnotation : KlipOption<List<String>>(
    name = "klipAnnotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register for compiler processing",
  )

  /**
   * Registers an annotation to be used to identify "scope" functions under which "klippable"
   * annotation detection should happen
   */
  object ScopeAnnotation : KlipOption<List<String>>(
    name = "scopeAnnotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register function scope for compiler klip detection and processing",
  )

  /**
   * Registers a function to be used to identify "scope" functions under which "klippable" function
   * detection should happen
   */
  object ScopeFunction : KlipOption<List<String>>(
    name = "scopeFunction",
    valueDescription = "<fully qualified function name>",
    description = "function to register function scope for compiler klip detection and processing",
  )
}
