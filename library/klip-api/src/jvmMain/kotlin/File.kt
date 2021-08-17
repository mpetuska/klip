package dev.petuska.klip.ext

import kotlin.io.deleteRecursively as kDeleteRecursively
import kotlin.io.readText as kReadText
import kotlin.io.writeText as kWriteText

actual typealias File = java.io.File

actual fun File.writeText(text: String) = kWriteText(text)
actual fun File.readText(): String = kReadText()
actual fun File.deleteRecursively(): Boolean = kDeleteRecursively()
