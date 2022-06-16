plugins {
  id("convention.common")
  id("dev.petuska.klip")
  kotlin("js")
}

repositories {
  mavenLocal()
}

klip {
  debug.set(rootProject.klip.debug)
  update.set(rootProject.klip.update)
}

kotlin {
  js(IR) {
    useCommonJs()
    nodejs()
    browser()
  }
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
        implementation(kotlin("test-js"))
      }
      languageSettings {
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }
}
