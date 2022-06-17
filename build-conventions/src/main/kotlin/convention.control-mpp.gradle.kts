import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
import util.buildHost

plugins {
  kotlin("multiplatform")
  id("convention.common")
}

kotlin {
  fun Collection<KotlinTarget>.onlyBuildIf(enabled: Spec<in Task>) {
    forEach {
      it.compilations.all {
        compileKotlinTask.onlyIf(enabled)
      }
    }
  }

  val nativeTargets = targets.withType<KotlinNativeTarget>()
  val windowsHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.MINGW }
  val linuxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.LINUX }
  val osxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.OSX }
  val mainHostTargets = targets.filter { it !in nativeTargets }
  linuxHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsLinux }
  osxHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsMac }
  windowsHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsMingw }
  mainHostTargets.onlyBuildIf {
    !CI || SANDBOX || isMainHost
  }
}
