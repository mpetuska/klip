plugins {
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