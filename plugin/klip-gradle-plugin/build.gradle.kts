plugins {
  id("com.gradle.plugin-publish")
  `java-gradle-plugin`
  id("convention.publishing-jvm")
  id("convention.build-konfig")
}

description = """Gradle plugin to manage KLIP snapshots, processors and dependencies"""

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

kotlin {
  sourceSets {
    main {
      dependencies {
        compileOnly(kotlin("gradle-plugin-api"))
        api("dev.petuska:container-tasks-gradle-plugin:_")
        implementation("io.ktor:ktor-server-cio:_")
        implementation("io.ktor:ktor-server-cors:_")
        implementation("io.ktor:ktor-server-content-negotiation:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
      }
    }

    test {
      dependencies {
        implementation(kotlin("gradle-plugin-api"))
        implementation(kotlin("test-junit5"))
      }
    }
  }
}

java {
  withSourcesJar()
}
