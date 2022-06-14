plugins {
  id("convention.library-mpp")
  id("dev.petuska.klip")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
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
//    named("sharedTest") {
//      dependencies {
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
//      }
//    }
//    named("blockingTest") {
//      dependencies {
//        implementation("dev.petuska:klip")
//      }
//    }
    named("androidTest") {
      dependencies {
        implementation("io.kotest:kotest-framework-engine:_")
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-framework-engine:_")
        runtimeOnly("io.kotest:kotest-runner-junit5:_")
      }
    }
    named("jsTest") {
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

tasks {
  register("detektAll", io.gitlab.arturbosch.detekt.Detekt::class) {
    description = "Run Detekt for all modules"
    config.from(project.detekt.config)
    buildUponDefaultConfig = project.detekt.buildUponDefaultConfig
    setSource(files(projectDir))
  }
}
