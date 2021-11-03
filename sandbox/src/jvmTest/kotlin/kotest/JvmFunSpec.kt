package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
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
