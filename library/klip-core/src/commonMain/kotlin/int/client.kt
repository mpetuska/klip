package dev.petuska.klip.core.int

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.cbor.cbor
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
internal val client = HttpClient {
  install(ContentNegotiation) {
    cbor()
  }
  defaultRequest {
  }
}
