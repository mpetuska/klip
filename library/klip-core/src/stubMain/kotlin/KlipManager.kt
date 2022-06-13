package dev.petuska.klip.core

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.core.domain.Klip

internal actual object KlipManager {
  actual suspend fun writeKlip(context: KlipContext, klip: Klip): Unit = stub
  actual suspend fun readKlip(context: KlipContext): Klip? = stub
}
