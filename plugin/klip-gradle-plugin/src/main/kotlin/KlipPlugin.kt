package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.KOTLIN_NATIVE_PLUGIN_ARTEFACT_ID
import dev.petuska.klip.plugin.config.KOTLIN_PLUGIN_ARTEFACT_ID
import dev.petuska.klip.plugin.config.KOTLIN_PLUGIN_ID
import dev.petuska.klip.plugin.config.VERSION
import dev.petuska.klip.plugin.util.KlipOption
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/** A kotlin gradle sub-plugin to manage klip kotlin compiler plugin */
class KlipPlugin : KotlinCompilerPluginSupportPlugin {
  override fun apply(target: Project) {
    with(target) {
      val extension = createExtension()
      tasks.withType(Test::class.java) {
        it.inputs.property("klip.update", "${extension.update}")
        it.inputs.property("klip.enabled", "${extension.enabled}")
        it.environment("KLIP_UPDATE", "${extension.update}")
      }
    }
  }

  private fun Project.createExtension() =
      extensions.findByType(KlipExtension::class.java)
          ?: extensions.create(KlipExtension.NAME, KlipExtension::class.java, this@createExtension)

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
      kotlinCompilation.target.project.plugins.hasPlugin(KlipPlugin::class.java)

  override fun getCompilerPluginId(): String = KOTLIN_PLUGIN_ID

  override fun getPluginArtifact(): SubpluginArtifact =
      SubpluginArtifact(
          groupId = GROUP,
          artifactId = KOTLIN_PLUGIN_ARTEFACT_ID,
          version = VERSION,
      )

  override fun getPluginArtifactForNative(): SubpluginArtifact =
      SubpluginArtifact(
          groupId = GROUP,
          artifactId = KOTLIN_NATIVE_PLUGIN_ARTEFACT_ID,
          version = VERSION,
      )

  override fun applyToCompilation(
      kotlinCompilation: KotlinCompilation<*>
  ): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    val extension = project.klip

    return project.provider {
      listOf(
          SubpluginOption(
              key = KlipOption.Enabled.name, lazyValue = lazy { extension.enabled.toString() }),
          SubpluginOption(
              key = KlipOption.Update.name, lazyValue = lazy { extension.update.toString() }),
      ) +
          extension.klipAnnotations.map {
            SubpluginOption(key = KlipOption.KlipAnnotation.name, value = it)
          } +
          extension.scopeAnnotations.map {
            SubpluginOption(key = KlipOption.ScopeAnnotation.name, value = it)
          } +
          extension.scopeFunctions.map {
            SubpluginOption(key = KlipOption.ScopeFunction.name, value = it)
          }
    }
  }
}
