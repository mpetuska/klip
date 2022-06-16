package dev.petuska.klip.core

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.core.domain.Klip
import kotlin.reflect.KType

internal expect object KlipManager {
  suspend fun <T> writeKlip(context: KlipContext, klip: Klip<T>)
  suspend fun <T> readKlip(context: KlipContext, type: KType): Klip<T>?
}

public suspend fun <T> KlipContext.syncKlip(actual: Klip<T>): Klip<T> {
  return if (update) {
    actual.also {
      KlipManager.writeKlip(this, it)
    }
  } else {
    KlipManager.readKlip(this, actual.type) ?: actual.also {
      KlipManager.writeKlip(this, it)
    }
  }
}
