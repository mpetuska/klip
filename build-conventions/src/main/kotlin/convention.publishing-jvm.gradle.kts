import org.gradle.api.Task
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("convention.publishing")
}

val mainHostSpec: Spec<in Task> = Spec { !CI || SANDBOX || isMainHost }

tasks {
  withType<KotlinCompile> {
    onlyIf(mainHostSpec)
    inputs.property("project.mainOS", project.property("project.mainOS"))
  }
}

afterEvaluate {
  publishing {
    publications {
      all {
        val targetPublication = this@all
        tasks.withType<AbstractPublishToMaven>()
          .matching { it.publication == targetPublication }
          .configureEach {
            onlyIf(mainHostSpec)
          }
        tasks.withType<GenerateModuleMetadata>()
          .matching { it.publication.get() == targetPublication }
          .configureEach {
            onlyIf(mainHostSpec)
          }
      }
    }
  }
}
