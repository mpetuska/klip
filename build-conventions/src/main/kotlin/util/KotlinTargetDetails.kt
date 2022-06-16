package util

import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

val KonanTarget.buildHost: Family
  get() = when (this) {
    KonanTarget.WASM32,
    KonanTarget.ANDROID_X64,
    KonanTarget.ANDROID_X86,
    KonanTarget.ANDROID_ARM32,
    KonanTarget.ANDROID_ARM64,
    KonanTarget.LINUX_ARM64,
    KonanTarget.LINUX_ARM32_HFP,
    KonanTarget.LINUX_MIPS32,
    KonanTarget.LINUX_MIPSEL32,
    KonanTarget.LINUX_X64 -> Family.LINUX

    KonanTarget.MINGW_X86,
    KonanTarget.MINGW_X64 -> Family.MINGW

    KonanTarget.IOS_ARM32,
    KonanTarget.IOS_ARM64,
    KonanTarget.IOS_X64,
    KonanTarget.IOS_SIMULATOR_ARM64,
    KonanTarget.WATCHOS_ARM32,
    KonanTarget.WATCHOS_ARM64,
    KonanTarget.WATCHOS_X86,
    KonanTarget.WATCHOS_X64,
    KonanTarget.WATCHOS_SIMULATOR_ARM64,
    KonanTarget.TVOS_ARM64,
    KonanTarget.TVOS_X64,
    KonanTarget.TVOS_SIMULATOR_ARM64,
    KonanTarget.MACOS_X64,
    KonanTarget.MACOS_ARM64 -> Family.OSX

    is KonanTarget.ZEPHYR -> throw IllegalStateException("Target $this not supported")
  }
