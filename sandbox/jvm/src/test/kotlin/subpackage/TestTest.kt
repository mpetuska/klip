package subpackage

import dev.petuska.klip.Klippable
import org.junit.Test

@Klippable
class TestTest {
  @Test
  fun test() {
    assertKlip("0", "zero")
    assertKlip("1", "one")
  }

  @Test
  fun test1() {
    assertKlip("2", "two")
    assertKlip("3", "three")
  }
}
