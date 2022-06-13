package sandbox.test.kotest

import dev.petuska.klip.assertions.assertKlip
import dev.petuska.klip.assertions.assertMatchesKlip
import io.kotest.core.spec.style.FunSpec

class JvmFunSpec : FunSpec({
  test("test one") {
    assertMatchesKlip("kotest zero")
    assertMatchesKlip("kotest one")
  }

  test("test two") {
    "kotest 2".assertKlip()
  }
})
