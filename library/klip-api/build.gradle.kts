plugins {
  id("plugin.library-mpp")
  id("plugin.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. API dependency."

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":library:klip-core"))
      }
    }
    all {
      languageSettings {
        optIn("kotlin.contracts.ExperimentalContracts")
      }
    }
  }
}
