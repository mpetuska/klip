package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.NAME
import dev.petuska.klip.plugin.config.VERSION
import dev.petuska.klip.plugin.task.KlipServerTask
import dev.petuska.klip.plugin.util.KlipOption
import org.gradle.api.Project
import org.gradle.api.Task
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
      val server = startServer()
      val stopServer = stopServer(server)
      project.gradle.buildFinished {
        server.stop()
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

  private fun Project.startServer(): KlipServerTask {
    val extension = rootKlip
    val server = rootProject.tasks.maybeCreate("startKlipServer", KlipServerTask::class.java).apply {
      port.convention(extension.port)
    }
    return server
  }

  private fun Project.stopServer(server: KlipServerTask): Task {
    val stopServer = rootProject.tasks.maybeCreate("stopKlipServer").apply {
      description = "Stops klip server"
      mustRunAfter(server)
      mustRunAfter(tasks.withType(AbstractTestTask::class.java))
      doLast {
        server.stop()
      }
    }
    return stopServer
  }

  private fun Project.createExtension(): KlipExtension {
    val commonConfig: KlipExtension.() -> Unit = {
      klipAnnotations.addAll(KlipOption.KlipAnnotation.default)
      scopeAnnotations.addAll(KlipOption.ScopeAnnotation.default)
      scopeFunctions.addAll(KlipOption.ScopeFunction.default)
      debug.convention(KlipOption.Debug.default)
      enabled.convention(KlipOption.Enabled.default)
      update.convention(KlipOption.Update.default)
    }
    rootProject.extensions.findByType(KlipRootExtension::class.java)
      ?: rootProject.extensions.create(KlipExtension.NAME, KlipRootExtension::class.java).apply {
        port.convention(6969)
        commonConfig()
      }
    val klip = extensions.findByType(KlipExtension::class.java)
      ?: extensions.create(KlipExtension.NAME, KlipExtension::class.java).apply(commonConfig)
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
    val rootExtension = project.rootProject.rootKlip
    val extension = project.klip

    return project.provider {
      val debugFile = if (extension.debug.get()) {
        project.buildDir.resolve("klip-${kotlinCompilation.compileKotlinTaskName}.log").absolutePath
      } else ""
      listOfNotNull(
        SubpluginOption(key = KlipOption.Enabled.name, value = extension.enabled.get().toString()),
        SubpluginOption(key = KlipOption.Update.name, value = extension.update.get().toString()),
        SubpluginOption(key = KlipOption.ServerUrl.name, value = "http://localhost:${rootExtension.port.get()}"),
        SubpluginOption(key = KlipOption.Debug.name, value = debugFile),
      ) + extension.klipAnnotations.get()
        .map { SubpluginOption(key = KlipOption.KlipAnnotation.name, value = it) } + extension.scopeAnnotations.get()
        .map { SubpluginOption(key = KlipOption.ScopeAnnotation.name, value = it) } + extension.scopeFunctions.get()
        .map { SubpluginOption(key = KlipOption.ScopeFunction.name, value = it) }
    }
  }
}
