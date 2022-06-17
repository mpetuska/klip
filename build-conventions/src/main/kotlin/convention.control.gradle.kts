import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
import util.buildHost

plugins {
  id("convention.common")
}

pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
  extensions.getByType(KotlinMultiplatformExtension::class.java).targets.let(::control)
}
pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
  objects.namedDomainObjectList(KotlinTarget::class.java).apply {
    add(extensions.getByType(KotlinJvmProjectExtension::class.java).target)
  }.let(::control)
}
pluginManager.withPlugin("org.jetbrains.kotlin.js") {
  objects.namedDomainObjectList(KotlinTarget::class.java).apply {
    add(extensions.getByType(KotlinJsProjectExtension::class.java).js())
  }.let(::control)
}

fun control(targets: NamedDomainObjectCollection<KotlinTarget>) {
  fun NamedDomainObjectCollection<out KotlinTarget>.onlyBuildIf(enabled: Spec<in Task>) {
    all {
      if (this is KotlinNativeTarget) {
        binaries.all {
          linkTask.onlyIf(enabled)
        }
      }
      compilations.all {
        compileKotlinTask.onlyIf(enabled)
      }
    }
  }

  val nativeTargets = targets.withType<KotlinNativeTarget>()
  val windowsHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.MINGW }
  val linuxHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.LINUX }
  val osxHostTargets = nativeTargets.matching { it.konanTarget.buildHost == Family.OSX }
  val mainHostTargets = targets.matching { it !in nativeTargets }
  linuxHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsLinux }
  osxHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsMac }
  windowsHostTargets.onlyBuildIf { !CI || SANDBOX || HostManager.hostIsMingw }
  mainHostTargets.onlyBuildIf { !CI || SANDBOX || isMainHost }
}
