plugins {
  kotlin("multiplatform") apply false
  id("org.jetbrains.dokka")
  id("com.github.jakemarsden.git-hooks")
  id("org.jlleitschuh.gradle.ktlint")
  id("io.github.gradle-nexus.publish-plugin")
  `maven-publish`
  signing
  idea
}

gitHooks {
  setHooks(
    mapOf(
      "post-checkout" to "ktlintApplyToIdea",
      "pre-commit" to "ktlintFormat",
      "pre-push" to "check"
    )
  )
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

allprojects {
  repositories {
    mavenCentral()
    google()
    jcenter()
  }
}

subprojects {
  apply(plugin = "idea")
  apply(plugin = "signing")
  apply(plugin = "maven-publish")
  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  idea {
    module {
      isDownloadJavadoc = true
      isDownloadSources = true
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

  tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_11}"
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
}

infix fun <T> Property<T>.by(value: T) {
  set(value)
}

object Git {
  val headCommitHash by lazy {
    val child = Runtime.getRuntime().exec("git rev-parse --verify HEAD")
    child.waitFor()
    child.inputStream.readAllBytes().toString(java.nio.charset.Charset.defaultCharset()).trim()
  }
}
