package dev.petuska.klip.ext

import kotlinx.cinterop.convert
import kotlinx.cinterop.toKString
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

internal actual fun mppRealpath(path: String): String? = realpath(path, null)?.toKString()

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path, permissions.convert())
