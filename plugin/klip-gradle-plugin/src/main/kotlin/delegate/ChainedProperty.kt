package dev.petuska.klip.plugin.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class ChainedProperty<R, V>(
  private var main: ReadWriteProperty<R, V?>,
  private val fallback: ReadWriteProperty<R, V>
) : ReadWriteProperty<R, V> {
  override fun getValue(thisRef: R, property: KProperty<*>): V {
    return main.getValue(thisRef, property) ?: fallback.getValue(thisRef, property)
  }

  override fun setValue(thisRef: R, property: KProperty<*>, value: V) {
    main.setValue(thisRef, property, value)
  }
}
