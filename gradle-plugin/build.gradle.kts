import java.nio.charset.Charset

plugins {
  kotlin("jvm") version "1.4.31"
  id("org.jetbrains.dokka")
  id("org.jlleitschuh.gradle.ktlint")
  id("com.gradle.plugin-publish")
  id("io.github.gradle-nexus.publish-plugin")
  `java-gradle-plugin`
  `maven-publish`
  signing
  idea
}

description = """
    Gradle plugin to manage KLIP snapshots, processors and dependencies
""".trimIndent()

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

gradlePlugin {
  plugins {
    create(name) {
      id = "$group.klip"
      displayName = "Kotlin multiplatform snapshot testing"
      description = project.description
      implementationClass = "$id.KlipPlugin"
    }
  }
}

pluginBundle {
  website = "https://github.com/mpetuska/klip"
  vcsUrl = "https://github.com/mpetuska/klip.git"
  tags = listOf("multiplatform", "test", "kotlin", "snapshots")
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  if (signingKey != null) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
  }
}

java {
  withSourcesJar()
  withJavadocJar()
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation(kotlin("gradle-plugin", "_"))
  implementation("dev.petuska:klip-core")
  implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:_")

  testImplementation(kotlin("test-junit"))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "${JavaVersion.VERSION_11}"
    }
  }
  processResources {
    doLast {
      destinationDir.resolve("version").writeText("${project.version}")
      destinationDir.resolve("group").writeText("${project.group}")
    }
  }
}

publishing {
  publications {
    withType<MavenPublication> {
      pom {
        name by project.name
        url by "https://github.com/mpetuska/klip"
        description by project.description

        licenses {
          license {
            name by "The Apache License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }

        developers {
          developer {
            id to "mpetuska"
            name to "Martynas Petuška"
            email to "martynas@petuska.dev"
          }
        }

        scm {
          connection by "scm:git:git@github.com:mpetuska/klip.git"
          url by "https://github.com/mpetuska/klip"
          tag by Git.headCommitHash
        }
      }
    }
    repositories {
      maven("https://maven.pkg.github.com/mpetuska/klip") {
        name = "GitHub"
        credentials {
          username = System.getenv("GH_USERNAME")
          password = System.getenv("GH_PASSWORD")
        }
      }
    }
  }
}

afterEvaluate {
  tasks {
    withType<Jar> {
      manifest {
        attributes += sortedMapOf(
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "${org.gradle.util.GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        )
      }
    }
  }
}

infix fun <T> Property<T>.by(value: T) {
  set(value)
}

object Git {
  val headCommitHash by lazy {
    val child = Runtime.getRuntime().exec("git rev-parse --verify HEAD")
    child.waitFor()
    child.inputStream.readAllBytes().toString(Charset.defaultCharset()).trim()
  }
}
