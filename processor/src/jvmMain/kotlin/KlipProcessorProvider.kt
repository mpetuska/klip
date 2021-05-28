package dev.petuska.klip

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KlipProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val sourcesRoot = File(environment.options["klip.root"]!!).canonicalPath
        val klipsRoots = environment.options.filterKeys { it.startsWith("klip.root.") }.map { (key, value) ->
            key.removePrefix("klip.root.") to File(value).canonicalPath
        }.toMap()
        return KlipProcessor(
            environment.codeGenerator,
            environment.logger,
            sourcesRoot,
            klipsRoots
        )
    }
}