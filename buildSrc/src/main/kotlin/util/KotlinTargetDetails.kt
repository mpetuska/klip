package util


enum class KotlinTargetDetails(
  val presetName: String,
  val hasCoroutines: Boolean,
) {
  JVM("jvm", true),
  ANDROID("android", false),
  JS("jsIr", true),

  ANDROID_NDK_ARM32("androidNativeArm32", false),
  ANDROID_NDK_ARM64("androidNativeArm64", false),

  IOS_ARM32("iosArm32", true),
  IOS_ARM64("iosArm64", true),
  IOS_X64("iosX64", true),

  WATCHOS_X86("watchosX86", true),
  WATCHOS_X64("watchosX64", true),
  WATCHOS_ARM64("watchosArm64", true),
  WATCHOS_ARM32("watchosArm32", true),

  TVOS_ARM64("tvosArm64", true),
  TVOS_X64("tvosX64", true),

  MACOS_X64("macosX64", true),

  LINUX_ARM32_HFP("linuxArm32Hfp", false),
  LINUX_MIPS32("linuxMips32", false),
  LINUX_MIPSEL32("linuxMipsel32", false),
  LINUX_X64("linuxX64", true),
  LINUX_ARM64("linuxArm64", false),

  MINGW_X64("mingwX64", true),
  MINGW_X32("mingwX86", false),
}
