import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  id("dev.petuska.klip")
  kotlin("jvm")
  idea
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip-api")
        implementation(kotlin("test-junit5"))
      }
    }
  }
}

tasks {
  val mainPluginSourceSets = { rootProject.extensions.getByType(KotlinMultiplatformExtension::class).sourceSets }
  fun Sync.registerSources(sourceSet: KotlinSourceSet, root: File) {
    destinationDir = root
    from(sourceSet.kotlin.sourceDirectories) {
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
    registerSources(mainPluginSourceSets()["commonMain"], projectDir.resolve("src/main"))
  }
  named("compileKotlin") { dependsOn(syncSourceMain) }
  val syncSourceTest by registering(Sync::class) {
    registerSources(mainPluginSourceSets()["commonTest"], projectDir.resolve("src/test"))
  }
  named("compileTestKotlin") { dependsOn(syncSourceTest) }
}
