package dev.petuska.klip.core.domain

import dev.petuska.klip.core.stub

public actual inline fun <reified T> Klip.typed(): TypedKlip<T> = stub
public actual inline fun <reified T> TypedKlip<T>.raw(): Klip = stub
