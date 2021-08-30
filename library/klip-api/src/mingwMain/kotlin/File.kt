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

/**
 * Checks if file path is starting from root (thanks a bunch, windows...)
 */
internal actual val File.isRooted: Boolean get() = getPath().matches("^[A-z]:\\$separator".toRegex())
