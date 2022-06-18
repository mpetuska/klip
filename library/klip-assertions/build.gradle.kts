import util.blockingMain

plugins {
  id("convention.library-mpp")
  id("convention.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Assertions dependency."

kotlin {
  sourceSets {
    named("commonMain") {
      dependencies {
        api(project(":library:klip-api"))
        implementation(kotlin("test"))
        implementation(project(":library:klip-core"))
      }
    }
    blockingMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
  }
}
