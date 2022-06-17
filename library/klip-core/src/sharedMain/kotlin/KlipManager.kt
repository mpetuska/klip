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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KType

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

  actual suspend fun <T> writeKlip(
    context: KlipContext,
    klip: Klip<T>,
  ) {
    withContext(KlipDispatcher) {
      val data = Json.encodeToJsonElement(Json.serializersModule.serializer(klip.type), klip.data)
      val attributes = Json.encodeToJsonElement(klip.attributes)
      client.post(context.serverUrl) {
        expectSuccess = true
        parameter("path", context.path)
        parameter("key", context.key)
        contentType(ContentType.Application.Json)
        setBody(
          JsonObject(
            mapOf(
              "data" to data,
              "attributes" to attributes,
            )
          )
        )
      }
    }
  }

  actual suspend fun <T> readKlip(
    context: KlipContext,
    type: KType,
  ): Klip<T>? = withContext(KlipDispatcher) {
    val response = client.get(context.serverUrl) {
      parameter("path", context.path)
      parameter("key", context.key)
    }

    val kJson = when (response.status) {
      HttpStatusCode.NotFound -> null
      HttpStatusCode.OK -> response.body<JsonObject>()
      else -> error("Klip server error ${response.status}")
    }
    kJson?.let {
      @Suppress("UNCHECKED_CAST")
      Klip(
        type = type,
        data = Json.decodeFromJsonElement(Json.serializersModule.serializer(type), kJson["data"]!!),
        attributes = Json.decodeFromJsonElement(kJson["attributes"]!!),
      ) as Klip<T>
    }
  }
}
