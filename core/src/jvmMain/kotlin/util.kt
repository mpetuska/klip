package dev.petuska.klip

internal actual inline fun <reified T : Any> T.buildKlipPath(): String {
    return this::class.qualifiedName!!.replace(".", "/")
}