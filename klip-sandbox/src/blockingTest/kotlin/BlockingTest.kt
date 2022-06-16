package sandbox

import dev.petuska.klip.assertions.assertKlipBlocking
import dev.petuska.klip.assertions.assertMatchesKlipBlocking
import kotlin.test.Test

class BlockingTest {
  @Test
  fun withoutReceiver() {
    assertMatchesKlipBlocking("zero")
    assertMatchesKlipBlocking("one")
  }

  @Test
  fun withReceiver() {
    0.assertKlipBlocking()
    1.assertKlipBlocking()
  }
}
