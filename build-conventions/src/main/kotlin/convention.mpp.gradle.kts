import util.nativeTargetGroup

plugins {
  id("convention.common")
  kotlin("multiplatform")
}

kotlin {
  jvm()
  js(IR) {
    browser()
    nodejs()
  }
  linuxX64()
  mingwX64()
  nativeTargetGroup(
    "apple",
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
//  KotlinPlatformType.jvm()
//  KotlinPlatformType.js(IR) {
//    useCommonJs()
//    enableSCSS(main = true, test = true)
//    browser {
//      testTask {
//        useKarma {
//          when (project.properties["kotlin.js.test.browser"]) {
//            "firefox" -> useFirefox()
//            "firefox-headless" -> useFirefoxHeadless()
//            "firefox-developer" -> useFirefoxDeveloper()
//            "firefox-developer-headless" -> useFirefoxDeveloperHeadless()
//            "chrome" -> useChrome()
//            "chrome-headless" -> useChromeHeadless()
//            "chromium" -> useChromium()
//            "chromium-headless" -> useChromiumHeadless()
//            "safari" -> useSafari()
//            "opera" -> useOpera()
//            else -> usePhantomJS()
//          }
//        }
//      }
//    }
//  }

  sourceSets {
    named("commonTest") {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation("io.kotest:kotest-assertions-core:_")
        implementation("io.kotest:kotest-property:_")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
      }
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
