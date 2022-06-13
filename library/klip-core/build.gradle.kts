plugins {
  kotlin("plugin.serialization")
  id("convention.library-mpp")
//  id("plugin.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Runtime dependency."

kotlin {
  sourceSets {
    named("commonMain") {
      dependencies {
        api(project(":library:klip-api"))
        implementation("io.ktor:ktor-client-core:_")
        implementation("io.ktor:ktor-serialization-kotlinx-cbor:_")
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
