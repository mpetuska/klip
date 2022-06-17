pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
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

includeBuild("../build-conventions")
includeBuild("..")

rootProject.name = "klip-sandbox"
include(
  ":jvm",
  ":js",
)
