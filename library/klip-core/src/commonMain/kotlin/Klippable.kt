package dev.petuska.klip.core

import dev.petuska.klip.core.int.KlipContext
import kotlin.contracts.contract

/**
 * Annotation to mark functions to be picked up by the compiler plugin.
 * Function signature must contain optional nullable [KlipContext] argument as shown bellow:
 *
 * ```
 *   @Klippable
 *   fun klippable(..., _context: KlipContext? = null)
 * ```
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 * @see [KlipContext.validate]
 */
public annotation class Klippable

/**
 * Utility function to verify compiler-injected arguments
 * @throws IllegalArgumentException if the validation fails
 */
public fun KlipContext?.validate() {
  contract {
    returns() implies (this@validate != null)
  }
  requireNotNull(this) { "KlipContext should not be null and set by the compiler plugin. Did compiler plugin run?" }
}
