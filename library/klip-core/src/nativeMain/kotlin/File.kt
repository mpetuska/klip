package dev.petuska.klip.core.ext

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import platform.posix.EOF
import platform.posix.FTW_DEPTH
import platform.posix.F_OK
import platform.posix.S_IFDIR
import platform.posix.S_IFMT
import platform.posix.S_IRWXU
import platform.posix.access
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.fputs
import platform.posix.getcwd
import platform.posix.nftw
import platform.posix.perror
import platform.posix.remove
import platform.posix.rmdir
import platform.posix.stat

/**
 * Multiplatform wrapper over java.io.File
 */
public actual class File actual constructor(path: String) {
  private val _path: String

  init {
    require(path.isNotEmpty()) { "Path cannot be empty" }
    this._path = cleanupPath(path)
  }

  /**
   * Retrieves parent file
   */
  public actual fun getParentFile(): File? {
    val pPath = _path.removeSuffix(separator)
      .let { it.removeSuffix(it.substringAfterLast(separator)) }
      .removeSuffix(separator)
    return if (pPath.isEmpty()) {
      null
    } else {
      File(pPath)
    }
  }

  /**
   * Returns local path to this file
   */
  public actual fun getPath(): String = _path

  /**
   * Returns absolute path to this file
   */
  public actual fun getAbsolutePath(): String {
    return if (isRooted) {
      _path
    } else {
      memScoped {
        "${getcwd(null, 0)!!.toKString()}$separator$_path"
      }
    }
  }

  /**
   * Recursively makes all directories up to this directory file
   */
  public actual fun mkdirs(): Boolean {
    val parent = getParentFile()
    if (parent != null && !parent.exists() && parent._path != separator && parent._path.isNotEmpty()) {
      parent.mkdirs()
    }

    return mppMkdir(_path, S_IRWXU) == 0
  }

  /**
   * Checks if the file exists
   */
  public actual fun exists(): Boolean = access(_path, F_OK) == 0

  /**
   * checks if the file is directory
   */
  public actual fun isDirectory(): Boolean = memScoped {
    val stat = alloc<stat>()
    if (stat(_path, stat.ptr) != 0) {
      false
    } else {
      S_IFDIR == (stat.st_mode and S_IFMT.convert()).convert<Int>()
    }
  }

  actual override fun toString(): String = getPath()
}

/**
 * Writes text to file creating it if needed and fully overwriting any previous content
 */
public actual fun File.writeText(text: String) {
  val file = fopen(getPath(), "w") ?: error("Cannot open output file ${getPath()}")
  try {
    memScoped {
      if (fputs(text, file) == EOF) {
        error("File write error")
      }
    }
  } finally {
    fclose(file)
  }
}

/**
 * Reads this file as text
 */
public actual fun File.readText(): String {
  val returnBuffer = StringBuilder()
  val file = fopen(getPath(), "r") ?: error("Cannot open input file ${getPath()}")

  try {
    memScoped {
      val readBufferLength = 64 * 1024
      val buffer = allocArray<ByteVar>(readBufferLength)
      var line = fgets(buffer, readBufferLength, file)?.toKString()
      while (line != null) {
        returnBuffer.append(line)
        line = fgets(buffer, readBufferLength, file)?.toKString()
      }
    }
  } finally {
    fclose(file)
  }

  return returnBuffer.toString()
}

/**
 * Deletes this file and any subdirectories recursively
 */
public actual fun File.deleteRecursively(): Boolean {
  return if (isDirectory()) {
    nftw(
      getPath(),
      staticCFunction { fpath, _, _, _ ->
        val spath = fpath?.toKString()
        val isDir = memScoped {
          val stat = alloc<stat>()
          if (stat(spath, stat.ptr) != 0) {
            false
          } else {
            S_IFDIR == (stat.st_mode and S_IFMT.convert()).convert<Int>()
          }
        }
        if (isDir) {
          rmdir(spath).also {
            if (it != 0) {
              perror("Directory removal error[$it]: $spath")
            }
          }
        } else {
          remove(spath).also {
            if (it != 0) {
              perror("File removal error[$it]: $spath")
            }
          }
        }
      },
      64, FTW_DEPTH
    )
  } else {
    remove(getPath())
  } == 0
}

internal expect fun mppMkdir(path: String, permissions: Int): Int

/**
 * Checks if file path is starting from root (thanks a bunch, windows...)
 */
internal expect val File.isRooted: Boolean
