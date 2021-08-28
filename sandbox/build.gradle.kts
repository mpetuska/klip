import de.fayard.refreshVersions.core.versionFor

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

allprojects {
  apply(plugin = "idea")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  idea {
    module {
      isDownloadJavadoc = true
      isDownloadSources = true
    }
  }

  ktlint {
    version to versionFor("version.ktlint")
    additionalEditorconfigFile to rootDir.resolve(".editorconfig")
  }

  repositories {
    mavenCentral()
  }
}

kotlin {
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  linuxX64()
  macosX64()
  macosArm64()
  mingwX64()

  // Fallback Targets
  androidNativeArm32()
  androidNativeArm64()
  mingwX86()
  linuxArm32Hfp()
  linuxMips32()
  linuxMipsel32()
  linuxArm64()

  sourceSets {
    commonTest {
      dependencies {
        implementation("dev.petuska:klip-api")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
    named("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}

allprojects {
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
  }
}
