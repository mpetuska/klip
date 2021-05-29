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

rootProject.name = "klip-gradle-plugin"
includeBuild("../")
