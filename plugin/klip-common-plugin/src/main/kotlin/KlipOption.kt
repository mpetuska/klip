package dev.petuska.klip.plugin

sealed class KlipOption<T>(
  val name: String,
  val valueDescription: String,
  val description: String,
  val default: T,
) {
  object Enabled : KlipOption<Boolean>(
    name = "enabled",
    valueDescription = "<true|false>",
    description = "whether the plugin is enabled",
    default = true,
  )

  object Root : KlipOption<String?>(
    name = "root",
    valueDescription = "<path>",
    description = "path to sources root",
    default = null,
  )

  object Update : KlipOption<Boolean>(
    name = "update",
    valueDescription = "<true|false>",
    description = "whether the klips should be updated",
    default = false,
  )

  object KlipAnnotation : KlipOption<List<String>>(
    name = "annotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register for compiler processing",
    default = listOf("${KlipMap.group}.${KlipMap.name}.Klippable"),
  )

  object ScopeAnnotation : KlipOption<List<String>>(
    name = "annotation",
    valueDescription = "<fully qualified annotation name>",
    description = "annotation to register function scope for compiler klip detection and processing",
    default = listOf(
      "kotlin.Test",
      "org.junit.Test",
      "org.junit.jupiter.api.Test"
    ),
  )
}
