package dev.petuska.klip.ext

/**
 * Multiplatform wrapper over java.io.File
 */
public actual class File actual constructor(private val path: String) {
  init {
    require(path.isNotEmpty()) { "Path cannot be empty" }
  }

  /**
   * Retrieves parent file
   */
  public actual fun getParentFile(): File = TODO()

  /**
   * Returns local path to this file
   */
  public actual fun getPath(): String = TODO()

  /**
   * Returns absolute path to this file
   */
  public actual fun getAbsolutePath(): String = TODO()

  /**
   * Recursively makes all directories up to this directory file
   */
  public actual fun mkdirs(): Boolean = TODO()

  /**
   * Checks if the file exsists
   */
  public actual fun exists(): Boolean = TODO()

  /**
   * checks if the file is directory
   */
  public actual fun isDirectory(): Boolean = TODO()

  actual override fun toString(): String = TODO()
}

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
public actual fun File.writeText(text: String): Unit = TODO()

/**
 * Reads this file as text
 */
public actual fun File.readText(): String = TODO()

/**
 * Deletes this file and any subdirectories recursively
 */
public actual fun File.deleteRecursively(): Boolean = TODO()

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "/"

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = "\n"
