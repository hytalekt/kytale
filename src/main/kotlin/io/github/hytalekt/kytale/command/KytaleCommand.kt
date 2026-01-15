package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.AbstractCommand
import com.hypixel.hytale.server.core.command.system.CommandContext
import java.util.concurrent.CompletableFuture

class KytaleCommand(
    name: String?,
    description: String,
    var defaultExecutor: KytaleCommandContext.(CommandContext) -> CompletableFuture<Void?>? = { null },
) : AbstractCommand(name, description) {
    override fun execute(context: CommandContext): CompletableFuture<Void?>? = KytaleCommandContext(context).defaultExecutor(context)
}
