package dev.petuska.klip

/**
 * Multiplatform wrapper ove java.io.File
 */
actual class File actual constructor(path: String) {
  /**
   * Retrieves parent file
   */
  actual fun getParentFile(): File {
    TODO()
  }

  /**
   * Returns local path to this file
   */
  actual fun getPath(): String {
    TODO("Not yet implemented")
  }

  /**
   * Returns absolute path to this file
   */
  actual fun getAbsolutePath(): String {
    TODO("Not yet implemented")
  }

  /**
   * Recursively makes all directories up to this directory file
   */
  actual fun mkdirs(): Boolean {
    TODO("Not yet implemented")
  }

  /**
   * Checks if the file exsists
   */
  actual fun exists(): Boolean {
    TODO("Not yet implemented")
  }

  /**
   * checks if the file is directory
   */
  actual fun isDirectory(): Boolean {
    TODO("Not yet implemented")
  }
}

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
actual fun File.writeText(text: String) {
}

/**
 * Reads this file as text
 */
actual fun File.readText(): String {
  TODO("Not yet implemented")
}

/**
 * Deletes this file and any subdirectories recursively
 */
actual fun File.deleteRecursively(): Boolean {
  TODO("Not yet implemented")
}
