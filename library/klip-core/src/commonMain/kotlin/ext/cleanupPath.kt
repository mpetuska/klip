package dev.petuska.klip.core.ext

internal fun File.cleanupPath(path: String): String = path.replace("/", separator).replace("\\", separator)
