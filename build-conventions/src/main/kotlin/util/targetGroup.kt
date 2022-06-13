package util

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

fun <T : KotlinTarget> KotlinMultiplatformExtension.targetGroup(
  name: String,
  mainSourceSetTarget: String,
  testSourceSetTarget: String,
  vararg targets: T
): Pair<String, String> {
  val mainName = "${name}Main"
  val testName = "${name}Test"
  sourceSets {
    val main = create(mainName) { dependsOn(getByName(mainSourceSetTarget)) }
    val test = create(testName) { dependsOn(getByName(testSourceSetTarget)) }
    targets.forEach { target ->
      target.compilations["main"].defaultSourceSet { dependsOn(main) }
      target.compilations["test"].defaultSourceSet { dependsOn(test) }
    }
  }
  return mainName to testName
}
