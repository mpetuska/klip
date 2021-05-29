package dev.petuska.klip

import dev.petuska.klip.delegate.propertyDelegate
import org.gradle.api.Project
import java.io.File

open class KlipExtension(
  private val project: Project,
) {
  /**
   * Project sources root
   */
  val root by project.propertyDelegate(default = project.projectDir.resolve("src")) { File(it) }

  val update by project.propertyDelegate(default = false) { true }

  companion object {
    const val NAME = "klip"
    val version by lazy {
      this::class.java.getResource("/version")!!.readText()
    }
    val group by lazy {
      this::class.java.getResource("/group")!!.readText()
    }
  }
}

internal val Project.klip: KlipExtension
  get() = extensions.getByName(KlipExtension.NAME) as? KlipExtension
    ?: throw IllegalStateException("${KlipExtension.NAME} is not of the correct type")

internal fun Project.klip(config: KlipExtension.() -> Unit = {}): KlipExtension =
  klip.apply(config)
