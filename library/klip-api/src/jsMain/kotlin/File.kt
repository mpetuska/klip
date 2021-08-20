package dev.petuska.klip.ext

internal external fun require(module: String): dynamic

private val jsPath = require("path")
private val fs = require("fs")

public actual class File actual constructor(private val path: String) {
  public actual fun getAbsolutePath(): String = jsPath.normalize(path)
  public actual fun getPath(): String = path
  public actual fun mkdirs(): Boolean = kotlin.runCatching {
    fs.mkdirSync(path, jsObject { recursive = true })
  }.isSuccess

  public actual fun getParentFile(): File = File(jsPath.dirname(path))
  public actual fun exists(): Boolean = fs.existsSync(path)

  override fun toString(): String = getPath()
  public actual fun isDirectory(): Boolean = fs.lstatSync(path).isDirectory()
}

public actual fun File.writeText(text: String): Unit = fs.writeFileSync(getAbsolutePath(), text, "utf8")
public actual fun File.readText(): String = fs.readFileSync(getAbsolutePath(), "utf8")
public actual fun File.deleteRecursively(): Boolean = runCatching {
  if (isDirectory()) {
    fs.rmdirSync(getPath(), jsObject { recursive = true })
  } else {
    fs.unlinkSync(getPath())
  }
}.isSuccess
