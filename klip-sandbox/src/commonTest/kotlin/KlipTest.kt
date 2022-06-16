package sandbox.test

import dev.petuska.klip.assertions.assertKlip
import dev.petuska.klip.assertions.assertMatchesKlip
import dev.petuska.klip.runner.runTest
import kotlin.test.Test

abstract class KlipTest {
  @Test
  fun withoutReceiver() = runTest {
    assertMatchesKlip("zero")
    assertMatchesKlip("one")
  }

  @Test
  fun withReceiver() = runTest {
    0.assertKlip()
    1.assertKlip()
  }
}
