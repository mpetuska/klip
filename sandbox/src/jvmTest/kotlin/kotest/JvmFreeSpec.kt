package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.FreeSpec

class JvmFreeSpec : FreeSpec({
  "Hello" - {
    "context" - {
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
