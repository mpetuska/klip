package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.WordSpec

class JvmWordSpec : WordSpec({
  "Hello" When {
    "context" should {
      "test one" {
        assertMatchesKlip("kotest zero")
        assertMatchesKlip("kotest one")
      }
      "test two" {
        "kotest 2".assertKlip()
      }
    }
  }
})
