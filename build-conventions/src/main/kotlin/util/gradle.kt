package util

import org.gradle.api.Project
import org.jetbrains.kotlin.konan.target.HostManager
import java.nio.charset.*

object Git {
  val headCommitHash by lazy { execAndCapture("git rev-parse --verify HEAD") }
}

fun execAndCapture(cmd: String): String? {
  val child = Runtime.getRuntime().exec(cmd)
  child.waitFor()
  return if (child.exitValue() == 0) {
    child.inputStream.readAllBytes().toString(Charset.defaultCharset()).trim()
  } else null
}
