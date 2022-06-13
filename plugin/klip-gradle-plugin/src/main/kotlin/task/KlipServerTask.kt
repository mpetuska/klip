package dev.petuska.klip.plugin.task

import dev.petuska.container.task.AsyncExecTask
import dev.petuska.container.task.runner.AsyncRunner
import dev.petuska.klip.plugin.server.KlipServer
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.internal.service.ServiceRegistry
import org.slf4j.Logger
import java.io.File
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class KlipServerTask : AsyncExecTask<KlipServer, KlipServerTask.Runner>(
  handleType = Runner.Handle::class,
  logMarker = "klip-server"
) {
  @get:Input
  abstract val port: Property<Int>

  init {
    description = "Starts a server to manage klips state"
    async.setFinal(true)
    port.convention(0)
  }

  override fun buildRunner(): Runner = Runner(project.projectDir, port.get(), logger)

  fun stop() {
    logger.info("Stopping klip server on ${port.get()}")
    handle?.stop()
  }

  class Runner(
    private val projectDir: File,
    private val port: Int,
    private val logger: Logger,
  ) : AsyncRunner<KlipServer, KlipServer> {
    override fun execute(services: ServiceRegistry): KlipServer = KlipServer(projectDir, port).apply {
      logger.info("Starting blocking klip server on $port")
      start(true)
    }

    override fun start(): KlipServer = KlipServer(projectDir, port).apply {
      logger.info("Starting async klip server on $port")
      start(false)
    }

    abstract class Handle
    @Inject constructor(override val runner: Runner) : AsyncRunner.Handle<KlipServer>() {
      override fun abort(process: KlipServer): Boolean {
        process.close()
        return true
      }
    }
  }
}
