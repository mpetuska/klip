package dev.petuska.klip

import __dirname

internal actual inline fun <reified T: Any> T.buildKlipPath(): String {
    return "${__dirname}/${this::class.simpleName}"
}

