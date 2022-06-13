package dev.petuska.klip.core

import dev.petuska.klip.api.KlipContext
import dev.petuska.klip.core.domain.Klip
import dev.petuska.klip.core.domain.TypedKlip
import dev.petuska.klip.core.domain.raw
import dev.petuska.klip.core.domain.typed
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal expect object KlipManager {
  suspend fun writeKlip(context: KlipContext, klip: Klip)
  suspend fun readKlip(context: KlipContext): Klip?
}

public suspend fun KlipContext.syncKlip(actual: () -> Klip): Klip {
  return if (update) {
    actual().also {
      KlipManager.writeKlip(this, it)
    }
  } else {
    KlipManager.readKlip(this) ?: actual().also {
      KlipManager.writeKlip(this, it)
    }
  }
}

public suspend inline fun <reified T> KlipContext.syncTypedKlip(crossinline actual: () -> TypedKlip<T>): TypedKlip<T> {
  val klip = syncKlip {
    actual().raw()
  }
  return klip.typed()
}
