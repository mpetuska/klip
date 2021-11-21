package dev.petuska.klip.api

import dev.petuska.klip.core.Klippable
import dev.petuska.klip.core.int.KlipContext
import dev.petuska.klip.core.int.KlipManager
import dev.petuska.klip.core.int.KlipType
import dev.petuska.klip.core.validate
import kotlin.test.assertEquals

@DslMarker
@Retention(AnnotationRetention.SOURCE)
internal annotation class KlipDsl

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 * @return [actual]
 */
@Klippable
@KlipDsl
public fun <T : Any?> assertMatchesKlip(actual: T, _context: KlipContext? = null): T = actual.also {
  _context.validate()
  val actualStr = actual.toString()
  val (klip) = KlipManager.klip(_context, mapOf("type" to "${KlipType.TEXT}")) { actualStr }
  assertEquals(klip, actualStr, message = "Value does not match its klip")
}

/**
 * Asserts that the given object matches its respective klip
 * @receiver actual value to assert
 * @param _context [KlipContext] injected by the compiler with details about the klip file
 * @return [this]
 */
@Klippable
@KlipDsl
public fun <T : Any?> T.assertKlip(_context: KlipContext? = null): T = assertMatchesKlip(this, _context)
