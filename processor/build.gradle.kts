plugins {
  kotlin("multiplatform")
}

description = "Kotlin multiplatform snapshot (klip) testing. KSP-based processor."

kotlin {
  jvm()
  sourceSets {
    named("jvmMain") {
      dependencies {
        implementation("com.google.devtools.ksp:symbol-processing-api:_")
        implementation(project(":klip-core"))
      }
    }
  }
}
