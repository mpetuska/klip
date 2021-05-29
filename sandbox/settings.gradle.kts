pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
  }
}

plugins {
  id("de.fayard.refreshVersions") version "0.10.0"
  id("com.gradle.enterprise") version "3.6.1"
}

rootProject.name = "sandbox"
includeBuild("../")
includeBuild("../gradle-plugin")
include(":jvm", "js")
