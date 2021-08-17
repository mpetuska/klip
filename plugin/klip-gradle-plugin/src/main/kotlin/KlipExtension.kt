package dev.petuska.klip.plugin

import dev.petuska.klip.plugin.delegate.propertyDelegate
import org.gradle.api.Project

open class KlipExtension(
  private val project: Project,
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
   * Fully qualified function names to register for compiler processing
   */
  var functions by project.propertyDelegate(default = KlipOption.Function.default) { it.split(",").toSet() }

  companion object {
    val NAME = KlipMap.name
  }
}

internal val Project.klip: KlipExtension
  get() = extensions.findByType(KlipExtension::class.java)
    ?: throw IllegalStateException("${KlipExtension.NAME} is not of the correct type")

internal fun Project.klip(config: KlipExtension.() -> Unit): KlipExtension =
  klip.apply(config)
