plugins {
  id("plugin.publishing")
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  api(project(":plugin:klip-common-plugin"))
  compileOnly(kotlin("compiler-embeddable"))
  compileOnly("com.google.auto.service:auto-service-annotations:_")
  kapt("com.google.auto.service:auto-service:_")

  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("compiler-embeddable"))
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:_")
}
