package dev.petuska.klip.ext

internal external fun require(module: String): dynamic

private val jsPath = require("path")
private val fs = require("fs")

/**
 * Multiplatform wrapper over java.io.File API
 */
public actual class File actual constructor(private val path: String) {
  /**
   * Returns absolute path to this file
   */
  public actual fun getAbsolutePath(): String = jsPath.normalize(path)

  /**
   * Returns local path to this file
   */
  public actual fun getPath(): String = path

  /**
   * Recursively makes all directories up to this directory file
   */
  public actual fun mkdirs(): Boolean = kotlin.runCatching {
    fs.mkdirSync(path, jsObject { recursive = true })
  }.isSuccess

  /**
   * Retrieves parent file
   */
  public actual fun getParentFile(): File = File(jsPath.dirname(path))

  /**
   * Checks if the file exsists
   */
  public actual fun exists(): Boolean = fs.existsSync(path)

  /**
   * checks if the file is directory
   */
  public actual fun isDirectory(): Boolean = fs.lstatSync(path).isDirectory()

  actual override fun toString(): String = getPath()
}

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
public actual fun File.writeText(text: String): Unit = fs.writeFileSync(getAbsolutePath(), text, "utf8")

/**
 * Reads this file as text
 */
public actual fun File.readText(): String = fs.readFileSync(getAbsolutePath(), "utf8")

/**
 * Deletes this file and any subdirectories recursively
 */
public actual fun File.deleteRecursively(): Boolean = runCatching {
  if (isDirectory()) {
    fs.rmdirSync(getPath(), jsObject { recursive = true })
  } else {
    fs.unlinkSync(getPath())
  }
}.isSuccess

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = jsPath.sep

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = require("os").EOL