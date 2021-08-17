package subpackage2

import dev.petuska.klip.assertKlip
import kotlin.test.Test

class TestTest2 {
  @Test
  fun test() {
    assertKlip("zero", "hah")
    assertKlip("one")
  }

  @Test
  fun test1() {
    assertKlip("2", "two")
  }
}

class TestTest3 {
  class Inner {
    fun run() {
      assertKlip("2", "two")
    }
  }

  @Test
  fun test() {
    assertKlip("zero", "hah")
    assertKlip("one")
  }

  @Test
  fun test1() {
    Inner().run()
  }
}
