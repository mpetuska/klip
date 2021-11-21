package dev.petuska.klip.plugin.task

import dev.petuska.klip.plugin.klip
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.testing.Test

open class KlipUpdateTask : DefaultTask() {
  init {
    group = "verification"
    description = "Updates klip files during test run"
    project.klip.update = true
    project.tasks.withType(Test::class.java) { dependsOn(it) }
  }
}
