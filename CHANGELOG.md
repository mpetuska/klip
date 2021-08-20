# v0.1.0
## Versions
* Kotlin: 1.5.30-RC
* Gradle: 7.2.0
* JDK: 11

## Changes
* Full rework of the previous preview version, restructuring the codebase and getting rid of ksp and kotlinx-nodejs
  (due to it still only being available at JCenter)
* Implemented a kotlin compiler plugin to fully support MPP
* New gradle plugin architecture building on top of kotlin gradle plugin sub-plugins API
* Reworked runtime library to make use of new compiler plugin features
* Added support for jvm, js, linuxX64, mingwX64 and macosX64 targets (remaining targets are coming!)
* New marker artefact `dev.petuska:klip:0.1.0` to conveniently bring all future runtime modules under one umbrella
* GH actions to support MPP testing and publishing

# v0.0.1
## Versions
* Kotlin: 1.5.10
* Gradle: 7.0.0
* JDK: 11

## Changes
* Initial implementation of gradle plugin, runtime library and ksp-based processor supporting JVM
* Basic, yet flaky klip assertions.
