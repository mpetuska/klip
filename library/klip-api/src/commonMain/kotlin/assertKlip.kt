package dev.petuska.klip

import kotlin.test.assertEquals

fun assertKlip(actual: Any?, o: Any? = null, path: String? = null, key: String? = null, update: Boolean? = null) {
  println("KLIP: $actual, $o, $path, $key, $update")
  requireNotNull(path) { "Klip path should not be null and set by the compiler plugin. Did compiler plugin run?" }
  requireNotNull(key) { "Klip key should not be null and set by the compiler plugin. Did compiler plugin run?" }
  requireNotNull(update) { "Klip update should not be null and set by the compiler plugin. Did compiler plugin run?" }
  val klip = KlipManager.klip(update, path, key) { actual.toString() }
  assertEquals(klip, actual)
}
