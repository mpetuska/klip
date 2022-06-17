package dev.petuska.klip.api

/**
 * A container to pass in compiler-plugin data
 *
 * @param path file path to retrieve and save klip for the given context
 * @param key key to write the klip under in the file at [path]
 * @param update whether the klip should be overridden instead of asserted
 * @param serverUrl to query for klips
 */
@Suppress("OutdatedDocumentation")
public data class KlipContext(
  val path: String,
  val key: String,
  val update: Boolean,
  val serverUrl: String,
)
