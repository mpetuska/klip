import util.withName

plugins {
  if (System.getenv("CI") == null) {
    id("convention.git-hooks")
  }
  id("convention.library-mpp")
  id("convention.publishing-nexus")
  id("convention.publishing-mpp")
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
        api(project(":library:klip-api"))
        api(project(":library:klip-runner"))
        api(project(":library:klip-assertions"))
      }
    }

    withName("androidMain") {
      dependencies {
        implementation("io.ktor:ktor-client-android:_")
      }
    }
    withName("jsMain") {
      dependencies {
        implementation("io.ktor:ktor-client-js:_")
      }
    }
    withName("jvmMain") {
      dependencies {
        implementation("io.ktor:ktor-client-java:_")
      }
    }
    withName("linuxX64Main") {
      dependencies {
        implementation("io.ktor:ktor-client-curl:_")
      }
    }
    withName("mingwX64Main") {
      dependencies {
        implementation("io.ktor:ktor-client-curl:_")
      }
    }
    withName("appleMain") {
      dependencies {
        implementation("io.ktor:ktor-client-darwin:_")
      }
    }
  }
}
