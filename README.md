[![Gitpod ready-to-code](https://img.shields.io/badge/gitpod-ready_to_code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/mpetuska/klip)
[![Slack chat](https://img.shields.io/badge/kotlinlang-chat-green?logo=slack&style=flat-square)](https://kotlinlang.slack.com/team/UL1A5BA2X)
[![Dokka docs](https://img.shields.io/badge/docs-dokka-orange?style=flat-square)](http://mpetuska.github.io/klip)
[![Version gradle-plugin-portal](https://img.shields.io/maven-metadata/v?label=gradle%20plugin%20portal&logo=gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fdev.petuska%2Fklip%2Fmaven-metadata.xml&style=flat-square)](https://plugins.gradle.org/plugin/dev.petuska.klip)
[![Version maven-central](https://img.shields.io/maven-central/v/dev.petuska/klip?logo=apache-maven&style=flat-square)](https://mvnrepository.com/artifact/dev.petuska/klip/latest)

# KLIP

Kotlin Multiplatform (pending KSP support) snapshot (klip) manager for tests

# Modules

* core - runtime library
* processor - ksp-based annotation processor to enable runtime work
* gradle-plugin - a gradle plugin to manage dependencies, config and ksp
* sandbox - a preconfigured sandbox to try out the setup without having to publish stuff

# Usage

1. Apply the plugin

```kotlin
plugins {
  id("dev.petuska.klip") version "<version>"
}
```

2. (Optional) Configure the plugin extension

```kotlin
klip {
  root = File("some/custom/sources/root/src") // Unlikely to ever be anything but "./src" (default)
  update = false // If set to true will overwrite the klips when running tests. Avoid hard-coding this.
}
```

3. Annotate your test classes where you want to use klips with `@dev.petuska.klip.Klippable`

## Properties

Most of the DSL configuration options can also be set/overridden via gradle properties
`./gradlew <some-task> -Pprop.name=propValue`, `gradle.properties` or `~/.gradle/gradle.properties`. Bellow is the full
list of supported properties:

* `klip.root`
* `klip.update`

## Basic Flow

1. Run tests as normal and use generated klip assertions such
   as `assertKlip(id = "some id unique in test class scope", actual = MyActualValue())`. New klips will always be
   written to files, whereas existing ones (identified by test class scope and given id) will be read and used for
   assertions.
2. When the actual value changes, tests will fail since they do not match previous klip. In such cases inspect the
   differences and if everything is as expected, re-run test(s) with klip updates enabled. This is done by either
   passing a gradle prop `./gradlew test -Pklip.update`
   or setting an environment variable `KLIP_UPDATE=true ./gradlew test`.
