import util.sharedMain

plugins {
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Common test runner."

kotlin {
  sourceSets {
    sharedMain {
      dependencies {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
      }
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
