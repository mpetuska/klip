package dev.petuska.klip

import dev.petuska.klip.ext.File
import dev.petuska.klip.ext.readText
import dev.petuska.klip.ext.writeText

typealias Klips = MutableMap<String, String>

internal object KlipManager {
  private const val SEPARATOR = ":::::>>"
  private const val SEPARATOR_KEY = "$SEPARATOR\n"
  private const val SEPARATOR_KLIPS = "\n$SEPARATOR"
  private val klipMatrix = mutableMapOf<String, Klips>()

  private fun stringifyKlip(key: String, source: () -> String): String {
    return "$SEPARATOR_KLIPS$key$SEPARATOR_KEY${source()}"
  }

  private fun loadKlips(path: String) = klipMatrix[path] ?: run {
    val klips = read(path)?.split(SEPARATOR_KLIPS)?.filter(String::isNotEmpty)?.associate { kl ->
      val split = kl.split(SEPARATOR_KEY)
      require(split.size == 2 && !split[0].startsWith("\n")) { "Corrupted klip at $path:${split.getOrNull(0)?.substringAfter(SEPARATOR)}" }
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

  private fun writeKlip(path: String, id: String, source: () -> String): String {
    val klips = loadKlips(path)
    return source().also { saveKlips(path, (klips + mutableMapOf(id to it)).toMutableMap()) }
  }

  private fun readKlip(path: String, id: String, source: () -> String): String {
    val klips = loadKlips(path)
    val klip = klips[id]
    return when {
      klip != null -> klip
      else -> writeKlip(path, id, source)
    }
  }

  /**
   * Writes or returns klip depending on the working mode. [id] is converted to string using [String::toString] method
   */
  fun klip(update: Boolean, path: String, id: String, source: () -> String): String {
    return if (update) {
      writeKlip(path, id, source)
    } else {
      readKlip(path, id, source)
    }
  }
}
