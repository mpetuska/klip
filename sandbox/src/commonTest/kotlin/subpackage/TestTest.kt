package subpackage

import dev.petuska.klip.assertKlip
import dev.petuska.klip.assertMatchesKlip
import kotlin.test.Test

class TestTest {
  @Test
  fun test() {
    assertKlip("zero")
    assertKlip("one")
  }

  @Test
  fun test1() {
    "2".assertMatchesKlip()
  }
}
