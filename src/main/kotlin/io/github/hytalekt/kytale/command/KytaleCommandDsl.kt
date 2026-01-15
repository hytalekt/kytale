package io.github.hytalekt.kytale.command

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class KytaleCommandDsl

@KytaleCommandDsl
fun command(
    name: String,
    description: String,
    block: KytaleCommandBuilder.() -> Unit,
): KytaleCommand = KytaleCommandBuilder(KytaleCommand(name, description)).inner
