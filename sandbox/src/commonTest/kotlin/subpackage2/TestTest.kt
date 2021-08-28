package subpackage2

import dev.petuska.klip.assert.assertMatchesKlip
import kotlin.test.Test

class TestTest2 {
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

class TestTest3 {
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
