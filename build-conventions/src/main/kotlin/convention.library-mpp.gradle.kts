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
    afterEvaluate {
      named("androidMain") {
        dependsOn(getByName("blockingMain"))
      }
      named("androidTest") {
        dependsOn(getByName("blockingTest"))
        dependencies {
          implementation(kotlin("test-junit5"))
        }
      }
    }
  }
}
