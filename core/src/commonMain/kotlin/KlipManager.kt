package dev.petuska.klip

/**
 * Klip manager for a given [klipRoot]. Writes and reads all klips under [klipRoot].
 */
@Suppress("unused")
class KlipManager(
  private val klipRoot: String,
) {
  init {
    File(klipRoot).mkdirs()
  }

  private fun path(id: String) = "$klipRoot/$id.klip"

  private fun write(id: String, source: String) {
    File(path(id)).writeText(source)
  }

  private fun read(id: String, source: () -> String): String {
    val file = File(path(id))
    return if (file.exists()) {
      file.readText()
    } else {
      source().also {
        write(id, it)
      }
    }
  }

  /**
   * Writes or returns klip depending on the working mode. [id] is converted to string using [String::toString] method
   */
  fun klip(id: Any, source: () -> String): String {
    val file = File(path("$id"))
    return if (updateMode) {
      source().also {
        file.writeText(it)
      }
    } else {
      if (file.exists()) {
        file.readText()
      } else {
        source().also {
          file.writeText(it)
        }
      }
    }
  }

  companion object {
    /**
     * The name of the environment variable that's used to set the working mode
     */
    const val UPDATE_ENV_VAR_NAME = "KLIP_UPDATE"

    private val updateMode by lazy {
      Environment[UPDATE_ENV_VAR_NAME].equals("true", true)
    }
  }
}
