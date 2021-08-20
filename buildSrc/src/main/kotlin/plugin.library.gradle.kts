import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.HostManager

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
  mingwX64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val nativeMain by creating {
      dependsOn(commonMain)
    }
    val nativeTest by creating {
      dependsOn(commonTest)
    }
    targets.withType<KotlinNativeTarget> {
      compilations["main"].defaultSourceSet.dependsOn(nativeMain)
      compilations["test"].defaultSourceSet.dependsOn(nativeTest)
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
      konanTarget == HostManager.host
    }
  }
  withType<AbstractKotlinNativeCompile<*, *>> {
    onlyIf {
      compilation.konanTarget == HostManager.host
    }
  }
}
