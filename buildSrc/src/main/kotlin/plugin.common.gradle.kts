import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

repositories {
  mavenCentral()
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

ktlint {
  version to versionFor("version.ktlint")
  additionalEditorconfigFile to rootDir.resolve(".editorconfig")
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = project.properties["org.gradle.project.targetCompatibility"]!!.toString()
    }
  }
  withType<Test> {
    useJUnitPlatform()
  }
}
