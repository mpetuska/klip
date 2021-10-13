package dev.petuska.klip.api

import dev.petuska.klip.core.Klippable
import dev.petuska.klip.core.int.KlipContext
import dev.petuska.klip.core.int.KlipManager
import dev.petuska.klip.core.validate
import kotlin.test.assertEquals

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 */
@Klippable
public fun assertMatchesKlip(actual: Any?, _context: KlipContext? = null) {
  _context.validate()
  val actualStr = actual.toString()
  val klip = KlipManager.klip(_context) { actualStr }
  assertEquals(klip, actualStr, "Value does not match its klip")
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
