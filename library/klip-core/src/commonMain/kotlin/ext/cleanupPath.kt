package dev.petuska.klip.core.ext

internal fun cleanupPath(path: String, separator: String = "/"): String =
  path.replace("/", separator).replace("\\", separator)
