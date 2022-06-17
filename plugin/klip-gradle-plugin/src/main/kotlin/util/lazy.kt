package dev.petuska.klip.plugin.util

import org.gradle.api.Project
import org.gradle.api.provider.Property

fun <T> Property<T>.sysOrGradleOrEnvConvention(
  project: Project,
  propName: String,
  envName: String,
  default: (() -> T)? = null,
  converter: (String) -> T,
) {
  val p = project.providers.systemProperty(propName)
    .orElse(project.provider { project.extensions.extraProperties.properties[propName]?.toString() })
    .orElse(project.providers.gradleProperty(propName))
    .orElse(project.providers.environmentVariable(envName))
  convention(
    p.map(converter).orElse(project.provider { default?.invoke() })
  )
}
