package dev.petuska.klip.plugin

object KlipMap {
  private fun readFromResource(path: String): String? {
    return this::class.java.getResource(path)?.readText()
  }

  val name by lazy {
    readFromResource("/klip.name")!!
  }
  val version by lazy {
    readFromResource("/klip.version")!!
  }
  val group by lazy {
    readFromResource("/klip.group")!!
  }

  val gradlePluginArtifactId by lazy {
    readFromResource("/klip.gradlePluginArtifactId")!!
  }

  val kotlinPluginArtifactId by lazy {
    readFromResource("/klip.kotlinPluginArtifactId")!!
  }

  val kotlinNativePluginArtifactId by lazy {
    readFromResource("/klip.kotlinNativePluginArtifactId")!!
  }

  val kotlinPluginId by lazy {
    "$group.$kotlinPluginArtifactId"
  }
}
