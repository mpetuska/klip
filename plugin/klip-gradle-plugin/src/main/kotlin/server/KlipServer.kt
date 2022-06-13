package dev.petuska.klip.plugin.server

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.cbor.cbor
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.Closeable
import java.io.File

class KlipServer(
  projectDir: File,
  port: Int,
) : Closeable {
  private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
  }

  override fun close() = engine.stop()
  fun start(wait: Boolean = false) {
    engine.start(wait = wait)
  }

  @OptIn(ExperimentalSerializationApi::class)
  private val engine: ApplicationEngine = embeddedServer(CIO, port = port) {
    install(ContentNegotiation) {
      cbor()
      json()
    }
    routing {
      route("klip") {
        get {
          val klipPath = call.request.queryParameters["path"]
            ?: throw BadRequestException("missing path parameter")
          call.respondFile(File(klipPath))
        }
        post {
          val klipPath = call.request.queryParameters["path"]
            ?: throw BadRequestException("missing path parameter")
          val klipFile = File(klipPath)
          if (!klipFile.startsWith(projectDir)) {
            throw BadRequestException("Klip path is not withing project bounds ${projectDir.absolutePath}")
          }
          klipFile.outputStream().use {
            json.encodeToStream(call.receive<Map<String, Any?>>(), it)
          }
          call.respond(HttpStatusCode.Accepted)
        }
      }
    }
  }
}
