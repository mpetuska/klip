plugins {
  id("com.github.jakemarsden.git-hooks")
  id("plugin.library")
  id("plugin.publishing-mpp")
  id("plugin.publishing-nexus")
}

gitHooks {
  setHooks(
    mapOf(
      "pre-commit" to "ktlintFormat",
      "pre-push" to "ktlintCheck"
    )
  )
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        subprojects.filter { it.path.startsWith(":library:") }.forEach {
          api(it)
        }
      }
    }
  }
}
