pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}

rootProject.name = "klip"
include(":sandbox")

val modules = arrayOf(":plugin", ":core", ":processor")
modules.forEach {
    include(it)
    project(it).apply {
        name = "${rootProject.name}-${name}"
    }
}