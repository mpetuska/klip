package dev.petuska.klip.plugin.util

import java.io.File

private object Debugger {
  private val file by lazy {
    File("${System.getProperty("user.home")}/IdeaProjects/klip/sandbox/klip.log").also {
      it.writeText("PWD: ${File("").canonicalPath}")
    }
  }
  private val enabled = System.getenv("KLIP_DEBUG")?.let { !it.equals("false", true) } == true
  fun log(text: () -> String) {
    if (enabled)
        text().let {
          println("DEBUG: $it")
          file.appendText("${it}\n")
        }
  }
}

fun debug(text: () -> String) {
  Debugger.log(text)
}
