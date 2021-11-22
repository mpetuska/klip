import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("plugin.common")
  id("com.github.gmazzo.buildconfig")
}

buildConfig {
  useKotlinOutput {
    internalVisibility = true
    topLevelConstants = true
  }
  packageName("dev.petuska.klip.plugin.config")
  buildConfigField("String", "GROUP", "\"${rootProject.group}\"")
  buildConfigField("String", "NAME", "\"${rootProject.name}\"")
  buildConfigField("String", "VERSION", "\"${rootProject.version}\"")
  buildConfigField(
    "String",
    "GRADLE_PLUGIN_ARTEFACT_ID",
    "\"${project(":plugin:klip-gradle-plugin").name}\""
  )
  buildConfigField(
    "String",
    "KOTLIN_PLUGIN_ARTEFACT_ID",
    "\"${project(":plugin:klip-kotlin-plugin").name}\""
  )
  buildConfigField(
    "String",
    "KOTLIN_NATIVE_PLUGIN_ARTEFACT_ID",
    "\"\$KOTLIN_PLUGIN_ARTEFACT_ID-native\""
  )
  buildConfigField(
    "String",
    "KOTLIN_PLUGIN_ID",
    "\"\$GROUP.\$KOTLIN_PLUGIN_ARTEFACT_ID\""
  )
}

tasks { withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" } }
