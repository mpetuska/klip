import util.withName

plugins {
  id("convention.mpp")
  id("convention.library-android")
  id("convention.control")
}

kotlin {
  explicitApi()
  android {
    if (!CI || SANDBOX || isMainHost) {
      publishLibraryVariants("release", "debug")
    }
  }
  sourceSets {
    withName("androidMain") {
      kotlin.srcDir("src/sharedMain/kotlin")
      kotlin.srcDir("src/blockingMain/kotlin")
      resources.srcDir("src/sharedMain/resources")
      resources.srcDir("src/blockingMain/resources")
    }
    withName("androidTest") {
      kotlin.srcDir("src/sharedTest/kotlin")
      kotlin.srcDir("src/blockingTest/kotlin")
      resources.srcDir("src/sharedTest/resources")
      resources.srcDir("src/blockingTest/resources")
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
  }
}
