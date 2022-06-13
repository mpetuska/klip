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

val CI by lazy { "true".equals(System.getenv("CI"), true) }
val SANDBOX by lazy { "true".equals(System.getenv("SANDBOX"), true) }

val Project.isMainHost: Boolean
  get() = HostManager.simpleOsName().equals("${properties["project.mainOS"]}", true)
