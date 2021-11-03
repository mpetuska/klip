package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.ExpectSpec

class JvmExpectSpec : ExpectSpec({
  context("context") {
    expect("test one") {
      assertMatchesKlip("kotest zero")
      assertMatchesKlip("kotest one")
    }
    expect("test two") {
      "kotest 2".assertKlip()
    }
  }
})
