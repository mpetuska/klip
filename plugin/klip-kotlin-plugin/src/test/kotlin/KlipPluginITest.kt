package dev.petuska.klip.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import dev.petuska.klip.plugin.test.compile
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.test.assertEquals

class KlipPluginITest {
  @Test
  fun works() {
    val result = compile(
      SourceFile.kotlin(
        "annotations.kt",
        """
        package test
        
        annotation class Klippable
        annotation class CustomKlippable        
        """.trimIndent()
      ),
      SourceFile.kotlin(
        "main.kt",
        """        
        class Main {
          @test.Klippable
          fun klip(value: Any?, path: String? = null, key: String? = null, update: Boolean? = null): String {
            return listOf(value, path, key, update).joinToString()
          }
          
          
          @test.CustomKlippable
          fun Any?.klip2(path: String? = null, key: String? = null, update: Boolean? = null): String {
            return listOf(this, path, key, update).joinToString()
          }
          
          @org.junit.jupiter.api.Test
          fun testKlip(value: Any?) = klip(value)
          @org.junit.jupiter.api.Test
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
    val klipPath = File("build/tmp/src/commonMain/klips/sources/main.kt.klip").canonicalPath
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
