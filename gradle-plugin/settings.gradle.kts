pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}

rootProject.name = "klip-gradle-plugin"
includeBuild("../")