plugins {
  id("convention.common")
  id("dev.petuska.klip")
  kotlin("jvm")
}

repositories {
  mavenLocal()
}

klip {
  debug.set(rootProject.klip.debug)
  update.set(rootProject.klip.update)
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip")
        implementation(kotlin("test-junit5"))
      }
    }
  }
}
