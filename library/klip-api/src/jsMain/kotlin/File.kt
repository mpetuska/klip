package dev.petuska.klip

internal external fun require(module: String): dynamic

private val jsPath = require("path")
private val fs = require("fs")

actual class File actual constructor(private val path: String) {
  actual fun getAbsolutePath(): String = jsPath.normalize(path)
  actual fun getPath(): String = path
  actual fun mkdirs(): Boolean = kotlin.runCatching {
    fs.mkdirSync(path, jsObject { recursive = true })
  }.isSuccess

  actual fun getParentFile(): File = File(jsPath.dirname(path))
  actual fun exists(): Boolean = fs.existsSync(path)

  override fun toString(): String = getAbsolutePath()
  actual fun isDirectory(): Boolean = fs.lstatSync(path).isDirectory()
}

actual fun File.writeText(text: String) = fs.writeFileSync(getAbsolutePath(), text, "utf8")
actual fun File.readText(): String = fs.readFileSync(getAbsolutePath(), "utf8")
actual fun File.deleteRecursively(): Boolean = runCatching {
  if (isDirectory()) {
    fs.rmdirSync(getPath(), jsObject { recursive = true })
  } else {
    fs.unlinkSync(getPath())
  }
}.isSuccess
