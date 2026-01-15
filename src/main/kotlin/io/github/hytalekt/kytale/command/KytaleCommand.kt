package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.AbstractCommand
import com.hypixel.hytale.server.core.command.system.CommandContext
import java.util.concurrent.CompletableFuture

/**
 * A command wrapper that provides [KytaleCommandContext] to executors
 *
 * @param name The command name, or null for variants
 * @param description The command description
 * @param defaultExecutor The command executor function
 *
 * @see KytaleCommandContext
 * @see com.hypixel.hytale.server.core.command.system.AbstractCommand
 */
class KytaleCommand internal constructor(
    name: String?,
    description: String,
    var defaultExecutor: KytaleCommandContext.(CommandContext) -> CompletableFuture<Void?>? = { null },
) : AbstractCommand(name, description) {
    override fun execute(context: CommandContext): CompletableFuture<Void?>? = KytaleCommandContext(context).defaultExecutor(context)
}
