pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
  }
}

plugins {
  id("de.fayard.refreshVersions") version "0.21.0"
  id("com.gradle.enterprise") version "3.6.4"
}

refreshVersions { extraArtifactVersionKeyRules(file("versions.rules")) }

rootProject.name = "sandbox"
includeBuild("../")
include(":jvm", "js")
