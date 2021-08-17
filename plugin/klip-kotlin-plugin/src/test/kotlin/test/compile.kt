package dev.petuska.klip.plugin.test

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import dev.petuska.klip.plugin.KlipCommandLineProcessor
import dev.petuska.klip.plugin.KlipComponentRegistrar
import dev.petuska.klip.plugin.KlipMap
import dev.petuska.klip.plugin.KlipOption
import java.io.File

fun compile(
  vararg sourceFiles: SourceFile,
): KotlinCompilation.Result {
  return KotlinCompilation().apply {
    sources = sourceFiles.toList()
    useIR = true
    compilerPlugins = listOf(KlipComponentRegistrar())
    commandLineProcessors = listOf(KlipCommandLineProcessor())
    inheritClassPath = true
    workingDir = File("build/tmp/src/commonMain/kotlin")
    pluginOptions = listOf(
      PluginOption(KlipMap.kotlinPluginId, KlipOption.Enabled.name, "true"),
      PluginOption(KlipMap.kotlinPluginId, KlipOption.Update.name, "false"),
      PluginOption(KlipMap.kotlinPluginId, KlipOption.Root.name, workingDir.path),
      PluginOption(KlipMap.kotlinPluginId, KlipOption.KlipAnnotation.name, "test.Klippable"),
      PluginOption(KlipMap.kotlinPluginId, KlipOption.KlipAnnotation.name, "test.CustomKlippable"),
    )
  }.compile()
}
