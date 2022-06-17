import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
import util.buildHost

plugins {
  kotlin("multiplatform")
  id("convention.publishing")
}

kotlin {
  fun NamedDomainObjectCollection<out Named>.onlyPublishIf(enabled: Spec<in Task>) {
    publishing {
      publications {
        matching { it.name in this@onlyPublishIf.names }.all {
          val targetPublication = this@all
          tasks.withType<AbstractPublishToMaven>()
            .matching { it.publication == targetPublication }
            .all { onlyIf(enabled) }
          tasks.withType<GenerateModuleMetadata>()
            .matching { it.publication.orNull == targetPublication }
            .all { onlyIf(enabled) }
        }
      }
    }
  }

  val nativeTargets = targets.withType<KotlinNativeTarget>()
  val windowsHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.MINGW }
  val linuxHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.LINUX }
  val osxHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.OSX }
  val mainHostTargets = targets.matching { it !in nativeTargets }
  val androidTargets = targets.withType<KotlinAndroidTarget>()
  logger.info("Linux host targets: $linuxHostTargets")
  logger.info("OSX host targets: $osxHostTargets")
  logger.info("Windows host targets: $windowsHostTargets")
  logger.info("Main host targets: $mainHostTargets")
  logger.info("Android targets: $androidTargets")

  androidTargets.all {
    if (!CI || SANDBOX || isMainHost) {
      publishLibraryVariants("release", "debug")
    }
  }
  linuxHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsLinux }
  osxHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsMac }
  windowsHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsMingw }
  mainHostTargets.onlyPublishIf { !CI || SANDBOX || isMainHost }
  val mpp = objects.domainObjectContainer(Named::class.java)
  mpp.add(Named { "kotlinMultiplatform" })
  mpp.onlyPublishIf { !CI || SANDBOX || isMainHost }
}
