package dev.petuska.klip

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KlipProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KlipProcessor(
            environment.codeGenerator,
            environment.logger,
            File("/Users/mpetuska/IdeaProjects/Personal/klip/sandbox/src/jvmMain") // TODO
        )
    }
}