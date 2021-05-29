pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}

rootProject.name = "sandbox"
includeBuild("../")
includeBuild("../gradle-plugin")
include(":jvm", "js")