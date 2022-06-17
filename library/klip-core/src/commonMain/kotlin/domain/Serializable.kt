package dev.petuska.klip.core.domain

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
public expect annotation class Serializable()

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
public expect annotation class Contextual()

@Target(AnnotationTarget.PROPERTY)
public expect annotation class Transient()
