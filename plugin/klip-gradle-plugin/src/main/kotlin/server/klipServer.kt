package dev.petuska.klip.plugin.server

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.ApplicationRequest
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
import org.gradle.api.provider.Provider
import java.io.File

internal fun klipServer(port: Provider<Int>, rootDir: Provider<File>) = embeddedServer(CIO, port = port.get()) {
  install(ContentNegotiation) {
    json()
  }
  install(CORS) {
    anyHost()
  }
  routing {
    get {
      val klipPath = call.request.path
      val klipKey = call.request.key
      val klipFile = File(klipPath)
      if (!klipFile.startsWith(rootDir.get())) {
        throw BadRequestException("Klip path is not within the project bounds ${rootDir.get().absolutePath}")
      }
      if (!klipFile.exists()) throw NotFoundException("Klip file does not exist")
      val klips: JsonObject = json.decodeFromString(klipFile.readText())
      val klip = klips[klipKey] ?: throw NotFoundException("Klip does not exist")
      call.respond(klip)
    }
    post {
      val klipPath = call.request.path
      val klipKey = call.request.key
      withContext(Dispatchers.IO) {
        val klipFile = File(klipPath)
        if (!klipFile.startsWith(rootDir.get())) {
          throw BadRequestException("Klip path is not within the project bounds ${rootDir.get().absolutePath}")
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

private inline val ApplicationRequest.path: String
  get() = queryParameters["path"] ?: throw BadRequestException("Missing path parameter")

private inline val ApplicationRequest.key: String
  get() = queryParameters["key"] ?: throw BadRequestException("Missing key parameter")

private val json = Json {
  prettyPrint = true
}
