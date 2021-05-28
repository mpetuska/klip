package dev.petuska.klip

import kotlin.jvm.JvmField
import kotlin.jvm.JvmName

expect class File(path: String) {
    fun getPath(): String
    fun getAbsolutePath(): String
}