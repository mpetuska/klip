package dev.petuska.klip.plugin.server

import io.ktor.server.engine.ApplicationEngine
import kotlinx.serialization.json.Json
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import java.io.Closeable

abstract class KlipServerService : BuildService<KlipServerService.Params>, OperationCompletionListener, Closeable {
  interface Params : BuildServiceParameters {
    val port: Property<Int>
    val rootDir: DirectoryProperty
  }

  private val json = Json {
    prettyPrint = true
  }

  private val engine: ApplicationEngine = klipServer(parameters.port, parameters.rootDir.asFile)

  fun start(wait: Boolean = false) {
    engine.start(wait)
  }

  override fun onFinish(event: FinishEvent) {
    close()
  }

  override fun close() {
    engine.stop()
  }
}
