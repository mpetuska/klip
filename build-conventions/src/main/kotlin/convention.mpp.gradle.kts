import util.sharedTestDependencies
import util.targetGroup

plugins {
  id("convention.common")
  kotlin("multiplatform")
}

kotlin {
  targetGroup(
    "stub",
    "commonMain",
    "commonTest",
    mingwX86(),
    linuxArm32Hfp(),
    linuxArm64(),
    linuxMips32(),
    linuxMipsel32(),
    androidNativeArm32(),
    androidNativeArm64(),
    androidNativeX64(),
    androidNativeX86(),
  )
  val (sharedMain, sharedTest) = targetGroup(
    "shared",
    "commonMain",
    "commonTest",
    js(IR) {
      browser()
      nodejs()
    }
  )
  val (blockingMain, blockingTest) = targetGroup(
    "blocking",
    sharedMain,
    sharedTest,
    jvm(),
  )
  val (nativeMain, nativeTest) = targetGroup(
    "native",
    blockingMain,
    blockingTest,
    mingwX64(),
    linuxX64(),
  )
  targetGroup(
    "apple",
    nativeMain,
    nativeTest,
    iosArm32(),
    iosArm64(),
    iosSimulatorArm64(),
    iosX64(),
    macosArm64(),
    macosX64(),
    tvosArm64(),
    tvosSimulatorArm64(),
    tvosX64(),
    watchosArm32(),
    watchosArm64(),
    watchosSimulatorArm64(),
    watchosX86(),
    watchosX64(),
  )

  sourceSets {
    named("commonTest") {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    sharedTestDependencies {
//      implementation("io.kotest:kotest-assertions-core:_")
//      implementation("io.kotest:kotest-property:_")
      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
    }
    named("jsTest") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
    configureEach {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
      }
    }
  }
}
