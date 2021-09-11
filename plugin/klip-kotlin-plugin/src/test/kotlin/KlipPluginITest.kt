package dev.petuska.klip.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import dev.petuska.klip.plugin.test.Compiler
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.test.assertEquals

class KlipPluginITest {
  @Test
  fun works() {
    val result = Compiler.compile(
      SourceFile.kotlin(
        "main.kt",
        """     
        import dev.petuska.klip.core.int.KlipContext
        
        class Main {
          @${Compiler.klipAnnotations[0]}
          fun klip(value: Any?, _context: KlipContext? = null): String = with(_context ?: error("No KlipContext")) {
            return listOf(value, path, key, update).joinToString()
          }          
          
          @${Compiler.klipAnnotations[1]}
          fun Any?.klip2(_context: KlipContext? = null): String = with(_context ?: error("No KlipContext")) {
            return listOf(this@klip2, path, key, update).joinToString()
          }
          
          @${Compiler.scopeAnnotations[0]}
          fun testKlip(value: Any?) = klip(value)
        
          @${Compiler.scopeAnnotations[1]}
          fun Any?.testKlip2() = klip2()
        }
        """.trimIndent()
      )
    )
    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    val kClazz = result.classLoader.loadClass("Main")
    val instance = kClazz.getConstructor().newInstance()
    val testKlip = kClazz.kotlin.memberFunctions.find { it.name == "testKlip" }!!
    val testKlip2 = kClazz.kotlin.memberExtensionFunctions.find { it.name == "testKlip2" }!!
    val klipPath = File("${Compiler.kotlinRoot}/sources/__klips__/main.kt.klip").canonicalPath
    assertEquals(
      "Argument, $klipPath, Main.testKlip#0, false",
      testKlip.call(instance, "Argument")
    )
    assertEquals(
      "Receiver, $klipPath, Main.testKlip2#0, false",
      testKlip2.call(instance, "Receiver")
    )
  }
}
