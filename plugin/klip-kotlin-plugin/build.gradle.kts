plugins {
  kotlin("kapt")
  id("plugin.publishing-jvm")
}

description = "Kotlin compiler plugin to manage KLIP snapshots for jvm & js"

java {
  withSourcesJar()
}

dependencies {
  compileOnly(kotlin("compiler-embeddable"))
  compileOnly("com.google.auto.service:auto-service-annotations:_")
  kapt("com.google.auto.service:auto-service:_")

  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("compiler-embeddable"))
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:_")
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

kotlin {
  sourceSets {
    main {
      kotlin.source(project(":plugin:klip-common-plugin").sourceSets["main"].allSource)
    }
  }
}

tasks {
  named("processResources", Copy::class) {
    val commonProcessResources = project(":plugin:klip-common-plugin").tasks.getByName("processResources", Copy::class)
    dependsOn(commonProcessResources)
    from(commonProcessResources.destinationDir)
  }
}
