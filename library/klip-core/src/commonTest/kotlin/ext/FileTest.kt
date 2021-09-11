package dev.petuska.klip.core.ext

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTest {
  private val testDir = File("build/test-dir/FileTest")

  @BeforeTest
  fun cleanUp() {
    testDir.deleteRecursively()
  }

  @Test
  fun deleteRecursively() {
    val file = File(testDir.getPath())
    assertFalse(file.exists())
    file.mkdirs()
    assertTrue(file.exists())
    require(file.deleteRecursively()) { "Error during recursive deletion" }
    assertFalse(file.exists())
  }

  @Test
  fun getParentFile() {
    val file = File("$testDir/ok.kt")
    assertEquals(testDir.getPath(), file.getParentFile().getPath())
  }

  @Test
  fun getPath() {
    val file = File("$testDir/ok.kt")
    assertEquals("$testDir${testDir.separator}ok.kt", file.getPath())
  }

  @Test
  fun getAbsolutePath() {
    val file = File("$testDir/ok.kt")
    file.mkdirs()
    assertEquals("${file.getParentFile().getAbsolutePath()}${testDir.separator}ok.kt", file.getAbsolutePath())
  }

  @Test
  fun mkdirs() {
    val file = File("$testDir/subdir")
    assertTrue(file.mkdirs())
    assertTrue(file.exists())
  }

  @Test
  fun exists() {
    val file = File(testDir.getPath())
    assertFalse(file.exists())
    file.mkdirs()
    assertTrue(file.exists())
  }
}
