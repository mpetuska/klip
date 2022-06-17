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
  fun Collection<Named>.onlyPublishIf(enabled: Spec<in Task>) {
    val publications: Collection<String> = map { it.name }
    afterEvaluate {
      publishing {
        publications {
          matching { it.name in publications }.all {
            val targetPublication = this@all
            tasks.withType<AbstractPublishToMaven>()
              .matching { it.publication == targetPublication }
              .configureEach {
                onlyIf(enabled)
              }
            tasks.withType<GenerateModuleMetadata>()
              .matching { it.publication.get() == targetPublication }
              .configureEach {
                onlyIf(enabled)
              }
          }
        }
      }
    }
  }

  val nativeTargets = targets.withType<KotlinNativeTarget>()
  val windowsHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.MINGW }
  val linuxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.LINUX }
  val osxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.OSX }
  val mainHostTargets = targets.filter { it !in nativeTargets }
  val androidTargets = targets.withType<KotlinAndroidTarget>()
  logger.info("Linux host targets: $linuxHostTargets")
  logger.info("OSX host targets: $osxHostTargets")
  logger.info("Windows host targets: $windowsHostTargets")
  logger.info("Main host targets: $mainHostTargets")
  logger.info("Android targets: $androidTargets")

  androidTargets.forEach {
    if (!CI || SANDBOX || isMainHost) {
      it.publishLibraryVariants("release", "debug")
    }
  }
  linuxHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsLinux }
  osxHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsMac }
  windowsHostTargets.onlyPublishIf { !CI || SANDBOX || HostManager.hostIsMingw }
  (mainHostTargets + Named { "kotlinMultiplatform" }).onlyPublishIf {
    !CI || SANDBOX || isMainHost
  }
}
