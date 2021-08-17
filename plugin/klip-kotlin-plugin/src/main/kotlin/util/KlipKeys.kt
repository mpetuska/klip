package dev.petuska.klip.plugin.util

import dev.petuska.klip.plugin.KlipOption
import org.jetbrains.kotlin.config.CompilerConfigurationKey

private inline fun <reified T> KlipOption<T>.key(): CompilerConfigurationKey<T> = CompilerConfigurationKey(name)

internal object KlipKeys {
  val KEY_ENABLED = KlipOption.Enabled.key()
  val KEY_ROOT = KlipOption.Root.key()
  val KEY_UPDATE = KlipOption.Update.key()
  val KEY_FUNCTION = KlipOption.Function.key()
}
