plugins {
  id("com.github.jakemarsden.git-hooks")
  id("plugin.library-mpp")
  id("plugin.publishing-nexus")
  id("plugin.publishing-mpp")
}

gitHooks { setHooks(mapOf("pre-commit" to "spotlessApply", "pre-push" to "spotlessCheck")) }

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies { subprojects.filter { it.path.startsWith(":library:") }.forEach { api(it) } }
    }
  }
}
