package dev.petuska.klip.plugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class KlipPluginTest {
  @Test
  fun `plugin registers task`() {
    val project = ProjectBuilder.builder().build()
    project.plugins.apply("dev.petuska.klip")

    assertNotNull(project.extensions.findByName("klip"))
  }
}
