package subpackage2

import dev.petuska.klip.assertKlip
import kotlin.test.Test

class TestTest2 {
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

class TestTest3 {
  class Inner {
    fun run() {
      assertKlip("2")
    }
  }

  @Test
  fun test() {
    assertKlip("zero")
    assertKlip("one")
  }

  @Test
  fun test1() {
    Inner().run()
  }
}
