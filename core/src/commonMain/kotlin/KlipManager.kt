package dev.petuska.klip


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
        const val UPDATE_ENV_VAR_NAME = "KLIP_UPDATE"
        private val updateMode by lazy {
            Environment[UPDATE_ENV_VAR_NAME].equals("true", true)
        }
    }
}