import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
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

configurations.all {
  resolutionStrategy {
    force("org.jetbrains.kotlin:kotlin-stdlib:$embeddedKotlinVersion")
  }
}

kotlin {
  sourceSets {
    main {
      dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly(kotlin("gradle-plugin-api"))
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

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      languageVersion = "1.4"
    }
  }
}
