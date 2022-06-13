package dev.petuska.klip.core.domain

public typealias Klips = Map<String, Klip>
public typealias MutableKlips = MutableMap<String, Klip>

@Serializable
public data class Klip(
  val data: String,
  val attributes: Map<String, String> = mapOf(),
)

public data class TypedKlip<T>(
  val data: T,
  val attributes: Map<String, String> = mapOf(),
)
