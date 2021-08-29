package dev.petuska.klip.ext

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import platform.posix.mkdir
import platform.posix.realpath

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "/"

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = "\n"

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path, permissions.toUInt())

internal actual fun mppRealpath(path: String): CPointer<ByteVar>? = realpath(path, null)
