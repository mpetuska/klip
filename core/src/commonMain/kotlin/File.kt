package dev.petuska.klip

expect class File(path: String) {
  fun getParentFile(): File
  fun getPath(): String
  fun getAbsolutePath(): String
  fun mkdirs(): Boolean
  fun exists(): Boolean
  fun isDirectory(): Boolean
}

expect fun File.writeText(text: String)
expect fun File.readText(): String
expect fun File.deleteRecursively(): Boolean
