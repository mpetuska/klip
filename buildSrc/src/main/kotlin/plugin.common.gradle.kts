import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import util.by

plugins {
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

repositories {
  mavenCentral()
  google()
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

ktlint {
  version by versionFor("version.ktlint")
  additionalEditorconfigFile.set(rootDir.resolve(".editorconfig"))
}

tasks {
  project.properties["org.gradle.project.targetCompatibility"]?.toString()?.let {
    withType<KotlinCompile> {
      kotlinOptions {
        jvmTarget = it
      }
    }
  }
  afterEvaluate {
    if (tasks.findByName("compile") == null) {
      register("compile") {
        dependsOn(withType(AbstractKotlinCompile::class))
        group = "build"
      }
    }
    if (tasks.findByName("allTests") == null) {
      register("allTests") {
        dependsOn(withType(KotlinTest::class))
        group = "verification"
      }
    }
  }
}
