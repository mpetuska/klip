package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.delegate.propertyDelegate
import org.gradle.api.Project

open class KlipExtension(
  project: Project,
) {
  /**
   * Whether plugin is enabled
   */
  var enabled by project.propertyDelegate(default = KlipOption.Enabled.default) { it.toBoolean() }

  /**
   * Whether the klips should be updated
   */
  var update by project.propertyDelegate(default = KlipOption.Update.default) { it.toBoolean() }

  /**
   * Fully qualified annotation names to register for compiler processing and path injection
   */
  var klipAnnotations by project.propertyDelegate(default = KlipOption.KlipAnnotation.default) { it.split(",").toSet() }

  fun klipAnnotation(fqName: String) {
    klipAnnotations += fqName
  }

  /**
   * Fully qualified annotation names to register function scopes for compiler klip detection and processing
   */
  var scopeAnnotations by project.propertyDelegate(default = KlipOption.ScopeAnnotation.default) { it.split(",").toSet() }

  fun scopeAnnotation(fqName: String) {
    scopeAnnotations += fqName
  }

  companion object {
    val NAME = KlipMap.name
  }
}

internal val Project.klip: KlipExtension
  get() = extensions.findByType(KlipExtension::class.java)
    ?: throw IllegalStateException("${KlipExtension.NAME} is not of the correct type")

internal fun Project.klip(config: KlipExtension.() -> Unit): KlipExtension =
  klip.apply(config)
