package dev.petuska.klip.plugin.delegate

import java.util.Locale
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import org.gradle.api.Project

internal class PropertyDelegate<V>(
    private val project: Project,
    private val prefix: String?,
    private val converter: (String) -> V?,
    private val default: V
) : ReadWriteProperty<Any, V> {
  private fun Project.propertyOrNull(name: String): String? =
      if (hasProperty(name)) {
        property(name)?.toString()
      } else null

  private var value: V? = null

  override fun getValue(thisRef: Any, property: KProperty<*>): V {
    value =
        value
            ?: (project.propertyOrNull(property.buildPropertyName())
                    ?: System.getenv(property.buildEnvName()))
                ?.toString()
                ?.let(converter)
    return value ?: default
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
    this.value = value
  }

  private fun KProperty<*>.buildPropertyName(): String {
    return "$PROP_BASE${
      prefix?.removeSuffix(".")?.removePrefix(".")?.let { ".$it" } ?: ""
    }.$name"
  }

  private fun KProperty<*>.buildEnvName(): String {
    return buildPropertyName().replace("[- .]".toRegex(), "_").uppercase(Locale.getDefault())
  }

  companion object {
    private const val PROP_BASE = "klip"
  }
}
