plugins {
  if (System.getenv("CI") == null) {
    id("convention.git-hooks")
  }
  id("convention.library-mpp")
  id("convention.publishing-nexus")
//  id("plugin.publishing-mpp")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":library:klip-assertions"))
        api(project(":library:klip-api"))
      }
    }

    named("jsMain") {
      dependencies {
        implementation("io.ktor:ktor-client-js:_")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation("io.ktor:ktor-client-java:_")
      }
    }
    named("linuxX64Main") {
      dependencies {
        implementation("io.ktor:ktor-client-curl:_")
      }
    }
    named("mingwX64Main") {
      dependencies {
        implementation("io.ktor:ktor-client-curl:_")
      }
    }
    named("appleMain") {
      dependencies {
        implementation("io.ktor:ktor-client-darwin:_")
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
