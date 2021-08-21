package dev.petuska.klip.ext

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
import platform.posix.mkdir
import platform.posix.nftw
import platform.posix.perror
import platform.posix.realpath
import platform.posix.remove
import platform.posix.stat

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
  public actual fun getParentFile(): File {
    val pPath = path.removeSuffix("/").let { it.removeSuffix(it.substringAfterLast("/")) }.removeSuffix("/")
    return if (pPath.isEmpty() && path != ".") {
      File(".")
    } else {
      File(pPath)
    }
  }

  /**
   * Returns local path to this file
   */
  public actual fun getPath(): String = path

  /**
   * Returns absolute path to this file
   */
  public actual fun getAbsolutePath(): String = memScoped {
    realpath(path, null)?.toKString() ?: error("Cannot determine absolute path for $path")
  }

  /**
   * Recursively makes all directories up to this directory file
   */
  public actual fun mkdirs(): Boolean {
    val parent = getParentFile()
    println("${parent.path} ${parent.exists()}")
    if (!parent.exists() && parent.path != "/" && parent.path.isNotEmpty()) {
      parent.mkdirs()
    }

    return mkdir(path, S_IRWXU) == 0
  }

  /**
   * Checks if the file exsists
   */
  public actual fun exists(): Boolean = access(path, F_OK) == 0

  /**
   * checks if the file is directory
   */
  public actual fun isDirectory(): Boolean = memScoped {
    val stat = alloc<stat>()
    if (stat(path, stat.ptr) != 0) {
      false
    } else {
      S_IFDIR == (stat.st_mode and S_IFMT.convert()).convert<Int>()
    }
  }

  override fun toString(): String = getPath()
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
        remove(fpath?.toKString()).also {
          if (it != 0) {
            perror(fpath?.toKString())
          }
        }
      },
      64, FTW_DEPTH
    )
  } else {
    remove(getPath())
  } == 0
}