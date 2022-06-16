package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.NAME
import dev.petuska.klip.plugin.config.VERSION
import dev.petuska.klip.plugin.server.KlipServerService
import dev.petuska.klip.plugin.task.KlipServerStartTask
import dev.petuska.klip.plugin.util.KlipOption
import dev.petuska.klip.plugin.util.sysOrGradleOrEnvConvention
import org.gradle.api.Project
import org.gradle.api.provider.Provider
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
      val server = project.gradle.sharedServices.registerIfAbsent(KlipExtension.NAME, KlipServerService::class.java) {
        it.parameters.port.set(rootKlip.port)
        it.parameters.rootDir.set(rootProject.layout.projectDirectory)
      }
      val startServer = rootProject.tasks.maybeCreate("startKlipServer", KlipServerStartTask::class.java).also {
        it.usesService(server)
        it.service.set(server)
      }
      tasks.withType(AbstractTestTask::class.java) {
        it.dependsOn(startServer)
        it.usesService(server)
        it.inputs.property("klip.enabled", extension.enabled)
        it.inputs.property("klip.update", extension.update)
        it.inputs.property("klip.debug", extension.debug)
      }
      configurations.all {
        it.resolutionStrategy.eachDependency { dep ->
          if (dep.requested.group == GROUP && dep.requested.name.startsWith(NAME)) {
            dep.useVersion(VERSION)
          }
        }
      }
    }
  }

  private fun Project.createExtension(): KlipExtension {
    val commonConfig: KlipExtension.() -> Unit = {
      klipAnnotations.addAll(KlipOption.KlipAnnotation.default)
      scopeAnnotations.addAll(KlipOption.ScopeAnnotation.default)
      scopeFunctions.addAll(KlipOption.ScopeFunction.default)
      debug.sysOrGradleOrEnvConvention(
        project = project,
        propName = "klip.debug",
        envName = "KLIP_DEBUG",
        default = { KlipOption.Debug.default },
        converter = { !"false".equals(it, true) },
      )
      enabled.sysOrGradleOrEnvConvention(
        project = project,
        propName = "klip.enabled",
        envName = "KLIP_ENABLED",
        default = { KlipOption.Enabled.default },
        converter = { !"false".equals(it, true) },
      )
      update.sysOrGradleOrEnvConvention(
        project = project,
        propName = "klip.update",
        envName = "KLIP_UPDATE",
        default = { KlipOption.Update.default },
        converter = { !"false".equals(it, true) },
      )
    }
    rootProject.extensions.findByType(KlipRootExtension::class.java)
      ?: rootProject.extensions.create(KlipExtension.NAME, KlipRootExtension::class.java).apply {
        port.sysOrGradleOrEnvConvention(
          project = project,
          propName = "klip.port",
          envName = "KLIP_PORT",
          default = { 6969 },
          converter = String::toInt,
        )
        commonConfig()
      }
    return extensions.findByType(KlipExtension::class.java) ?: extensions.create(
      KlipExtension.NAME,
      KlipExtension::class.java
    ).apply(commonConfig)
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
