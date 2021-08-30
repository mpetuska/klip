package dev.petuska.klip.ext

import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.getcwd
import platform.posix.mkdir

/**
 * Native file separator for the platform (thanks a bunch, windows...)
 */
public actual val File.separator: String get() = "/"

/**
 * Native newline separator for the platform (thanks a bunch, windows...)
 */
public actual val File.newline: String get() = "\n"

internal actual fun mppMkdir(path: String, permissions: Int): Int = mkdir(path, permissions.convert())

internal actual fun File.mppGetAbsolutePath(): String {
  val path = getPath()
  return if (path.startsWith("/")) {
    path
  } else {
    memScoped {
      "${getcwd(alloc(), 0)!!.toKString()}$separator$path"
    }
  }
}
