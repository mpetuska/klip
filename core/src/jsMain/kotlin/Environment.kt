package dev.petuska.klip

import NodeJS.Dict
import process

private external val Object: dynamic
private fun <T> Dict<T>.toMap(): Map<String, T> {
  return Object.entries(this).unsafeCast<Array<Array<Any>>>().associate {
    it[0].unsafeCast<String>() to it[2].unsafeCast<T>()
  }
}

actual object Environment : Map<String, String> by process.env.toMap()
