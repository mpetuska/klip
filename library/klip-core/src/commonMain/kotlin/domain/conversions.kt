package dev.petuska.klip.core.domain

public expect inline fun <reified T> Klip.typed(): TypedKlip<T>
public expect inline fun <reified T> TypedKlip<T>.raw(): Klip
