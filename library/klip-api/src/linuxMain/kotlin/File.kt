package dev.petuska.klip.ext

import platform.posix.mkdir

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path, permissions.toUInt())
