package subpackage

import dev.petuska.klip.assertKlip
import dev.petuska.klip.assertMatchesKlip
import kotlin.test.Test

class TestTest {
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
