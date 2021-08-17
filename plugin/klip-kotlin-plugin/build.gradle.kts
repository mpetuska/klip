plugins {
  id("plugin.publishing")
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  implementation(project(":plugin:klip-common-plugin"))
  compileOnly(kotlin("compiler-embeddable", "_"))
  compileOnly("com.google.auto.service:auto-service-annotations:_")
  kapt("com.google.auto.service:auto-service:_")
}
