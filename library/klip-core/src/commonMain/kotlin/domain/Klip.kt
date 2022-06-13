package dev.petuska.klip.core.domain

public typealias Klips = MutableMap<String, Klip>

public data class Klip(
  val data: String,
  val attributes: Map<String, String> = mapOf(),
)
