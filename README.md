[![Gitpod ready-to-code](https://img.shields.io/badge/gitpod-ready_to_code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/mpetuska/klip)
[![Slack chat](https://img.shields.io/badge/kotlinlang-chat-green?logo=slack&style=flat-square)](https://kotlinlang.slack.com/team/UL1A5BA2X)
[![Dokka docs](https://img.shields.io/badge/docs-dokka-orange?style=flat-square)](http://mpetuska.github.io/klip)
[![Version gradle-plugin-portal](https://img.shields.io/maven-metadata/v?label=gradle%20plugin%20portal&style=flat-square&logo=gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fdev.petuska%2Fklip-gradle-plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/dev.petuska.klip)
[![Version maven-central](https://img.shields.io/maven-central/v/dev.petuska/klip?logo=apache-maven&style=flat-square)](https://mvnrepository.com/artifact/dev.petuska/klip/latest)

# KLIP

Kotlin Multiplatform snapshot ((c|k)lip) manager for tests. Automatically generates and asserts against a
persistent `Any::toString()` representation of the object until you explicitly trigger an update. Powered by kotlin
compiler plugin to inject relevant keys and paths.

# Support

The plugin only works on targets using new IR kotlin compiler (which is pretty much all of them since kotlin 1.5 except
JS which still defaults to legacy compiler).

# Versions

The current version was built using the following tooling versions and is guaranteed to work with this setup. Given the
experimental nature of kotlin compiler plugin API, the plugin powering this library is likely to stop working on
projects using newer/older kotlin versions.

* Kotlin: `1.6.0`
* Gradle: `7.2.0`
* JDK: `11`

# Targets

Bellow is a list of currently supported targets and planned targets:

- [x] android
- [x] js
- [x] jvm
- [x] linuxX64
- [x] mingwX64
- [x] macosX64
- [x] macosArm64
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

# Usage

1. Apply the plugin and add a runtime dependency.

```kotlin
plugins {
  kotlin("multiplatform")
  id("dev.petuska.klip") version "<<version>>"

  kotlin {
    sourceSets {
      commonTest {
        dependencies {
          implementation("dev.petuska:klip:<<version>>")
        }
      }
    }
  }
}
```

2. (Optional) Configure the plugin extension (shown with default values). For property descriptions.
   see [Gradle Properties](#gradle-properties)

```kotlin
klip {
  enabled = true // Turns the compiler plugin on/off
  update = false // Whether to overwrite the existing klips while running tests
  klipAnnotations = setOf("dev.petuska.klip.core.Klippable") // Takes full control of annotations
  klipAnnotation("dev.petuska.klip.core.Klippable") // Appends the annotation to the default ones
  scopeAnnotations = setOf(
    // Takes full control of annotations
    "kotlin.test.Test",
    "org.junit.Test",
    "org.junit.jupiter.api.Test",
    "org.testng.annotations.Test",
    "io.kotest.core.spec.style.AnnotationSpec.Test",
  )
  scopeAnnotation("kotlin.test.Test") // Appends the annotation to the default ones
  scopeFunctions = setOf(
    // Takes full control of functions
    "io.kotest.core.spec.style.scopes.FunSpecRootScope.test",
    "io.kotest.core.spec.style.scopes.DescribeSpecContainerScope.it",
    "io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope.Then",
    "io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope.then",
    "io.kotest.core.spec.style.scopes.WordSpecShouldContainerScope.invoke",
    "io.kotest.core.spec.style.scopes.FreeSpecContainerScope.invoke",
    "io.kotest.core.spec.style.scopes.FeatureSpecContainerScope.scenario",
    "io.kotest.core.spec.style.scopes.ExpectSpecContainerScope.expect",
  )
  scopeFunction("io.kotest.core.spec.style.scopes.FunSpecRootContext.test") // Appends the function to the default ones
}
```

3. Use provided klip assertions anywhere under one of the `scopeAnnotations` or `scopeFunctions`.

```kotlin
class MyTest {
  data class DomainObject(val name: String, val value: String?)

  @Test
  fun test1() {
    assertMatchesKlip(DomainObject("Dick", "Dickens"))
    DomainObject("John", "Doe").assertKlip()
  }

  @Test
  fun test2() {
    doAssertions()
  }

  private fun doAssertions() {
    assertMatchesKlip(DomainObject("Joe", "Mama"))
    DomainObject("Ben", "Dover").assertKlip()
  }
}
```

## Gradle Properties

Most of the DSL configuration options can also be set/overridden via gradle properties
`./gradlew <some-task> -Pprop.name=propValue`, `gradle.properties` or `~/.gradle/gradle.properties`. Environment
variables are also supported, however gradle properties take precedence over them. Bellow is the full list of supported
properties:

* `klip.enabled (KLIP_ENABLED)` - toggles the compiler processing on/off.
* `klip.update (KLIP_UPDATE)` - if true, will override and update all previous klips during test run.
* `klip.klipAnnotations (KLIP_KLIPANNOTATIONS)` - comma separated list of fully qualified names of annotations to
  process. Only useful when writing your own klippable functions.
* `klip.scopeAnnotations (KLIP_SCOPEANNOTATIONS` - comma separated list of fully qualified names of test annotations to
  scope klip keys.
* `klip.scopeFunctions (KLIP_SCOPEFUNCTIONS` - comma separated list of fully qualified names of test functions to scope
  klip keys.

## Basic Flow

1. Run tests as normal and use generated klip assertions such as `assertMatchesKlip(myObject)`
   or `myObject.assertKlip()`. New klips will always be written to file, whereas existing ones (identified by test class
   scope and given id) will be read and used for assertions.
2. When the actual value changes, tests will fail since they do not match previous klip. In such cases inspect the
   differences and if everything is as expected, re-run test(s) with klip updates enabled. This is done by either
   passing a gradle prop `./gradlew test -Pklip.update`,
   setting an environment variable `KLIP_UPDATE=true ./gradlew test` or running update task `./gradlew klipUpdate`.

# Modules

* `:library:klip-core` - main runtime library
* `:library:klip-api` - assertion api and utility DSLs
* `:plugin:klip-gradle-plugin` - gradle plugin to manage kotlin compiler plugins
* `:plugin:klip-kotlin-plugin` - kotlin compiler plugin for jvm & js that does the actual work
* `:plugin:klip-kotlin-plugin:klip-kotlin-plugin-native` - kotlin compiler plugin for native that does the actual work
* `sandbox` - a playground to test local changes from consumer end
