plugins {
  kotlin("kapt")
  id("com.gradle.plugin-publish")
  `java-gradle-plugin`
  id("plugin.publishing-jvm")
}

description = """Gradle plugin to manage KLIP snapshots, processors and dependencies"""

java {
  withSourcesJar()
}

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
      kotlin.source(project(":plugin:klip-common-plugin").sourceSets["main"].allSource)
      dependencies {
        compileOnly(kotlin("gradle-plugin-api"))
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

tasks {
  named("processResources", Copy::class) {
    val commonProcessResources = project(":plugin:klip-common-plugin").tasks.getByName("processResources", Copy::class)
    dependsOn(commonProcessResources)
    from(commonProcessResources.destinationDir)
  }
}
