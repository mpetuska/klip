package subpackage

import dev.petuska.klip.assertKlip
import kotlin.test.Test

class TestTest {
  @Test
  fun test() {
    assertKlip("zero")
    assertKlip("one")
  }

  @Test
  fun test1() {
    assertKlip("2")
  }
}
