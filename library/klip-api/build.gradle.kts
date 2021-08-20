plugins {
  id("plugin.library")
  id("plugin.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Runtime dependency."

kotlin {
  sourceSets {
    named("commonMain") {
      dependencies {
        api(kotlin("test"))
      }
    }
    named("jvmMain") {
      dependencies {
      }
    }
    named("jsMain") {
      dependencies {
      }
    }

    named("commonTest") {
      dependencies {
        api(kotlin("test-annotations-common"))
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
    named("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }

    all {
      languageSettings {
        optIn("kotlin.contracts.ExperimentalContracts")
      }
    }
  }
}
