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
}