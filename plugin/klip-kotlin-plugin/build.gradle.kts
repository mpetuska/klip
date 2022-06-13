plugins {
  id("convention.publishing-jvm")
  id("convention.build-konfig")
}

description = "Kotlin compiler plugin to manage KLIP snapshots for jvm & js"

dependencies {
  compileOnly(kotlin("compiler-embeddable"))
  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("compiler-embeddable"))
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:_")
  testImplementation(project(":library:klip-api"))
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
