package dev.petuska.klip

import kotlin.test.assertEquals

@Klippable
public fun assertMatchesKlip(actual: Any?, path: String? = null, key: String? = null, update: Boolean? = null) {
  verifyKlippable(path, key, update)
  val klip = KlipManager.klip(update, path, key) { actual.toString() }
  assertEquals(klip, actual)
}

@Klippable
public fun Any?.assertKlip(path: String? = null, key: String? = null, update: Boolean? = null) {
  assertMatchesKlip(this, path, key, update)
}
