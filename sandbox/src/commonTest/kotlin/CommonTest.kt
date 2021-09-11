package sandbox.test

import dev.petuska.klip.api.assertMatchesKlip
import kotlin.test.Test

class CommonTest {
  @Test
  fun test() {
    assertMatchesKlip("zero")
    assertMatchesKlip("one")
  }

  @Test
  fun test1() {
    assertMatchesKlip("2")
  }
}

class AnotherCommonTest {
  class Inner {
    fun run() {
      assertMatchesKlip("2")
    }
  }

  @Test
  fun test() {
    assertMatchesKlip("zero")
    assertMatchesKlip("one")
  }

  @Test
  fun test1() {
    Inner().run()
  }
}
