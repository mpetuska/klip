package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.NAME
import dev.petuska.klip.plugin.config.VERSION
import dev.petuska.klip.plugin.task.KlipServerTask
import dev.petuska.klip.plugin.util.KlipOption
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.testing.AbstractTestTask
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/** A kotlin gradle sub-plugin to manage klip kotlin compiler plugin */
class KlipPlugin : KotlinCompilerPluginSupportPlugin {
  override fun apply(target: Project) {
    with(target) {
      val extension = createExtension()
      val server = tasks.register("startKlipServer", KlipServerTask::class.java) {
        it.port.convention(extension.port)
        it.destroyables
      }
      val stopServer = tasks.register("stopKlipServer") {
        it.description = "Stops klip server"
        it.mustRunAfter(server)
        it.mustRunAfter(tasks.withType(AbstractTestTask::class.java))
        it.doLast {
          server.get().stop()
        }
      }
      project.gradle.buildFinished {
        server.get().stop()
      }
      tasks.withType(AbstractTestTask::class.java) {
        it.dependsOn(server)
        it.finalizedBy(stopServer)
        it.inputs.property("klip.enabled", extension.enabled)
        it.inputs.property("klip.update", extension.update)
        it.inputs.property("klip.debug", extension.debug)
      }
      tasks.withType(AbstractCompile::class.java) {
        it.finalizedBy(stopServer)
      }
    }
  }

  private fun Project.createExtension(): KlipExtension {
    val klip = extensions.findByType(KlipExtension::class.java)
      ?: extensions.create(KlipExtension.NAME, KlipExtension::class.java)
    klip.klipAnnotations.addAll(KlipOption.KlipAnnotation.default)
    klip.scopeAnnotations.addAll(KlipOption.ScopeAnnotation.default)
    klip.scopeFunctions.addAll(KlipOption.ScopeFunction.default)
    klip.debug.convention(KlipOption.Debug.default)
    klip.port.convention(6969)
    klip.enabled.convention(KlipOption.Enabled.default)
    klip.update.convention(KlipOption.Update.default)

    return klip
  }

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

  override fun getCompilerPluginId(): String = "$GROUP.$NAME-kotlin-plugin"

  override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
    groupId = GROUP,
    artifactId = "$NAME-kotlin-plugin",
    version = VERSION,
  )

  override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
    groupId = GROUP,
    artifactId = "$NAME-kotlin-plugin-native",
    version = VERSION,
  )

  override fun applyToCompilation(
    kotlinCompilation: KotlinCompilation<*>
  ): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    val extension = project.klip

    return project.provider {
      val debugFile = if (extension.debug.get()) {
        project.buildDir.resolve("klip-${kotlinCompilation.compileKotlinTaskName}.log").absolutePath
      } else ""
      listOfNotNull(
        SubpluginOption(key = KlipOption.Enabled.name, value = extension.enabled.get().toString()),
        SubpluginOption(key = KlipOption.Update.name, value = extension.update.get().toString()),
        SubpluginOption(key = KlipOption.ServerUrl.name, value = "http://localhost:${extension.port.get()}"),
        SubpluginOption(key = KlipOption.Debug.name, value = debugFile),
      ) +
        extension.klipAnnotations.get().map { SubpluginOption(key = KlipOption.KlipAnnotation.name, value = it) } +
        extension.scopeAnnotations.get().map { SubpluginOption(key = KlipOption.ScopeAnnotation.name, value = it) } +
        extension.scopeFunctions.get().map { SubpluginOption(key = KlipOption.ScopeFunction.name, value = it) }
    }
  }
}
