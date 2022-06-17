[![Gitpod ready-to-code](https://img.shields.io/badge/gitpod-ready_to_code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/mpetuska/klip)
[![Slack chat](https://img.shields.io/badge/kotlinlang-chat-green?logo=slack&style=flat-square)](https://kotlinlang.slack.com/team/UL1A5BA2X)
[![Dokka docs](https://img.shields.io/badge/docs-dokka-orange?style=flat-square)](http://mpetuska.github.io/klip)
[![Version gradle-plugin-portal](https://img.shields.io/maven-metadata/v?label=gradle%20plugin%20portal&style=flat-square&logo=gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fdev.petuska%2Fklip-gradle-plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/dev.petuska.klip)
[![Version maven-central](https://img.shields.io/maven-central/v/dev.petuska/klip?logo=apache-maven&style=flat-square)](https://mvnrepository.com/artifact/dev.petuska/klip/latest)

# KLIP

Kotlin Multiplatform snapshot ((c|k)lip) manager for tests. Automatically generates and asserts against a
persistent `@kotlinx.serialization.Serializable` representation of the object until you explicitly trigger an update.
Powered by kotlin compiler plugin to inject relevant keys and paths.

# Support

The plugin only works on targets using new IR kotlin compiler (which is pretty much all of them since kotlin 1.5 except
JS which still defaults to legacy compiler).

# Versions

The current version was built using the following tooling versions and is guaranteed to work with this setup. Given the
experimental nature of kotlin compiler plugin API, the plugin powering this library is likely to stop working on
projects using newer/older kotlin versions.

* Kotlin: `1.7.0`
* Gradle: `7.4.2`
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

There's also a subset of targets that you currently cannot run tests on (and as such making the library redundant).
These targets will use a fallback implementation that throws an error on native api access (since those targets will not
execute tests) to enable the general library usage in commonMain source set. If you have a valid use-case of the library
for these targets, please raise an issue to discuss a real implementation.

- [x] androidNativeArm32
- [x] androidNativeX86
- [x] androidNativeArm64
- [x] androidNativeX64
- [x] linuxArm32Hfp
- [x] linuxMips32
- [x] linuxMipsel32
- [x] linuxArm64
- [x] mingwX86

# Usage

1. Apply the plugin and add a runtime dependency.
2. If you're not using `dev.petuska:klip` marker dependency, you'll also need to add an appropriate ktor-client-engine
   for each platform

```kotlin
plugins {
  kotlin("multiplatform")
  id("dev.petuska.klip") version "<<version>>"

  kotlin {
    sourceSets {
      commonTest {
        dependencies {
          implementation("dev.petuska:klip")
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
  enabled.set(true) // Turns the compiler plugin on/off
  update.set(false) // Whether to overwrite the existing klips while running tests
  klipAnnotations.set(setOf("dev.petuska.klip.core.Klippable")) // Takes full control of annotations
  klipAnnotation("dev.petuska.klip.core.Klippable") // Appends the annotation to the default ones
  scopeAnnotations.set(
    setOf(
      // Takes full control of annotations
      "kotlin.test.Test",
      "org.junit.Test",
      "org.junit.jupiter.api.Test",
      "org.testng.annotations.Test",
      "io.kotest.core.spec.style.AnnotationSpec.Test",
    )
  )
  scopeAnnotation("kotlin.test.Test") // Appends the annotation to the default ones
  scopeFunctions.set(
    setOf(
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
  )
  scopeFunction("io.kotest.core.spec.style.scopes.FunSpecRootContext.test") // Appends the function to the default ones
}
```

3. Use provided klip assertions anywhere under one of the `scopeAnnotations` or `scopeFunctions`.

```kotlin
class MyTest {
  data class DomainObject(val name: String, val value: String?)

  @Test
  fun test1() = runTest {
    assertMatchesKlip(DomainObject("Dick", "Dickens"))
    DomainObject("John", "Doe").assertKlip()
  }

  @Test
  fun test2() = runTest {
    doAssertions()
  }

  private suspend fun doAssertions() {
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

## Basic Flow

1. Run tests as normal and use generated klip assertions such as `assertMatchesKlip(myObject)`
   or `myObject.assertKlip()`. New klips will always be written to file, whereas existing ones (identified by test class
   scope and given id) will be read and used for assertions.
2. When the actual value changes, tests will fail since they do not match previous klip. In such cases inspect the
   differences and if everything is as expected, re-run test(s) with klip updates enabled. This is done by either
   passing a gradle prop `./gradlew test -Pklip.update`,
   setting an environment variable `KLIP_UPDATE=true ./gradlew test`.

# Modules

* `:library:klip-core` - main runtime library
* `:library:klip-api` - shared api and utility DSLs
* `:library:klip-assertions` - assertion api
* `:library:klip-runner` - abstraction over `kotlinx-coroutines-test` to provide truly multiplatform way to run
  suspending tests
* `:plugin:klip-gradle-plugin` - gradle plugin to manage kotlin compiler plugins
* `:plugin:klip-kotlin-plugin` - kotlin compiler plugin for jvm & js that does the actual work
* `:plugin:klip-kotlin-plugin-native` - kotlin compiler plugin for native that does the actual work
* `klip-sandbox` - a playground to test local changes from consumer end
