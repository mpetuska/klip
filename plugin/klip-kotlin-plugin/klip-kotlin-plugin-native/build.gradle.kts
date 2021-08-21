plugins {
  id("plugin.publishing")
  kotlin("jvm")
  kotlin("kapt")
}

java {
  withSourcesJar()
}

ktlint {
  disabledRules.addAll("import-ordering")
}

dependencies {
  compileOnly(kotlin("compiler"))
  compileOnly("com.google.auto.service:auto-service-annotations:_")
  kapt("com.google.auto.service:auto-service:_")

  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("compiler-embeddable"))
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:_")
}

tasks {
  val mainPluginSourceSets = { project(":plugin:klip-kotlin-plugin").sourceSets }
  fun Sync.registerSources(sourceSet: SourceSet, root: File) {
    destinationDir = root
    from(sourceSet.allSource) {
      into("kotlin")
      filter {
        // Replace shadowed imports from plugin module
        when (it) {
          "import org.jetbrains.kotlin.com.intellij.mock.MockProject" -> "import com.intellij.mock.MockProject"
          else -> it
        }
      }
    }
    from(sourceSet.resources) {
      into("resources")
    }
  }

  val syncSourceMain by registering(Sync::class) {
    registerSources(mainPluginSourceSets().main.get(), projectDir.resolve("src/main"))
  }
  named("compileKotlin") {
    dependsOn(syncSourceMain)
  }
//  val syncSourceTest by registering(Sync::class) {
//    registerSources(mainPluginSourceSets().test.get(), projectDir.resolve("src/test"))
//  }
//  named("compileTestKotlin") { dependsOn(syncSourceTest) }
}
