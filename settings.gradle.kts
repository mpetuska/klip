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

rootProject.name = "klip"

val modules = arrayOf(":core", ":processor")
modules.forEach {
  include(it)
  project(it).apply {
    name = "${rootProject.name}-$name"
  }
}