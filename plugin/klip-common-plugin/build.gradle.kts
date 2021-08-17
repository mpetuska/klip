plugins {
  kotlin("jvm")
  id("plugin.publishing")
}

tasks {
  processResources {
    doLast {
      destinationDir.resolve("klip.version").writeText("${project.version}")
      destinationDir.resolve("klip.group").writeText("${project.group}")
      destinationDir.resolve("klip.gradlePluginArtifactId")
        .writeText(project(":plugin:klip-gradle-plugin").name)
      destinationDir.resolve("klip.kotlinPluginArtifactId")
        .writeText(project(":plugin:klip-kotlin-plugin").name)
      destinationDir.resolve("klip.kotlinNativePluginArtifactId")
        .writeText(project(":plugin:klip-kotlin-plugin:klip-kotlin-plugin-native").name)
    }
  }
}
