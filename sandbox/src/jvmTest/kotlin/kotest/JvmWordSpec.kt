package sandbox.test.kotest

import dev.petuska.klip.assertions.assertKlip
import dev.petuska.klip.assertions.assertMatchesKlip
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
