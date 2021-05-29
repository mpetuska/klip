plugins {
  id("dev.petuska.klip")
  kotlin("multiplatform")
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

allprojects {
  apply(plugin = "idea")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  idea {
    module {
      isDownloadJavadoc = true
      isDownloadSources = true
    }
  }

  repositories {
    mavenCentral()
    google()
    jcenter()
  }
}

kotlin {
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  sourceSets {
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
  }
}
