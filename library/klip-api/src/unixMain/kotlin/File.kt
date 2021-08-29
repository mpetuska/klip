package dev.petuska.klip.ext

import kotlinx.cinterop.toKString
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
