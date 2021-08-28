package dev.petuska.klip.ext

internal fun File.cleanupPath(path: String): String = path.replace("/", separator).replace("\\", separator)
