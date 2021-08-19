import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  id("plugin.common")
  kotlin("multiplatform")
}

kotlin {
  jvm()
  js {
    useCommonJs()
    nodejs()
  }
  linuxX64()
  macosX64()
  mingwX64()
}

kotlin {
  sourceSets {
    val commonMain by getting
    val nativeMain by creating {
      dependsOn(commonMain)
    }
    val commonTest by getting
    val nativeTest by creating {
      dependsOn(commonTest)
    }
    targets.withType<KotlinNativeTarget> {
      compilations["main"].defaultSourceSet.dependsOn(nativeMain)
      compilations["test"].defaultSourceSet.dependsOn(nativeTest)
    }
  }
}