# 0.3.0
## Versions
* Kotlin: `1.6.0`
* Gradle: `7.3.0`
* JDK: `11`
## Changes
* New config option `scopeFunctions` to allow registering scopes by qualified function names.
* Kotest support
* Klip attributes support. Useless for now, but opens up a world of potential improvements in the future.
* Bump kotlin version to `1.6.0`
* New `klipUpdate` gradle task for more convenient way to update klips

# 0.2.2
## Versions
* Kotlin: `1.5.31`
* Gradle: `7.2.0`
* JDK: `11`
## Changes
* Fix assertion functions to properly compare `Any?::toString()` representations
* Bump kotlin version to `1.5.31`

# 0.2.1
## Versions
* Kotlin: `1.5.30`
* Gradle: `7.2.0`
* JDK: `11`
## Changes
* Remaining android and apple targets added
  - [x] android
  - [x] iosArm32
  - [x] iosArm64
  - [x] iosSimulatorArm64
  - [x] iosX64
  - [x] watchosX86
  - [x] watchosX64
  - [x] watchosArm64
  - [x] watchosSimulatorArm64
  - [x] watchosArm32
  - [x] tvosArm64
  - [x] tvosSimulatorArm64
  - [x] tvosX64
* Per-target sandbox test to better assert correctness
* Previous `klip-api` module split into `klip-core` and `klip-api` to better isolate responsibilities. `klip-core` is
  bare minimum required for the plugin to work, whereas `klip-api` provides all the assertions and other DLS utilities.


# v0.2.0
## Versions
* Kotlin: `1.5.30`
* Gradle: `7.2.0`
* JDK: `11`
## Changes
* Reworked how klip settings are injected. Those will now be injected via a single argument of
  custom `dev.petuska.klip.int.KlipContext` type. This should prevent ambiguities with other arguments and helps to
  better identify injection target.
* Compiler plugin rework to be less aggressive. Now will only run if the plugin is enabled
  AND `dev.petuska.klip.int.KlipContext` class is detected (i.e. runtime dependency is added)
* Bumped kotlin to release version
* Got rid of `klip-common-plugin` module as it cannot be consumed by native plugin (replaced by build-configs plugin)
* Moved some classes and packages around for cleaner structure
* `MingwX64` and `macosArm64` support added
* `root` internal compiler option removed
* `*.klip` file resolution changed to work with relative paths to source files. Those will now be put
  in `${sourceFilePath}/__klips__/${sourceFileName}.klip`


# v0.1.0
## Versions
* Kotlin: `1.5.30-RC`
* Gradle: `7.2.0`
* JDK: `11`
## Changes
* Full rework of the previous preview version, restructuring the codebase and getting rid of ksp and kotlinx-nodejs
  (due to it still only being available at JCenter)
* Implemented a kotlin compiler plugin to fully support MPP
* New gradle plugin architecture building on top of kotlin gradle plugin sub-plugins API
* Reworked runtime library to make use of new compiler plugin features
* Added support for jvm, js, linuxX64 and macosX64 targets (remaining targets are coming!)
* New marker artefact `dev.petuska:klip:0.1.0` to conveniently bring all future runtime modules under one umbrella
* GH actions to support MPP testing and publishing
* Published additional fallback targets that cannot run tests to expand usage scope in `commonMain`
  * androidNativeArm32
  * androidNativeArm64
  * linuxArm32Hfp
  * linuxMips32
  * linuxMipsel32
  * linuxArm64
  * mingwX86


# v0.0.1
## Versions
* Kotlin: `1.5.10`
* Gradle: `7.0.0`
* JDK: `11`
## Changes
* Initial implementation of gradle plugin, runtime library and ksp-based processor supporting JVM
* Basic, yet flaky klip assertions.
