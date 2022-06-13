plugins {
  id("io.gitlab.arturbosch.detekt")
  id("convention.local-properties")
  id("convention.detekt")
  idea
}

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  google()
  gradlePluginPortal()
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

tasks {
  withType<Test> { useJUnitPlatform() }
}

afterEvaluate {
  tasks {
    if (findByName("compile") == null) {
      register("compile") {
        dependsOn(withType(AbstractCompile::class))
        group = "build"
      }
    }
    if (findByName("allTests") == null) {
      register("allTests") {
        dependsOn(withType(AbstractTestTask::class))
        group = "verification"
      }
    }
  }
}
