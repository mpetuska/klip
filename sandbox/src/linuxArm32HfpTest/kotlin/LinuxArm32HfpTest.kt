import dev.petuska.klip.assertion.assertKlip
import dev.petuska.klip.assertion.assertMatchesKlip
import kotlin.test.Test

class LinuxArm32HfpTest {
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
