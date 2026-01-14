package io.github.hytalekt.kytale.command.dsl

import io.github.hytalekt.kytale.command.KytaleCommand
import io.github.hytalekt.kytale.command.KytaleCommandBuilder

fun command(
    name: String,
    description: String,
    requiresConfirmation: Boolean = false,
    block: KytaleCommandBuilder.() -> Unit,
): KytaleCommand {
    val builder = KytaleCommandBuilder()
    builder.block()
    // TODO: Implement build
    return KytaleCommand()
}
