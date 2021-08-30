package dev.petuska.klip.ext

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
    assertEquals(file.getParentFile().getPath(), testDir.getPath())
  }

  @Test
  fun getPath() {
    val file = File("$testDir/ok.kt")
    assertEquals(file.getPath(), "$testDir${testDir.separator}ok.kt")
  }

  @Test
  fun getAbsolutePath() {
    val file = File("$testDir/ok.kt")
    file.mkdirs()
    assertEquals(file.getAbsolutePath(), "${file.getParentFile().getAbsolutePath()}${testDir.separator}ok.kt")
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
