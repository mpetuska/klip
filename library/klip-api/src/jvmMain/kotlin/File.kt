package dev.petuska.klip.ext

import kotlin.io.deleteRecursively as kDeleteRecursively
import kotlin.io.readText as kReadText
import kotlin.io.writeText as kWriteText

public actual typealias File = java.io.File

public actual fun File.writeText(text: String): Unit = kWriteText(text)
public actual fun File.readText(): String = kReadText()
public actual fun File.deleteRecursively(): Boolean = kDeleteRecursively()

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = File.separator

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = System.lineSeparator()
