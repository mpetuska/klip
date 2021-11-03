package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.BehaviorSpec

class JvmBehaviourSpec : BehaviorSpec({
  Given("context") {
    When("sub") {
      Then("test one") {
        assertMatchesKlip("kotest zero")
        assertMatchesKlip("kotest one")
      }
    }

    When("sub2") {
      Then("test two") {
        "kotest 2".assertKlip()
      }
    }
  }
})
