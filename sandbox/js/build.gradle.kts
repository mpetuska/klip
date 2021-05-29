plugins {
  kotlin("js")
  id("dev.petuska.klip")
}

repositories {
  jcenter()
}

kotlin {
  js {
    useCommonJs()
    nodejs()
  }
  sourceSets {
    named("test") {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
