package dev.petuska.klip.ext

import platform.posix.mkdir

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "\\"

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = "\r\n"

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path)

internal actual fun mppRealpath(path: String): String? = path
