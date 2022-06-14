plugins {
  id("convention.library-android")
  id("convention.mpp")
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
        kotlin.srcDir("src/sharedMain/kotlin")
        kotlin.srcDir("src/blockingMain/kotlin")
        resources.srcDir("src/sharedMain/resources")
        resources.srcDir("src/blockingMain/resources")
        dependsOn(getByName("sharedMain"))
      }
      named("androidTest") {
        kotlin.srcDir("src/blockingTest/kotlin")
        kotlin.srcDir("src/sharedTest/kotlin")
        resources.srcDir("src/blockingTest/resources")
        resources.srcDir("src/sharedTest/resources")
        dependsOn(getByName("sharedTest"))
        dependencies {
          implementation(kotlin("test-junit5"))
        }
      }
    }
  }
}
