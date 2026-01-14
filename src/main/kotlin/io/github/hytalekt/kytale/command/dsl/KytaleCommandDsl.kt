package io.github.hytalekt.kytale.command.dsl

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
)
annotation class KytaleCommandDsl
