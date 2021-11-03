plugins {
  id("plugin.library-mpp")
  id("plugin.publishing-mpp")
}

description = "Kotlin multiplatform snapshot (klip) testing. Runtime dependency."

kotlin {
  sourceSets {
    val commonMain by getting { dependencies { api(kotlin("test")) } }
    all { languageSettings { optIn("kotlin.contracts.ExperimentalContracts") } }
  }
}
