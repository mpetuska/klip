package dev.petuska.klip.assert

import dev.petuska.klip.Klippable
import dev.petuska.klip.int.KlipContext
import dev.petuska.klip.int.KlipManager
import dev.petuska.klip.validate
import kotlin.test.assertEquals

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 */
@Klippable
public fun assertMatchesKlip(actual: Any?, _context: KlipContext? = null) {
  _context.validate()
  val klip = KlipManager.klip(_context) { actual.toString() }
  assertEquals(klip, actual)
}

/**
 * Asserts that the given object matches its respective klip
 * @receiver actual value to assert
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 */
@Klippable
public fun Any?.assertKlip(_context: KlipContext? = null) {
  assertMatchesKlip(this, _context)
}
