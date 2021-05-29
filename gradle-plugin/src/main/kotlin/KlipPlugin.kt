package dev.petuska.klip

import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

/**
 * A simple 'hello world' plugin.
 */
class KlipPlugin : Plugin<Project> {
  companion object {
    private const val KOTLIN_JS_PLUGIN = "org.jetbrains.kotlin.js"
    private const val KOTLIN_JVM_PLUGIN = "org.jetbrains.kotlin.jvm"
    private const val KOTLIN_MPP_PLUGIN = "org.jetbrains.kotlin.multiplatform"
  }

  override fun apply(project: Project) {
    with(project) {
      val extension = createExtension()
      pluginManager.apply(KspGradleSubplugin::class.java)
      // TODO Must change when introducing MPP
      configurations.findByName("ksp")
        ?.dependencies
        ?.add(dependencies.create("${KlipExtension.group}:klip-processor:${KlipExtension.version}"))

      afterEvaluate {
        extensions.findByType(KspExtension::class.java)?.arg("klip.root", extension.root.canonicalPath)
        pluginManager.withPlugin(KOTLIN_MPP_PLUGIN) {
          extensions.configure(KotlinMultiplatformExtension::class.java) {
            it.targets.filter { target ->
              target.isSupported()
            }.forEach { target ->
              configureTarget(extension, it.sourceSets, target)
            }
          }
        }
        pluginManager.withPlugin(KOTLIN_JVM_PLUGIN) {
          extensions.configure(KotlinJvmProjectExtension::class.java) {
            configureTarget(extension, it.sourceSets, it.target)
          }
        }
        pluginManager.withPlugin(KOTLIN_JS_PLUGIN) {
          extensions.configure(KotlinJsProjectExtension::class.java) {
            @Suppress("DEPRECATION")
            (
              it._target?.let { target ->
                configureTarget(extension, it.sourceSets, target)
              }
              )
          }
        }
        tasks.withType(Test::class.java) {
          if (extension.update) {
            it.environment(KlipManager.UPDATE_ENV_VAR_NAME, "${true}")
          }
        }
      }
    }
  }

  private fun KotlinTarget.isSupported() = this is KotlinJvmTarget // || this is KotlinJsTarget

  private fun Project.createExtension() = extensions.findByType(KlipExtension::class.java) ?: extensions.create(
    KlipExtension.NAME,
    KlipExtension::class.java,
    this@createExtension
  )

  private fun Project.configureTarget(
    extension: KlipExtension,
    sourceSets: NamedDomainObjectContainer<KotlinSourceSet>,
    target: KotlinTarget
  ) {
    extensions.findByType(KspExtension::class.java).let { ksp ->
      target.compilations.filter { it.name.contains("test", true) }.forEach { compilation ->
        sourceSets.findByName(compilation.defaultSourceSetName)?.let { ss ->
          val klipRoot = extension.root.resolve("${ss.name}/klips").canonicalPath
          ss.resources.srcDir(klipRoot)
          ksp?.arg("klip.root.${ss.name}", klipRoot)
          ss.dependencies {
            implementation("${KlipExtension.group}:klip-core:${KlipExtension.version}")
          }
        }
      }
    }
  }
}
