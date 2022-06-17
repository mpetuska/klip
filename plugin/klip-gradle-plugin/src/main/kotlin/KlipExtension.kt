package dev.petuska.klip.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/** Gradle extension to manage klip plugin properties */
@Suppress("unused")
abstract class KlipExtension {
  abstract val debug: Property<Boolean>

  /** Whether plugin is enabled */
  abstract val enabled: Property<Boolean>

  /** Whether the klips should be updated */
  abstract val update: Property<Boolean>

  /**
   * Fully qualified annotation names to register for compiler processing and path injection.
   * Setting this property gives you full control over the annotations and overrides the default
   * ones.
   */
  abstract val klipAnnotations: SetProperty<String>

  /**
   * Register an annotation for compiler processing and path injection
   * @param fqName fully qualified annotation name to register
   */
  fun klipAnnotation(fqName: String) {
    klipAnnotations.add(fqName)
  }

  /**
   * Fully qualified annotation names to register function scopes for compiler klip detection and
   * processing Setting this property gives you full control over the annotations and overrides the
   * default ones.
   */
  abstract val scopeAnnotations: SetProperty<String>

  /**
   * Register an annotation for function scopes for compiler klip detection and processing
   * @param fqName fully qualified annotation name to register
   */
  fun scopeAnnotation(fqName: String) {
    scopeAnnotations.add(fqName)
  }

  /**
   * Fully qualified function names to register function scopes for compiler klip detection and
   * processing Setting this property gives you full control over the functions and overrides the
   * default ones.
   */
  abstract val scopeFunctions: SetProperty<String>

  /**
   * Register a function for function scopes for compiler klip detection and processing
   * @param fqName fully qualified function name to register
   */
  fun scopeFunction(fqName: String) {
    scopeFunctions.add(fqName)
  }

  companion object {
    /** Extension name */
    const val NAME = dev.petuska.klip.plugin.config.NAME
  }
}

/** Gradle extension to manage klip plugin properties applied to root project */
abstract class KlipRootExtension : KlipExtension() {
  abstract val port: Property<Int>
}

/**
 * Klip plugin extension
 * @throws IllegalStateException if the plugin did not register an extension yet
 */
internal inline val Project.klip: KlipExtension
  get() = extensions.getByType(KlipExtension::class.java)

/**
 * Root klip plugin extension
 * @throws IllegalStateException if the plugin did not register an extension yet
 */
internal inline val Project.rootKlip: KlipRootExtension
  get() = rootProject.extensions.getByType(KlipRootExtension::class.java)

/**
 * Configure klip plugin extension
 * @throws IllegalStateException if the plugin did not register an extension yet
 */
internal inline fun Project.klip(config: KlipExtension.() -> Unit): KlipExtension = klip.apply(config)
