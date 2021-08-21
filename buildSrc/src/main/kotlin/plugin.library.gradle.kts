import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.HostManager
import util.hostFamily

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("plugin.common")
}

kotlin {
  explicitApi()
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  macosX64()
  linuxX64()
//  TODO https://github.com/mpetuska/klip/pull/4/checks?check_run_id=3386785439#step:8:255
//  mingwX64()

//  To be implemented
//  iosArm32()
//  iosArm64()
//  iosX64()
//  watchosX86()
//  watchosX64()
//  watchosArm64()
//  watchosArm32()
//  tvosArm64()
//  tvosX64()

  // Fallback Targets
  val fallbackTargets = setOf(
    androidNativeArm32(),
    androidNativeArm64(),
    mingwX86(),
    linuxArm32Hfp(),
    linuxMips32(),
    linuxMipsel32(),
    linuxArm64(),
  )

  sourceSets {
    val commonMain by getting
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val fallbackMain by creating {
      dependsOn(commonMain)
    }
    val nativeMain by creating {
      dependsOn(commonMain)
    }
    val fallbackTest by creating {
      dependsOn(commonTest)
    }
    val nativeTest by creating {
      dependsOn(commonTest)
    }
    targets.withType<KotlinNativeTarget> {
      if (this in fallbackTargets) {
        compilations["main"].defaultSourceSet.dependsOn(fallbackMain)
        compilations["test"].defaultSourceSet.dependsOn(fallbackTest)
      } else {
        compilations["main"].defaultSourceSet.dependsOn(nativeMain)
        compilations["test"].defaultSourceSet.dependsOn(nativeTest)
      }
    }
  }
}

tasks {
  project.properties["org.gradle.project.targetCompatibility"]?.toString()?.let {
    withType<KotlinCompile> {
      kotlinOptions {
        jvmTarget = it
      }
    }
  }
  withType<CInteropProcess> {
    onlyIf {
      konanTarget.hostFamily == HostManager.host.family
    }
  }
  withType<AbstractKotlinNativeCompile<*, *>> {
    onlyIf {
      compilation.konanTarget.hostFamily == HostManager.host.family
    }
  }
}
