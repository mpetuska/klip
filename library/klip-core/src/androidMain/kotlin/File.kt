package dev.petuska.klip.core.ext

import kotlin.io.deleteRecursively as kDeleteRecursively
import kotlin.io.readText as kReadText
import kotlin.io.writeText as kWriteText

actual typealias File = java.io.File

actual fun File.writeText(text: String): Unit = kWriteText(text)
actual fun File.readText(): String = kReadText()
actual fun File.deleteRecursively(): Boolean = kDeleteRecursively()

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
actual val File.separator: String get() = File.separator

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
actual val File.newline: String get() = System.lineSeparator()
