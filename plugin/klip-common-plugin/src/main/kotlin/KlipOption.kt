package dev.petuska.klip.plugin

/**
 * Internal-use class containing information about command line options passed by gradle plugin to kotlin plugin
 */
sealed class KlipOption<T>(
  val name: String,
  val valueDescription: String,
  val description: String,
  val default: T,
) {

  /**
   * Toggles the compiler processing on/off
   */
  object Enabled : KlipOption<Boolean>(
    name = "enabled",
    valueDescription = "<true|false>",
    description = "whether the plugin is enabled",
    default = true,
  )

  /**
   * Specified the kotlin sources root to be used to build klip paths
   */
  object Root : KlipOption<String?>(
    name = "root",
    valueDescription = "<path>",
    description = "path to sources root",
    default = null,
  )

  /**
   * Value passed to "klippable" functions to indicate that klips should be updated
   */
  object Update : KlipOption<Boolean>(
    name = "update",
    valueDescription = "<true|false>",
    description = "whether the klips should be updated",
    default = false,
  )

  /**
   * Registers an annotation to be used to identify "klippable" functions
   */
  object KlipAnnotation : KlipOption<List<String>>(
    name = "annotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register for compiler processing",
    default = listOf("${KlipMap.group}.${KlipMap.name}.Klippable"),
  )

  /**
   * Registers an annotation to be used to identify "scope" functions
   * under which "klippable" annotation detection should happen
   */
  object ScopeAnnotation : KlipOption<List<String>>(
    name = "annotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register function scope for compiler klip detection and processing",
    default = listOf(
      "kotlin.Test",
      "org.junit.Test",
      "org.junit.jupiter.api.Test",
      "org.testng.annotations.Test"
    ),
  )
}
