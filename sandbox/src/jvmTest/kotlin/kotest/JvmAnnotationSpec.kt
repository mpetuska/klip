package sandbox.test.kotest

import dev.petuska.klip.api.assertKlip
import dev.petuska.klip.api.assertMatchesKlip
import io.kotest.core.spec.style.AnnotationSpec

class JvmAnnotationSpec : AnnotationSpec() {
  @Test
  fun testOne() {
    assertMatchesKlip("kotest zero")
    assertMatchesKlip("kotest one")
  }

  @Test
  fun testTwo() {
    "kotest 2".assertKlip()
  }
}
