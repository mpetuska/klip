package dev.petuska.klip.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/**
 * A simple 'hello world' plugin.
 */
class KlipPlugin : KotlinCompilerPluginSupportPlugin {
  override fun apply(target: Project) {
    with(target) {
      val extension = createExtension()
      tasks.withType(Test::class.java) {
        if (extension.update) {
          it.environment("KLIP_UPDATE", "${true}")
        }
      }
    }
  }

  private fun Project.configureTarget(
    extension: KlipExtension,
    target: KotlinTarget
  ) {
    target.compilations.forEach {
      val sourceSetRoot = it.defaultSourceSet.kotlin.sourceDirectories.first()
      it.defaultSourceSet.resources.srcDir(sourceSetRoot.resolve("../klips"))
    }
  }

  private fun Project.createExtension() = extensions.findByType(KlipExtension::class.java) ?: extensions.create(
    KlipExtension.NAME,
    KlipExtension::class.java,
    this@createExtension
  )

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
    kotlinCompilation.target.project.plugins.hasPlugin(KlipPlugin::class.java)

  override fun getCompilerPluginId(): String = KlipMap.kotlinPluginId

  override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
    groupId = KlipMap.group,
    artifactId = KlipMap.kotlinPluginArtifactId,
    version = KlipMap.version,
  )

  override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
    groupId = KlipMap.group,
    artifactId = KlipMap.kotlinNativePluginArtifactId,
    version = KlipMap.version,
  )

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    val extension = project.klip
    val sourceSetRoot = kotlinCompilation.defaultSourceSet.kotlin.sourceDirectories.first()
    kotlinCompilation.defaultSourceSet.resources.srcDir(sourceSetRoot.resolve("../klips"))

    return project.provider {
      listOf(
        SubpluginOption(key = KlipOption.Enabled.name, lazyValue = lazy { extension.enabled.toString() }),
        SubpluginOption(
          key = KlipOption.Root.name,
          value = sourceSetRoot.canonicalPath,
        ),
        SubpluginOption(key = KlipOption.Update.name, lazyValue = lazy { extension.update.toString() }),
      ) + (extension.klipAnnotations + KlipOption.KlipAnnotation.default).map {
        SubpluginOption(key = KlipOption.KlipAnnotation.name, value = it)
      } + extension.scopeAnnotations.map {
        SubpluginOption(key = KlipOption.ScopeAnnotation.name, value = it)
      }
    }
  }
}
