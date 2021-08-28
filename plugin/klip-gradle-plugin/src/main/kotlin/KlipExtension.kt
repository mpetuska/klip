package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.delegate.propertyDelegate
import dev.petuska.klip.plugin.util.KlipOption
import org.gradle.api.Project

/**
 * Gradle extension to manage klip plugin properties
 */
open class KlipExtension(project: Project) {
  /**
   * Whether plugin is enabled
   */
  var enabled by project.propertyDelegate(default = KlipOption.Enabled.default) { it.toBoolean() }

  /**
   * Whether the klips should be updated
   */
  var update by project.propertyDelegate(default = KlipOption.Update.default) { it.toBoolean() }

  /**
   * Fully qualified annotation names to register for compiler processing and path injection.
   * Setting this property gives you full control over the annotations and overrides the default ones.
   */
  var klipAnnotations by project.propertyDelegate(default = KlipOption.KlipAnnotation.default) { it.split(",").toSet() }

  /**
   * Register an annotation for compiler processing and path injection
   * @param fqName fully qualified annotation name to register
   */
  fun klipAnnotation(fqName: String) {
    klipAnnotations += fqName
  }

  /**
   * Fully qualified annotation names to register function scopes for compiler klip detection and processing
   * Setting this property gives you full control over the annotations and overrides the default ones.
   */
  var scopeAnnotations by project.propertyDelegate(default = KlipOption.ScopeAnnotation.default) {
    it.split(",").toSet()
  }

  /**
   * Register an annotation for function scopes for compiler klip detection and processing
   * @param fqName fully qualified annotation name to register
   */
  fun scopeAnnotation(fqName: String) {
    scopeAnnotations += fqName
  }

  companion object {
    /**
     * Extension name
     */
    const val NAME = dev.petuska.klip.plugin.config.NAME
  }
}

/**
 * Klip plugin extension
 * @throws IllegalStateException if the plugin did not register an extension yet
 */
val Project.klip: KlipExtension
  get() = extensions.findByType(KlipExtension::class.java)
    ?: throw IllegalStateException("${KlipExtension.NAME} is not of the correct type")

/**
 * Configure klip plugin extension
 * @throws IllegalStateException if the plugin did not register an extension yet
 */
internal fun Project.klip(config: KlipExtension.() -> Unit): KlipExtension =
  klip.apply(config)
