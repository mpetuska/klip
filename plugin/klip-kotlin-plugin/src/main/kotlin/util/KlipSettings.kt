package dev.petuska.klip.plugin.util

import org.jetbrains.kotlin.name.FqName

/** Payload class to pass-along command line options */
data class KlipSettings(
  val enabled: Boolean,
  val update: Boolean,
  val serverUrl: String,
  val klipAnnotations: Collection<FqName>,
  val scopeAnnotations: Collection<FqName>,
  val scopeFunctions: Collection<FqName>,
)
