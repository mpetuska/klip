package dev.petuska.klip.assertions

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.api.Klippable
import dev.petuska.klip.api.KlippableStub
import dev.petuska.klip.core.KlipCompilerAPI
import dev.petuska.klip.core.domain.TypedKlip
import dev.petuska.klip.core.syncTypedKlip
import kotlin.test.assertEquals

/**
 * Asserts that the given object matches its respective klip
 * @param actual value to assert
 * @return [actual]
 */
@Klippable
@Suppress("UNUSED_PARAMETER", "RedundantSuspendModifier")
public suspend inline fun <T : Any?> assertMatchesKlip(actual: T): T = KlippableStub

@Klippable
@KlipCompilerAPI
public suspend inline fun <reified T : Any?> assertMatchesKlip(context: KlipContext, actual: T): T = actual.also {
  val klip = context.syncTypedKlip {
    TypedKlip(data = actual)
  }
  assertEquals(klip.data, actual, message = "Value does not match its klip")
}

/**
 * Asserts that the given object matches its respective klip
 * @receiver actual value to assert
 * @return [this] for chaining
 */
@Klippable
@Suppress("UnusedReceiverParameter", "RedundantSuspendModifier")
public suspend inline fun <T : Any?> T.assertKlip(): T = KlippableStub

@KlipCompilerAPI
public suspend inline fun <reified T : Any?> T.assertKlip(context: KlipContext): T = assertMatchesKlip(context, this)
