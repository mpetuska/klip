plugins {
  kotlin("jvm")
  kotlin("kapt")
  id("plugin.publishing")
  id("com.gradle.plugin-publish")
  `java-gradle-plugin`
}

description = """
    Gradle plugin to manage KLIP snapshots, processors and dependencies
""".trimIndent()

gradlePlugin {
  plugins {
    create(name) {
      id = "$group.klip"
      displayName = "Kotlin multiplatform snapshot testing"
      description = project.description
      implementationClass = "$id.plugin.KlipPlugin"
    }
  }
}

pluginBundle {
  website = "https://github.com/mpetuska/klip"
  vcsUrl = "https://github.com/mpetuska/klip.git"
  tags = listOf("multiplatform", "test", "kotlin", "snapshots")
}

dependencies {
  implementation(project(":plugin:klip-common-plugin"))
  compileOnly(kotlin("gradle-plugin-api"))

  testImplementation(kotlin("gradle-plugin-api"))
  testImplementation(kotlin("test-junit5"))
}
