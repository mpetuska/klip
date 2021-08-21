package dev.petuska.klip

import kotlin.test.assertEquals

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @param path file path to retrieve and save klip for the given context
 * @param key key to write the klip under in the file at [path]
 * @param update whether the klip should be overridden instead of asserted
 */
@Klippable
public fun assertMatchesKlip(actual: Any?, path: String? = null, key: String? = null, update: Boolean? = null) {
  verifyKlippable(path, key, update)
  val klip = KlipManager.klip(update, path, key) { actual.toString() }
  assertEquals(klip, actual)
}

/**
 * Asserts that the given object matches its respective klip
 * @receiver value to assert
 * @param path file path to retrieve and save klip for the given context
 * @param key key to write the klip under in the file at [path]
 * @param update whether the klip should be overridden instead of asserted
 */
@Klippable
public fun Any?.assertKlip(path: String? = null, key: String? = null, update: Boolean? = null) {
  assertMatchesKlip(this, path, key, update)
}
