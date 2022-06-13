package dev.petuska.klip.core.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

public actual inline fun <reified T> Klip.typed(): TypedKlip<T> = TypedKlip(
  data = Json.decodeFromString(data),
  attributes = attributes
)

public actual inline fun <reified T> TypedKlip<T>.raw(): Klip = Klip(
  data = Json.encodeToString(data),
  attributes = attributes
)
