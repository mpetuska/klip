plugins {
  id("dev.petuska.klip")
  kotlin("jvm")
  idea
}

idea {
  module {
    generatedSourceDirs.add(buildDir.resolve("generated/ksp/main"))
    generatedSourceDirs.add(buildDir.resolve("generated/ksp/test"))
  }
}

kotlin {
  sourceSets {
    named("test") {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
  }
}
