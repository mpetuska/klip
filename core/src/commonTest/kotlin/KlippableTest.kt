package dev.petuska.klip

import kotlin.test.Test

class KlippableTest: Klippable {
    @Test
    fun test() {
        writeKlip("ABC")
    }
}