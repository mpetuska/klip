plugins {
  id("convention.common")
  id("dev.petuska.klip")
  kotlin("jvm")
}

klip {
  debug.set(true)
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip")
        implementation(kotlin("test-junit5"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
      }
    }
  }
}
