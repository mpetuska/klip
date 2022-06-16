package dev.petuska.klip.plugin.task

import dev.petuska.klip.plugin.server.KlipServerService
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class KlipServerStartTask : DefaultTask() {
  @get:Internal
  abstract val service: Property<KlipServerService>

  init {
    description = "Starts a server to manage klips state"
  }

  @TaskAction
  fun start() {
    service.get().start()
  }
}
