package sandbox.test.kotest

import dev.petuska.klip.assertions.assertKlip
import dev.petuska.klip.assertions.assertMatchesKlip
import io.kotest.core.spec.style.FeatureSpec

class JvmFeatureSpec : FeatureSpec({
  feature("context") {
    scenario("test one") {
      assertMatchesKlip("kotest zero")
      assertMatchesKlip("kotest one")
    }
    scenario("test two") {
      "kotest 2".assertKlip()
    }
  }
})
