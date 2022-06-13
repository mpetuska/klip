package dev.petuska.klip.core

@KlipCompilerAPI
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
  message = "This API is reserved for internal use by the compiler plugin. " +
    "Look for a similar API without KlipContext parameter.",
  level = RequiresOptIn.Level.ERROR
)
public annotation class KlipCompilerAPI
