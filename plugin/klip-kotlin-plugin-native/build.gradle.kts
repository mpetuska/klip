import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("convention.publishing-jvm")
}

description = "Kotlin compiler plugin to manage KLIP snapshots for native targets"

dependencies {
  compileOnly(kotlin("compiler"))
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

tasks {
  val mainPluginSourceSets = { project(":plugin:klip-kotlin-plugin").sourceSets }
  fun Sync.registerSources(sourceSet: SourceSet, root: File) {
    dependsOn(sourceSet)
    destinationDir = root
    from(sourceSet.allSource) {
      into("kotlin")
      filter {
        // Replace shadowed imports from plugin module
        when (it) {
          "import org.jetbrains.kotlin.com.intellij.mock.MockProject" ->
            "import com.intellij.mock.MockProject"

          else -> it
        }
      }
    }
    from(sourceSet.resources) { into("resources") }
  }

  val syncSourceMain by registering(Sync::class) {
    registerSources(mainPluginSourceSets().main.get(), projectDir.resolve("src/main"))
  }
  named("compileKotlin") { dependsOn(syncSourceMain) }
  named("clean") { doLast { projectDir.resolve("src").delete() } }
  withType<Detekt> { enabled = false }
}
