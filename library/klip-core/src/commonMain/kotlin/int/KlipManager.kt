package dev.petuska.klip.core.int

import dev.petuska.klip.core.ext.File
import dev.petuska.klip.core.ext.readText
import dev.petuska.klip.core.ext.writeText
import kotlin.native.concurrent.ThreadLocal

public typealias Klips = MutableMap<String, Klip>

public data class Klip(
  val value: String,
  val attributes: Map<String, String>,
)

/**
 * A helper class to assist in reading and writing persisted klips
 */
@ThreadLocal
public object KlipManager {
  private const val SEPARATOR = ":::::>>"
  private const val SEPARATOR_ATTR = "="
  private const val SEPARATOR_ATTRS = ";"
  private const val SEPARATOR_KEY = "$SEPARATOR\n"
  private const val SEPARATOR_KLIPS = "\n$SEPARATOR"
  private const val SEPARATOR_SOF = "$SEPARATOR KLIPS $SEPARATOR"
  private const val SEPARATOR_EOF = "\n$SEPARATOR$SEPARATOR$SEPARATOR\n"
  private val klipMatrix = mutableMapOf<String, Klips>()

  private fun loadKlips(path: String): Klips = klipMatrix[path] ?: run {
    val klips = read(path)
      ?.replace("\r\n", "\n")
      ?.removePrefix(SEPARATOR_SOF)
      ?.removeSuffix(SEPARATOR_EOF)
      ?.split(SEPARATOR_KLIPS)
      ?.filter(String::isNotEmpty)
      ?.associate { kl ->
        val split = kl.split(SEPARATOR_KEY)
        require(split.size == 2 && !split[0].startsWith("\n")) {
          "Corrupted klip at $path:${split.getOrNull(0)?.substringBefore(SEPARATOR)}"
        }
        val (k, v) = split
        val (attr, key) = k.split(SEPARATOR).let { if (it.size > 1) it[0] to it[1] else "" to it[0] }
        val attributes = attr.takeIf(String::isNotBlank)
          ?.split(SEPARATOR_ATTRS)
          ?.associate { it.split(SEPARATOR_ATTR).let { (k, v) -> k to v } }
          ?: mapOf()
        key to Klip(v, attributes)
      }?.toMutableMap() ?: mutableMapOf()
    klips.also {
      klipMatrix[path] = it
    }
  }

  private fun saveKlips(path: String, klips: Klips) {
    klipMatrix[path]?.putAll(klips) ?: run { klipMatrix[path] = klips }
    write(path) {
      (klipMatrix[path] ?: mutableMapOf()).entries.joinToString(
        separator = "",
        prefix = SEPARATOR_SOF,
        postfix = SEPARATOR_EOF
      ) { (k, v) ->
        stringifyKlip(k, v.attributes) { v.value }
      }
    }
  }

  private fun write(path: String, source: () -> String): String {
    return File(path).run {
      getParentFile()?.mkdirs()
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
   * @param attributes klip attributes
   * @return klip-writable form of the value provided by [source] that's safe to append to klip file
   */
  public fun stringifyKlip(key: String, attributes: Map<String, String>, source: () -> String): String {
    return SEPARATOR_KLIPS +
      attributes.entries.joinToString { (k, v) -> "$k=$v" } +
      SEPARATOR +
      key +
      SEPARATOR_KEY +
      source()
  }

  /**
   * Writes and returns a klip provided by [source] to a klip file specified by [path] under given [key].
   *
   * @param path file path to retrieve and save klip for the given context
   * @param key key to write the klip under in the file at [path]
   * @param attributes klip attributes
   * @param source a provider for a string representation to persist
   * @return value provided by [source]
   */
  public fun writeKlip(path: String, key: String, attributes: Map<String, String>, source: () -> String): Klip {
    val klips = loadKlips(path)
    return Klip(source(), attributes).also { saveKlips(path, (klips + mutableMapOf(key to it)).toMutableMap()) }
  }

  /**
   * Reads a klip from a klip file specified by [path] under given [key].
   * If such klip is not found, a value provided by [source] is persisted and returned instead.
   *
   * @param path file path to retrieve and save klip for the given context
   * @param key key to read or write the klip under in the file at [path]
   * @param attributes klip attributes
   * @param source a provider for a string representation to persist
   * @return read klip if it exists or persisted value that was provided by [source] otherwise
   */
  public fun readKlip(path: String, key: String, attributes: Map<String, String>, source: () -> String): Klip {
    val klips = loadKlips(path)
    val klip = klips[key]
    return when {
      klip != null -> klip
      else -> writeKlip(path, key, attributes, source)
    }
  }

  /**
   * Writes or returns klip depending on the working mode.
   * @param context [KlipContext] containing metadata about the klip
   * @param source a provider for a string representation to persist
   * @param attributes klip attributes
   * @return read klip if it exists and [KlipContext.update]` == false`
   * or persisted value that was provided by [source] otherwise
   */
  public fun klip(context: KlipContext, attributes: Map<String, String>, source: () -> String): Klip = with(context) {
    return if (update) {
      writeKlip(path, key, attributes, source)
    } else {
      readKlip(path, key, attributes, source)
    }
  }
}
