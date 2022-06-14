import util.sharedMainDependencies

plugins {
  id("convention.library-mpp")
//  id("plugin.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Common test runner."

kotlin {
  sourceSets {
    sharedMainDependencies {
      api("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
    }
    named("stubMain") {
      dependencies {
        implementation(project(":library:klip-core"))
      }
    }
    all {
      languageSettings {
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }
}
