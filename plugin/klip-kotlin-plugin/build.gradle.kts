plugins {
  kotlin("kapt")
  id("plugin.publishing-jvm")
  id("plugin.build-config-jvm")
}

description = "Kotlin compiler plugin to manage KLIP snapshots for jvm & js"

java { withSourcesJar() }

dependencies {
  compileOnly(kotlin("compiler-embeddable"))
  compileOnly("com.google.auto.service:auto-service-annotations:_")
  kapt("com.google.auto.service:auto-service:_")

  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("compiler-embeddable"))
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:_")
  testImplementation(project(":library:klip-core"))
}

publishing {
  publications {
    create(name, MavenPublication::class.java) {
      artifactId = name
      artifact(tasks.jar)
      artifact(tasks.kotlinSourcesJar)
    }
  }
}
