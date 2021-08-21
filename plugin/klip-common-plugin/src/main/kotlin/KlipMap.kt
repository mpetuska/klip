package dev.petuska.klip.plugin

/**
 * Internal-purpose object containing build information
 */
object KlipMap {
  private fun readFromResource(path: String): String? {
    return this::class.java.getResource(path)?.readText()
  }

  /**
   * Project group
   */
  val group by lazy {
    readFromResource("/klip.group")!!
  }

  /**
   * Project name
   */
  val name by lazy {
    readFromResource("/klip.name")!!
  }

  /**
   * Project version
   */
  val version by lazy {
    readFromResource("/klip.version")!!
  }

  /**
   * Artifact ID of the gradle plugin
   */
  val gradlePluginArtifactId by lazy {
    readFromResource("/klip.gradlePluginArtifactId")!!
  }

  /**
   * Artifact ID of the kotlin plugin for JVM & JS
   */
  val kotlinPluginArtifactId by lazy {
    readFromResource("/klip.kotlinPluginArtifactId")!!
  }

  /**
   * Artifact ID of the kotlin plugin for Native
   */
  val kotlinNativePluginArtifactId by lazy {
    readFromResource("/klip.kotlinNativePluginArtifactId")!!
  }

  /**
   * Unique kotlin plugin ID
   */
  val kotlinPluginId by lazy {
    "$group.$kotlinPluginArtifactId"
  }
}
