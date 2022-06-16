package dev.petuska.klip.core

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.core.domain.Klip
import kotlin.reflect.KType

internal actual object KlipManager {
  actual suspend fun <T> writeKlip(context: KlipContext, klip: Klip<T>): Unit = stub
  actual suspend fun <T> readKlip(context: KlipContext, type: KType): Klip<T>? = stub
}
