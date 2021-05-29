package dev.petuska.klip

/**
 * An annotation used to mark test classes in order to generate an appropriate [KlipManager] and assertion functions.
 */
@Target(AnnotationTarget.CLASS)
annotation class Klippable
