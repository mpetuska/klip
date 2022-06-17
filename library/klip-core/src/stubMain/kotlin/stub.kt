package dev.petuska.klip.core

public inline val stub: Nothing
  get() = throw NotImplementedError(
    "This API is stubbed on the current target and should never be reachable from tests"
  )
