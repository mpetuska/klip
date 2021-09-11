package dev.petuska.klip.core.ext

internal fun <T> jsObject(builder: T.() -> Unit) = js("{}").unsafeCast<T>().apply(builder)
