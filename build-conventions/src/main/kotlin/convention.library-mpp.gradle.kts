plugins {
  id("convention.mpp")
  id("convention.library-android")
}

kotlin {
  explicitApi()
  android {
    publishLibraryVariants("release", "debug")
//    androidTargets.forEach {
//      if (!CI || SANDBOX || isMainHost) {
//        it.publishLibraryVariants("release", "debug")
//      }
//    }
  }
  sourceSets {
    named("androidTest") {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
  }
}
