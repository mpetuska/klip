import util.withName

plugins {
  id("convention.library-mpp")
  id("convention.control")
  id("dev.petuska.klip") version "0.4.1"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

repositories {
  mavenLocal()
}

klip {
  debug.set(true)
}

kotlin {
  sourceSets {
    commonTest {
      dependencies {
        implementation("dev.petuska:klip")
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    withName("androidTest") {
      dependencies {
        implementation("io.kotest:kotest-framework-engine:_")
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    withName("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-framework-engine:_")
        runtimeOnly("io.kotest:kotest-runner-junit5:_")
      }
    }
    withName("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
    all {
      languageSettings {
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }
}
