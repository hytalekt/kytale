package io.github.hytalekt.kytale.coroutines

import com.hypixel.hytale.server.core.command.system.CommandContext
import io.github.hytalekt.kytale.command.KytaleCommandBuilder
import io.github.hytalekt.kytale.command.KytaleCommandContext
import io.github.hytalekt.kytale.command.KytaleCommandDsl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture

/**
 * Sets a coroutine-based executor for the command
 *
 * Allows using suspending functions as command executors by running them in a coroutine scope
 * and converting the result to a CompletableFuture.
 *
 * @param scope The coroutine scope to run in, defaults to Dispatchers.Default
 * @param block The suspending executor block
 *
 * @see KytaleCommandBuilder.executorAsync
 */
@KytaleCommandDsl
inline fun KytaleCommandBuilder.executorCoroutine(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    crossinline block: suspend KytaleCommandContext.(CommandContext) -> Unit,
) {
    executorAsync { context ->
        scope
            .async {
                block(context)
                null
            }.asCompletableFuture()
    }
}
