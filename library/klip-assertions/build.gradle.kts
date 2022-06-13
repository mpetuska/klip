plugins {
  id("convention.library-mpp")
//  id("plugin.publishing-mpp")
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
  }
}
