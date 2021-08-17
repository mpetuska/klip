package dev.petuska.klip.ext

internal fun <T> jsObject(builder: T.() -> Unit) = js("{}").unsafeCast<T>().apply(builder)
