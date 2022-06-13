pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

plugins {
  id("de.fayard.refreshVersions") version "0.40.2"
  id("com.gradle.enterprise") version "3.10.2"
}

refreshVersions {
  versionsPropertiesFile = rootDir.resolve("gradle/versions.properties")
  extraArtifactVersionKeyRules(rootDir.resolve("gradle/versions.rules"))
}

includeBuild("build-conventions")

rootProject.name = "klip"
include(
  ":library:klip-api",
  ":library:klip-core",
  ":library:klip-assertions",
  ":plugin:klip-gradle-plugin",
  ":plugin:klip-kotlin-plugin",
  ":plugin:klip-kotlin-plugin-native",
)
