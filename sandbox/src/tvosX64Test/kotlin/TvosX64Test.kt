package sandbox.test

import dev.petuska.klip.assertions.assertKlip
import dev.petuska.klip.assertions.assertMatchesKlip
import kotlin.test.Test

class TvosX64Test {
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
