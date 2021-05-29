plugins {
  kotlin("multiplatform")
}

description = "Kotlin multiplatform snapshot (klip) testing. Runtime dependency."

kotlin {
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  sourceSets {
    named("commonMain") {
      dependencies {
        api(kotlin("test"))
        api(kotlin("test-annotations-common"))
      }
    }
    named("jvmMain") {
      dependencies {
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    named("jsMain") {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:_")
      }
    }
  }
}
