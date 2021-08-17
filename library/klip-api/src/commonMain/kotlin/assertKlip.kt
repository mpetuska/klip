package dev.petuska.klip

import kotlin.test.assertEquals

@Klippable
fun assertKlip(actual: Any?, path: String? = null, key: String? = null, update: Boolean? = null) {
  verifyKlippable(path, key, update)
  val klip = KlipManager.klip(update, path, key) { actual.toString() }
  assertEquals(klip, actual)
}

@Klippable
fun Any?.assertMatchesKlip(path: String? = null, key: String? = null, update: Boolean? = null) {
  assertKlip(this, path, key, update)
}
