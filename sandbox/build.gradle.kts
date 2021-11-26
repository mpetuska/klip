import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

plugins {
  id("dev.petuska.klip")
  id("io.kotest.multiplatform")
  kotlin("multiplatform")
  id("com.android.library")
  idea
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

android {
  compileSdkVersion(31)
  defaultConfig {
    minSdkVersion(1)
    targetSdkVersion(31)
  }
}

allprojects {
  apply(plugin = "idea")

  idea {
    module {
      isDownloadJavadoc = true
      isDownloadSources = true
    }
  }

  repositories {
    mavenCentral()
    google()
  }
  tasks {
    afterEvaluate {
      if (tasks.findByName("compile") == null) {
        register("compile") {
          dependsOn(withType(AbstractKotlinCompile::class))
          group = "build"
        }
      }
      val testTasks = withType<Test> {
        useJUnitPlatform()
      }
      if (tasks.findByName("allTests") == null) {
        register("allTests") {
          dependsOn(testTasks)
          group = "verification"
        }
      }
    }
  }
}

kotlin {
  android()
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  linuxX64()
  macosX64()
  macosArm64()
  mingwX64()
  iosArm32()
  iosArm64()
  iosX64()
  iosSimulatorArm64()
  watchosX86()
  watchosX64()
  watchosArm64()
  watchosArm32()
  watchosSimulatorArm64()
  tvosArm64()
  tvosX64()
  tvosSimulatorArm64()

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
        implementation("dev.petuska:klip")
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    named("androidTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("io.kotest:kotest-framework-engine:_")
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    named("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
