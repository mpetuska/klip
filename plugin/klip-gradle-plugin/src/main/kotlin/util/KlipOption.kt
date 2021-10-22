package dev.petuska.klip.plugin.util

/**
 * Internal-use class containing information about command line options passed by gradle plugin to
 * kotlin plugin
 */
sealed class KlipOption<T>(
    val name: String,
    val default: T,
) {

  /** Toggles the compiler processing on/off */
  object Enabled :
      KlipOption<Boolean>(
          name = "enabled",
          default = true,
      )

  /** Value passed to "klippable" functions to indicate that klips should be updated */
  object Update :
      KlipOption<Boolean>(
          name = "update",
          default = false,
      )

  /** Registers an annotation to be used to identify "klippable" functions */
  object KlipAnnotation :
      KlipOption<List<String>>(
          name = "klipAnnotation",
          default = listOf("dev.petuska.klip.core.Klippable"),
      )

  /**
   * Registers an annotation to be used to identify "scope" functions under which "klippable"
   * annotation detection should happen
   */
  object ScopeAnnotation :
      KlipOption<List<String>>(
          name = "scopeAnnotation",
          default =
              listOf(
                  "kotlin.test.Test",
                  "org.junit.Test",
                  "org.junit.jupiter.api.Test",
                  "org.testng.annotations.Test"),
      )
}
