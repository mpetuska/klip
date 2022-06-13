package dev.petuska.klip.plugin.util

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import java.io.File
import kotlin.system.exitProcess

class KlipLogger(private val collector: MessageCollector, filePath: String?) : org.jetbrains.kotlin.util.Logger {
  private val file = filePath?.let(::File)?.apply {
    writeText("PWD: ${File(".").canonicalPath}\n")
  }

  private fun logToFile(text: Any?) {
    file?.appendText("${text}\n")
  }

  operator fun invoke(message: () -> Any?) {
    logToFile(message())
  }

  override fun error(message: String) {
    logToFile(message)
    collector.report(CompilerMessageSeverity.ERROR, message)
  }

  override fun fatal(message: String): Nothing {
    logToFile(message)
    collector.report(CompilerMessageSeverity.ERROR, message)
    exitProcess(1)
  }

  override fun warning(message: String) {
    logToFile(message)
    collector.report(CompilerMessageSeverity.WARNING, message)
  }

  override fun log(message: String) {
    logToFile(message)
    collector.report(CompilerMessageSeverity.LOGGING, message)
  }
}
