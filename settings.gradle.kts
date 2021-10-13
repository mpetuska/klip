pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
  }
}

plugins {
  id("de.fayard.refreshVersions") version "0.23.0"
  id("com.gradle.enterprise") version "3.7"
}

rootProject.name = "klip"

include(
  ":library:klip-core",
  ":library:klip-api",
  ":plugin:klip-gradle-plugin",
  ":plugin:klip-kotlin-plugin",
  ":plugin:klip-kotlin-plugin-native",
)
