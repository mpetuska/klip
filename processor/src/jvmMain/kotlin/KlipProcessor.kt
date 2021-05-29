package dev.petuska.klip

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.OutputStream

class KlipProcessor(
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger,
  private val sourcesRoot: String,
  private val klipRoots: Map<String, String>,
) : SymbolProcessor {

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val symbols = resolver.getSymbolsWithAnnotation(Klippable::class.qualifiedName!!)
    val classes = symbols.filterIsInstance<KSClassDeclaration>().toList()
    classes.map {
      generate(it)
    }
    return emptyList()
  }

  private fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
  }

  private fun generate(classDeclaration: KSClassDeclaration) {
    val containingFile = classDeclaration.containingFile!!
    val containingFilePath = containingFile.filePath
    val klipName = containingFilePath.let {
      it.removePrefix("$sourcesRoot/").split("/")[0]
    }
    val klipRoot = klipRoots[klipName]
    if (klipRoot != null) {
      val packageName = classDeclaration.packageName.asString()
      val className = classDeclaration.simpleName.asString()
      codeGenerator.createNewFile(
        dependencies = Dependencies(true, containingFile),
        packageName = packageName,
        fileName = "${className}Klip",
        extensionName = "kt"
      ).use { file ->
        if (packageName.isNotBlank()) {
          file.appendText("package $packageName\n\n")
        }
        val klipClassRoot = "${klipRoot}${containingFilePath.removePrefix("$sourcesRoot/$klipName/kotlin")}"
        file.appendText(buildStubs(className, klipClassRoot))
      }
    } else {
      logger.warn("Klip root for [$klipName] not found. Please add it via 'klip.root.$klipName' ksp option")
    }
  }

  private fun buildStubs(className: String, klipClassRoot: String): String = """
        private val klipManager = dev.petuska.klip.KlipManager("$klipClassRoot")
        
        ${klip(className)}        
        ${assertKlip(className)}
  """.trimIndent() + "\n"

  private fun klip(className: String) = """
        public fun $className.klip(id: Any, source: () -> String): String = klipManager.klip(id, source)
  """.trimIndent()

  private fun assertKlip(className: String) = """
        public fun <T> $className.assertKlip(id: Any, actual: T) {
            val valueStr = actual.toString()
            kotlin.test.assertEquals(klip(id) { valueStr }, valueStr)
        }
  """.trimIndent()
}
