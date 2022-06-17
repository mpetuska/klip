package dev.petuska.klip.plugin.test

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import dev.petuska.klip.plugin.KlipCommandLineProcessor
import dev.petuska.klip.plugin.KlipComponentRegistrar
import dev.petuska.klip.plugin.config.GROUP
import dev.petuska.klip.plugin.config.NAME
import dev.petuska.klip.plugin.util.KlipOption
import java.io.File

object Compiler {
  val kotlinRoot = File("build/tmp/src/commonMain/kotlin")
  const val enabled = "true"
  const val update = "false"
  val klipAnnotations = listOf("test.Klippable", "test.CustomKlippable")
  val scopeAnnotations = listOf("test.Test", "test.CustomTest")

  private val annotationsFile =
    SourceFile.kotlin(
      "Klippable.kt",
      """
    package test
    annotation class Klippable
    annotation class CustomKlippable 
    annotation class Test
    annotation class CustomTest
      """.trimIndent()
    )

  fun compile(
    vararg sourceFiles: SourceFile,
  ): KotlinCompilation.Result {
    val pluginId = "$GROUP.$NAME-kotlin-plugin"
    return KotlinCompilation()
      .apply {
        sources = sourceFiles.toList() + annotationsFile
        useIR = true
        compilerPlugins = listOf(KlipComponentRegistrar())
        commandLineProcessors = listOf(KlipCommandLineProcessor())
        inheritClassPath = true
        workingDir = kotlinRoot
        pluginOptions = listOf(
          PluginOption(pluginId, KlipOption.Enabled.name, enabled),
          PluginOption(pluginId, KlipOption.Update.name, update),
          *klipAnnotations
            .map { PluginOption(pluginId, KlipOption.KlipAnnotation.name, it) }
            .toTypedArray(),
          *scopeAnnotations
            .map { PluginOption(pluginId, KlipOption.ScopeAnnotation.name, it) }
            .toTypedArray(),
        )
      }.compile()
  }
}
