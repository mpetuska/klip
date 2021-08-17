package dev.petuska.klip.plugin

import java.io.File

data class KlipSettings(
  val root: File,
  val update: Boolean,
  val klipAnnotations: Collection<String>,
  val scopeAnnotations: Collection<String>,
)
