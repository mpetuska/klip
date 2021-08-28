package dev.petuska.klip.ext

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "\\"
