package dev.petuska.klip

import kotlin.js.json
import path.path as jsPath

actual class File actual constructor(private val path: String) {
    actual fun getAbsolutePath(): String = jsPath.resolve(path)
    actual fun getPath(): String = path
}