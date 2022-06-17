package dev.petuska.klip.core.domain

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
public actual annotation class Serializable

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
public actual annotation class Contextual

@Target(AnnotationTarget.PROPERTY)
public actual annotation class Transient
