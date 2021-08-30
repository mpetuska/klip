package dev.petuska.klip.ext

import kotlinx.cinterop.convert
import platform.posix.mkdir

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "/"

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = "\n"

/**
 * Checks if file path is starting from root (thanks a bunch, windows...)
 */
internal actual val File.isRooted: Boolean get() = getPath().startsWith("/")

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path, permissions.convert())
