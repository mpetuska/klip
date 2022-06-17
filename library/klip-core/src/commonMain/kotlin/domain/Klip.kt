package dev.petuska.klip.core.domain

import kotlin.jvm.Transient
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public typealias Klips = Map<String, Klip<*>>
public typealias MutableKlips = MutableMap<String, Klip<*>>

public data class Klip<T>(
  @Transient
  val type: KType,
  val data: @Serializable T,
  val attributes: Map<String, String>,
)

@Suppress("FunctionNaming")
public inline fun <reified T> Klip(
  data: T,
  attributes: Map<String, String> = mapOf(),
): Klip<T> = Klip(data = data, attributes = attributes, type = typeOf<T>())
