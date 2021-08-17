package dev.petuska.klip.ext

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTest {
  private val testDir = File("test-dir")

  @AfterTest
  fun cleanUp() {
    testDir.deleteRecursively()
  }

  @Test
  fun getParentFile() {
    val file = File("$testDir/ok.kt")
    assertEquals(file.getParentFile().getPath(), testDir.getPath())
  }

  @Test
  fun getPath() {
    val file = File("$testDir/ok.kt")
    assertEquals(file.getPath(), "$testDir/ok.kt")
  }

  @Test
  fun getAbsolutePath() {
    val file = File("$testDir/ok.kt")
    file.mkdirs()
    assertEquals(file.getAbsolutePath(), "${file.getParentFile().getAbsolutePath()}/ok.kt")
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

  @Test
  fun deleteRecursively() {
    val file = File(testDir.getPath())
    assertFalse(file.exists())
    file.mkdirs()
    assertTrue(file.exists())
    file.deleteRecursively()
    assertFalse(file.exists())
  }
}
