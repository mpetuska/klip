package dev.petuska.klip.core

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.core.domain.Klip
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

internal actual object KlipManager {
  private val client by lazy {
    HttpClient {
      install(ContentNegotiation) {
        json()
      }
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private val KlipDispatcher = Dispatchers.Default.limitedParallelism(1)

  actual suspend fun writeKlip(
    context: KlipContext,
    klip: Klip,
  ) {
    withContext(KlipDispatcher) {
      client.post(context.serverUrl) {
        expectSuccess = true
        parameter("path", context.path)
        parameter("key", context.key)
        contentType(ContentType.Application.Json)
        setBody(klip)
      }
    }
  }

  actual suspend fun readKlip(
    context: KlipContext,
  ): Klip? {
    return withContext(KlipDispatcher) {
      val response = client.get(context.serverUrl) {
        parameter("path", context.path)
        parameter("key", context.key)
      }

      when (response.status) {
        HttpStatusCode.NotFound -> null
        HttpStatusCode.OK -> response.body()
        else -> error("Klip server error ${response.status}")
      }
    }
  }
}
