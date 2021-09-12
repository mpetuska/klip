package sandbox.test

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import kotlin.test.Test

class AndroidNativeArm32Test {
  @Test
  fun test() {
    assertMatchesKlip("zero")
    assertMatchesKlip("one")
  }

  @Test
  fun test1() {
    "2".assertKlip()
  }
}