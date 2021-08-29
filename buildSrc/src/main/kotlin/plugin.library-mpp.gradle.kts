import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
import util.SANDBOX
import util.buildHost

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
  macosArm64()
  linuxX64()
  mingwX64()

//  To be implemented
//  iosArm32()
//  iosArm64()
//  iosX64()
//  iosSimulatorArm64()
//  watchosX86()
//  watchosX64()
//  watchosArm64()
//  watchosArm32()
//  watchosSimulatorArm64()
//  tvosArm64()
//  tvosX64()
//  tvosSimulatorArm64()

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
    val unixMain by creating {
      dependsOn(nativeMain)
    }
    val mingwMain by creating {
      dependsOn(nativeMain)
    }

    val fallbackTest by creating {
      dependsOn(commonTest)
    }
    val nativeTest by creating {
      dependsOn(commonTest)
    }
    val unixTest by creating {
      dependsOn(nativeTest)
    }
    val mingwTest by creating {
      dependsOn(nativeTest)
    }

    targets.withType<KotlinNativeTarget> {
      val main = compilations["main"].defaultSourceSet
      val test = compilations["test"].defaultSourceSet
      when {
        this in fallbackTargets -> {
          main.dependsOn(fallbackMain)
          test.dependsOn(fallbackTest)
        }
        konanTarget.family == Family.MINGW -> {
          main.dependsOn(mingwMain)
          test.dependsOn(mingwTest)
        }
        else -> {
          main.dependsOn(unixMain)
          test.dependsOn(unixTest)
        }
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
      SANDBOX || konanTarget.buildHost == HostManager.host.family
    }
  }
  withType<AbstractKotlinNativeCompile<*, *>> {
    onlyIf {
      SANDBOX || compilation.konanTarget.buildHost == HostManager.host.family
    }
  }
}
