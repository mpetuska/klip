import org.jetbrains.kotlin.konan.target.HostManager
import util.CI

plugins {
  id("com.github.jakemarsden.git-hooks")
  id("plugin.library")
  id("plugin.publishing")
  id("plugin.publishing-nexus")
}

println("CI $CI")
println("Is Linux ${HostManager.hostIsLinux}")
println("Is OSX ${HostManager.hostIsMac}")
println("Is MINGW ${HostManager.hostIsMingw}")

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
