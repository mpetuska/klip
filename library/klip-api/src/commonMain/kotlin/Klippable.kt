package dev.petuska.klip.api

/**
 * Annotation to mark functions to be picked up by the compiler plugin.
 * If marked function contains optionally nullable [KlipContext] type argument, it will be injected by the compiler.
 * Otherwise, an additional [KlipContext] parameter will be prepended at the very start of the call arguments list.
 *
 * ```
 *   @Klippable
 *   fun klippable(..., _context: KlipContext? = null)
 * ```
 * @see [KlipContext.validate]
 * @see [KlipContext]
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
public annotation class Klippable

public inline val KlippableStub: Nothing
  get() = throw NotImplementedError("Invoked stubbed klip API. Did the compiler plugin run?")
