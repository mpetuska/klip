package dev.petuska.klip

import kotlin.contracts.contract

/**
 * Annotation to mark functions to be picked up by the compiler plugin.
 * Function signature must end with three optional nullable arguments as shown bellow:
 *
 * ```
 *   fun klippable(..., path: String? = null, key: String? = null, update: Boolean? = null)
 * ```
 * @param path absolute path to the file where klips should be written
 * @param key unique key for the klip withing the given file
 * @param update whether klips should be updated
 * @see [assertMatchesKlip]
 */
public annotation class Klippable

/**
 * Utility function to verify compiler-injected arguments
 */
public fun verifyKlippable(path: String?, key: String?, update: Boolean?) {
  contract {
    returns() implies (path != null)
    returns() implies (key != null)
    returns() implies (update != null)
  }
  requireNotNull(path) { "Klip path should not be null and set by the compiler plugin. Did compiler plugin run?" }
  requireNotNull(key) { "Klip key should not be null and set by the compiler plugin. Did compiler plugin run?" }
  requireNotNull(update) { "Klip update should not be null and set by the compiler plugin. Did compiler plugin run?" }
}
