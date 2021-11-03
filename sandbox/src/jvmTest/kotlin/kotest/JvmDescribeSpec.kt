package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.DescribeSpec

class JvmDescribeSpec : DescribeSpec({
  describe("context") {
    it("test one") {
      assertMatchesKlip("kotest zero")
      assertMatchesKlip("kotest one")
    }

    it("test two") {
      "kotest 2".assertKlip()
    }
  }
})
