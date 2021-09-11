package dev.petuska.klip.core.ext

/**
 * Multiplatform wrapper over java.io.File API
 */
public expect class File(path: String) {
  /**
   * Retrieves parent file
   */
  public fun getParentFile(): File

  /**
   * Returns local path to this file
   */
  public fun getPath(): String

  /**
   * Returns absolute path to this file
   */
  public fun getAbsolutePath(): String

  /**
   * Recursively makes all directories up to this directory file
   */
  public fun mkdirs(): Boolean

  /**
   * Checks if the file exists
   */
  public fun exists(): Boolean

  /**
   * Checks if the file is directory
   */
  public fun isDirectory(): Boolean

  override fun toString(): String
}

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public expect val File.separator: String

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public expect val File.newline: String

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
public expect fun File.writeText(text: String)

/**
 * Reads this file as text
 */
public expect fun File.readText(): String

/**
 * Deletes this file and any subdirectories recursively
 */
public expect fun File.deleteRecursively(): Boolean
