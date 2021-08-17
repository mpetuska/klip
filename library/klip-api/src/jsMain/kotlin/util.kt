package dev.petuska.klip

internal fun <T> jsObject(builder: T.() -> Unit) = js("{}").unsafeCast<T>().apply(builder)
