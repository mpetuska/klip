[![Gitpod ready-to-code](https://img.shields.io/badge/gitpod-ready_to_code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/mpetuska/klip)
[![Slack chat](https://img.shields.io/badge/kotlinlang-chat-green?logo=slack&style=flat-square)](https://kotlinlang.slack.com/team/UL1A5BA2X)
[![Dokka docs](https://img.shields.io/badge/docs-dokka-orange?style=flat-square)](http://mpetuska.github.io/klip)
[![Version gradle-plugin-portal](https://img.shields.io/maven-metadata/v?label=gradle%20plugin%20portal&style=flat-square&logo=gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fdev.petuska%2Fklip-gradle-plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/dev.petuska.klip)
[![Version maven-central](https://img.shields.io/maven-central/v/dev.petuska/klip-gradle-plugin?logo=apache-maven&style=flat-square)](https://mvnrepository.com/artifact/dev.petuska/klip-gradle-plugin/latest)

# KLIP
Kotlin Multiplatform snapshot ((c|k)lip) manager for tests. Automatically generates and asserts against a
persistent `Any::toString()` representation of the object until you explicitly trigger an update. Powered by kotlin
compiler plugin to inject relevant keys and paths.

# Usage
1. Apply the plugin
```kotlin
plugins {
  id("dev.petuska.klip") version "<<version>>"
}
```
2. (Optional) Configure the plugin extension (shown with default values). For property descriptions.
   see [Gradle Properties](#gradle-properties)
```kotlin
klip {
  enabled = true
  update = false
  klipAnnotations = setOf("dev.petuska.klip.Klippable") // Takes full control of annotations
  klipAnnotation("dev.petuska.klip.Klippable") // Appends the annotation to the default ones
  scopeAnnotations = setOf( // Takes full control of annotations
    "kotlin.Test",
    "org.junit.Test",
    "org.junit.jupiter.api.Test"
  )
  scopeAnnotation( // Appends the annotation to the default ones
    "kotlin.Test",
    "org.junit.Test",
    "org.junit.jupiter.api.Test"
  )
}
```
3. Use provided klip assertions anywhere under one of the `scopeAnnotations`.
```kotlin
class MyTest {
  data class DomainObject(val name: String, val value: String?)

  @Test
  fun test1() {
    assertMatchesClip(DomainObject("Dick", "Dickens"))
    DomainObject("John", "Doe").assertKlip()
  }

  @Test
  fun test2() {
    doAssertions()
  }

  private fun doAssertions() {
    assertMatchesClip(DomainObject("Joe", "Mama"))
    DomainObject("Ben", "Dover").assertKlip()
  }
}
```

## Gradle Properties
Most of the DSL configuration options can also be set/overridden via gradle properties
`./gradlew <some-task> -Pprop.name=propValue`, `gradle.properties` or `~/.gradle/gradle.properties`. Environment
variables are also supported and take precedence over gradle properties. Bellow is the full list of supported
properties:
* `klip.enabled (KLIP_ENABLED)` - toggles the compiler processing on/off.
* `klip.update (KLIP_UPDATE)` - if true, will override and update all previous klips during test run.
* `klip.klipAnnotations (KLIP_KLIPANNOTATIONS)` - comma separated list of fully qualified names of annotations to
  process. Only useful when writing your own klippable functions.
* `klip.scopeAnnotations (KLIP_SCOPEANNOTATIONS` - comma separated list of fully qualified names of test annotations to
  scope klip keys.

## Basic Flow
1. Run tests as normal and use generated klip assertions such as `assertMatchesKlip(myObject)`
   or `myObject.assertKlip()`. New klips will always be written to file, whereas existing ones (identified by test class
   scope and given id) will be read and used for assertions.
2. When the actual value changes, tests will fail since they do not match previous klip. In such cases inspect the
   differences and if everything is as expected, re-run test(s) with klip updates enabled. This is done by either
   passing a gradle prop `./gradlew test -Pklip.update`
   or setting an environment variable `KLIP_UPDATE=true ./gradlew test`.

# Modules
* `:library:klip-api` - main runtime library
* `:plugin:klip-gradle-plugin` - gradle plugin to manage kotlin compiler plugins
* `:plugin:klip-common-plugin` - shared code between plugins (should not have any dependencies)
* `:plugin:klip-kotlin-plugin` - kotlin compiler plugin for jvm & js that does the actual work
* `:plugin:klip-kotlin-plugin:klip-kotlin-plugin-native` - kotlin compiler plugin for native that does the actual work
* `sandbox` - a playground to test local changes from consumer end
