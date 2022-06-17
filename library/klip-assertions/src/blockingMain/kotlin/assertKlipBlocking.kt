package dev.petuska.klip.assertions

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.api.Klippable
import dev.petuska.klip.api.KlippableStub
import dev.petuska.klip.core.KlipCompilerAPI
import kotlinx.coroutines.runBlocking

/**
 * Asserts that the given object matches its respective klip
 * @param T
 * @param actual value to assert
 * @return [actual]
 */
@Klippable
@Suppress("UNUSED_PARAMETER")
public inline fun <T : Any?> assertMatchesKlipBlocking(actual: T): T = KlippableStub

@Klippable
@KlipCompilerAPI
public inline fun <reified T : Any?> assertMatchesKlipBlocking(context: KlipContext, actual: T): T = actual.also {
  runBlocking {
    assertMatchesKlip(context, actual)
  }
}

/**
 * Asserts that the given object matches its respective klip
 * @param T
 * @receiver actual value to assert
 * @return [this] for chaining
 */
@Klippable
@Suppress("UnusedReceiverParameter")
public inline fun <T : Any?> T.assertKlipBlocking(): T = KlippableStub

@KlipCompilerAPI
public inline fun <reified T : Any?> T.assertKlipBlocking(context: KlipContext): T =
  assertMatchesKlipBlocking(context, this)
