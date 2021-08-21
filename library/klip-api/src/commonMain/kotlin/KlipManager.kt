package dev.petuska.klip

import dev.petuska.klip.ext.File
import dev.petuska.klip.ext.readText
import dev.petuska.klip.ext.writeText
import kotlin.native.concurrent.ThreadLocal

public typealias Klips = MutableMap<String, String>

/**
 * A helper class to assist in reading and writing persisted klips
 */
@ThreadLocal
public object KlipManager {
  private const val SEPARATOR = ":::::>>"
  private const val SEPARATOR_KEY = "$SEPARATOR\n"
  private const val SEPARATOR_KLIPS = "\n$SEPARATOR"
  private val klipMatrix = mutableMapOf<String, Klips>()

  private fun loadKlips(path: String) = klipMatrix[path] ?: run {
    val klips = read(path)?.split(SEPARATOR_KLIPS)?.filter(String::isNotEmpty)?.associate { kl ->
      val split = kl.split(SEPARATOR_KEY)
      require(split.size == 2 && !split[0].startsWith("\n")) {
        "Corrupted klip at $path:${
        split.getOrNull(0)?.substringAfter(SEPARATOR)
        }"
      }
      val (k, v) = split
      k to v
    }?.toMutableMap() ?: mutableMapOf()
    klips.also {
      klipMatrix[path] = it
    }
  }

  private fun saveKlips(path: String, klips: Klips) {
    klipMatrix[path]?.putAll(klips) ?: run { klipMatrix[path] = klips }
    write(path) {
      (klipMatrix[path] ?: mutableMapOf()).entries.joinToString(separator = "") { (k, v) -> stringifyKlip(k) { v } }
    }
  }

  private fun write(path: String, source: () -> String): String {
    return File(path).run {
      getParentFile().mkdirs()
      source().also { writeText(it) }
    }
  }

  private fun read(path: String): String? {
    val file = File(path)
    return if (file.exists()) {
      file.readText()
    } else {
      null
    }
  }

  /**
   * Converts a [key] and a klip provided by [source] to a writable form that's safe to append to klip file.
   * @param key a key to identify the klip within the file
   * @return klip-writable form of the value provided by [source] that's safe to append to klip file
   */
  public fun stringifyKlip(key: String, source: () -> String): String {
    return "$SEPARATOR_KLIPS$key$SEPARATOR_KEY${source()}"
  }

  /**
   * Writes and returns a klip provided by [source] to a klip file specified by [path] under given [key].
   *
   * @param path file path to retrieve and save klip for the given context
   * @param key key to write the klip under in the file at [path]
   * @param source a provider for a string representation to persist
   * @return value provided by [source]
   */
  public fun writeKlip(path: String, key: String, source: () -> String): String {
    val klips = loadKlips(path)
    return source().also { saveKlips(path, (klips + mutableMapOf(key to it)).toMutableMap()) }
  }

  /**
   * Reads a klip from a klip file specified by [path] under given [key].
   * If such klip is not found, a value provided by [source] is persisted and returned instead.
   *
   * @param path file path to retrieve and save klip for the given context
   * @param key key to read or write the klip under in the file at [path]
   * @param source a provider for a string representation to persist
   * @return read klip if it exists or persisted value that was provided by [source] otherwise
   */
  public fun readKlip(path: String, key: String, source: () -> String): String {
    val klips = loadKlips(path)
    val klip = klips[key]
    return when {
      klip != null -> klip
      else -> writeKlip(path, key, source)
    }
  }

  /**
   * Writes or returns klip depending on the working mode.
   * @param update whether the klip should be overridden instead of asserted
   * @param path file path to retrieve and save klip for the given context
   * @param key key to read or write the klip under in the file at [path]
   * @param source a provider for a string representation to persist
   * @return read klip if it exists and [update]` == false` or persisted value that was provided by [source] otherwise
   */
  public fun klip(update: Boolean, path: String, key: String, source: () -> String): String {
    return if (update) {
      writeKlip(path, key, source)
    } else {
      readKlip(path, key, source)
    }
  }
}