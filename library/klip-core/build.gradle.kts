import util.sharedMain

plugins {
  kotlin("plugin.serialization")
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Runtime dependency."

kotlin {
  sourceSets {
    named("commonMain") {
      dependencies {
        api(project(":library:klip-api"))
      }
    }
    sharedMain {
      dependencies {
        implementation("io.ktor:ktor-client-core:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
        implementation("io.ktor:ktor-client-content-negotiation:_")
      }
    }

    configureEach {
      languageSettings {
        optIn("kotlin.contracts.ExperimentalContracts")
      }
    }
  }
}
