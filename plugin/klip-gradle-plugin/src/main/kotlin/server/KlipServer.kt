package dev.petuska.klip.plugin.server

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.Closeable
import java.io.File

class KlipServer(
  projectDir: File,
  port: Int,
) : Closeable {
  private val json = Json {
    prettyPrint = true
  }

  override fun close() = engine.stop()
  fun start(wait: Boolean = false) {
    engine.start(wait = wait)
  }

  private val engine: ApplicationEngine = embeddedServer(CIO, port = port) {
    install(ContentNegotiation) {
      json()
    }
    routing {
      get {
        val klipPath = call.request.queryParameters["path"]
          ?: throw BadRequestException("Missing path parameter")
        val klipKey = call.request.queryParameters["key"]
          ?: throw BadRequestException("Missing key parameter")
        val klipFile = File(klipPath)
        if (!klipFile.startsWith(projectDir)) {
          throw BadRequestException("Klip path is not within the project bounds ${projectDir.absolutePath}")
        }
        if (!klipFile.exists()) throw NotFoundException("Klip file does not exist")
        val klips: JsonObject = json.decodeFromString(klipFile.readText())
        val klip = klips[klipKey] ?: throw NotFoundException("Klip does not exist")
        call.respond(klip)
      }
      post {
        val klipPath = call.request.queryParameters["path"]
          ?: throw BadRequestException("Missing path parameter")
        val klipKey = call.request.queryParameters["key"]
          ?: throw BadRequestException("Missing key parameter")
        withContext(Dispatchers.IO) {
          val klipFile = File(klipPath)
          if (!klipFile.startsWith(projectDir)) {
            throw BadRequestException("Klip path is not within the project bounds ${projectDir.absolutePath}")
          }
          val klip: JsonObject = call.receive()
          val klips: JsonObject = if (klipFile.exists()) {
            json.decodeFromString(klipFile.readText())
          } else JsonObject(mapOf())
          val newKlips = klips.plus(klipKey to klip)
          klipFile.parentFile.mkdirs()
          klipFile.writeText(json.encodeToString(JsonObject(newKlips)))
        }
        call.respond(HttpStatusCode.Accepted)
      }
    }
  }
}
