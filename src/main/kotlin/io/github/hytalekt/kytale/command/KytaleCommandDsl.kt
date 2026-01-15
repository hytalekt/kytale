package io.github.hytalekt.kytale.command

/**
 * DSL marker for the command builder
 *
 * @see command
 * @see KytaleCommandBuilder
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class KytaleCommandDsl

/**
 * Creates a command with the builder
 *
 * @param name The command name
 * @param description The command description
 * @param block The builder configuration
 *
 * @return A configured [KytaleCommand]
 *
 * @see KytaleCommandBuilder
 * @see com.hypixel.hytale.server.core.command.system.AbstractCommand
 */
@KytaleCommandDsl
fun command(
    name: String,
    description: String,
    block: KytaleCommandBuilder.() -> Unit,
): KytaleCommand = KytaleCommandBuilder(KytaleCommand(name, description)).apply(block).command
