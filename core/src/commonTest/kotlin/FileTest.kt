package dev.petuska.klip

import kotlin.test.*

class FileTest {
    private val testDir = File("test-dir")

    @AfterTest
    fun cleanUp() {
        testDir.deleteRecursively()
    }

    @Test
    fun getParentFile() {
        val file = File("${testDir.getPath()}/ok.kt")
        assertEquals(file.getParentFile().getPath(), testDir.getPath())
    }

    @Test
    fun getPath() {
        val file = File("${testDir.getPath()}/ok.kt")
        assertEquals(file.getPath(), "${testDir.getPath()}/ok.kt")
    }

    @Test
    fun getAbsolutePath() {
        val file = File("${testDir.getPath()}/ok.kt")
        assertEquals(file.getAbsolutePath(), "${file.getParentFile().getAbsolutePath()}/ok.kt")
    }

    @Test
    fun mkdirs() {
        val file = File(testDir.getPath())
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