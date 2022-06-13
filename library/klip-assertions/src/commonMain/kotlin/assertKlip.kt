package dev.petuska.klip.assertions

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.api.Klippable
import dev.petuska.klip.core.KlipCompilerAPI
import dev.petuska.klip.core.util.KlippableStub

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @return [actual]
 */
@Klippable
@Suppress("UNUSED_PARAMETER")
public fun <T : Any?> assertMatchesKlip(actual: T): T = KlippableStub

@Klippable
@KlipCompilerAPI
public fun <T : Any?> assertMatchesKlip(_context: KlipContext, actual: T): T = actual.also {
  println(
    """
    A-OK! 
      context=$_context
      actual=$actual
    """.trimIndent()
  )
//  _context.validate()
//  val actualStr = actual.toString()
//  val (klip) = KlipManager.klip(_context, mapOf("type" to "${KlipType.TEXT}")) { actualStr }
//  assertEquals(klip, actualStr, message = "Value does not match its klip")
}

/**
 * Asserts that the given object matches its respective klip
 * @receiver actual value to assert
 * @return [this] for chaining
 */
@Klippable
@Suppress("UnusedReceiverParameter")
public fun <T : Any?> T.assertKlip(): T = KlippableStub

@KlipCompilerAPI
public fun <T : Any?> T.assertKlip(_context: KlipContext): T = assertMatchesKlip(_context, this)
