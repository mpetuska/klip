plugins {
  id("convention.common")
  id("com.github.gmazzo.buildconfig")
}

buildConfig {
  useKotlinOutput {
    internalVisibility = true
    topLevelConstants = true
  }
  packageName("dev.petuska.klip.plugin.config")
  buildConfigField("String", "GROUP", "\"${rootProject.group}\"")
  buildConfigField("String", "NAME", "\"${rootProject.name}\"")
  buildConfigField("String", "VERSION", "\"${rootProject.version}\"")
}
