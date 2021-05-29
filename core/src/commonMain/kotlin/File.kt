package dev.petuska.klip

/**
 * Multiplatform wrapper ove java.io.File
 */
expect class File(path: String) {
  /**
   * Retrieves parent file
   */
  fun getParentFile(): File

  /**
   * Returns local path to this file
   */
  fun getPath(): String

  /**
   * Returns absolute path to this file
   */
  fun getAbsolutePath(): String

  /**
   * Recursively makes all directories up to this directory file
   */
  fun mkdirs(): Boolean

  /**
   * Checks if the file exsists
   */
  fun exists(): Boolean

  /**
   * checks if the file is directory
   */
  fun isDirectory(): Boolean
}

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
expect fun File.writeText(text: String)

/**
 * Reads this file as text
 */
expect fun File.readText(): String

/**
 * Deletes this file and any subdirectories recursively
 */
expect fun File.deleteRecursively(): Boolean
