package dev.petuska.klip

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.OutputStream

class KlipProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val sourceSetRoot: File,
) : SymbolProcessor {
    private val klipsRoot: File = sourceSetRoot.resolve("klips").absoluteFile

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Klippable::class.qualifiedName!!)
        val classes = symbols.filterIsInstance<KSClassDeclaration>().toList()
        logger.warn("Processing ${classes.size} classes")
        logger.warn("PWD: ${File("").absolutePath}")
        logger.warn("SourceSetRoot: ${sourceSetRoot.absolutePath}")
        logger.warn("klipsRoot: ${klipsRoot.absolutePath}")
        classes.map {
            val packageName = it.packageName.asString()
            val className = it.simpleName.asString()
            val containingFile = it.containingFile!!
            codeGenerator.createNewFile(
                dependencies = Dependencies(true, containingFile),
                packageName = it.packageName.asString(),
                fileName = "${className}Klipper",
                extensionName = "kt"
            ).use { file ->
                if (packageName.isNotBlank()) {
                    file.appendText("package $packageName\n\n")
                }
                file.appendText(
                    """
                    public val $className.klipPath: String
                        get() = "${klipsRoot}${containingFile.filePath.removePrefix(sourceSetRoot.absolutePath)}"
                """.trimIndent()
                )
            }
        }
        return emptyList()
    }

    fun OutputStream.appendText(str: String) {
        this.write(str.toByteArray())
    }
}