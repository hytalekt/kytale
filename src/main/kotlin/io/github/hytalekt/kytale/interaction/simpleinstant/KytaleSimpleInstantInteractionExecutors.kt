package io.github.hytalekt.kytale.interaction.simpleinstant

import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import io.github.hytalekt.kytale.interaction.KyInteractionExecutorHolder

typealias FirstRunExecutor = (InteractionType, InteractionContext, CooldownHandler) -> Unit

// Create a new
// Since this function is inline, a new class will be created for each call, circumventing the
// restriction of one class per interactionId.
inline fun createDelegatedSimpleInstantInteraction(
    interactionId: String,
    firstRunHolder: KyInteractionExecutorHolder<FirstRunExecutor>
): SimpleInstantInteraction = object : SimpleInstantInteraction() {
    override fun firstRun(
        var1: InteractionType, var2: InteractionContext, var3: CooldownHandler
    ) = firstRunHolder.executor?.let { it(var1, var2, var3) }
        ?: error("$interactionId: Uninitialized executor called, please provide an implementation using firstRun {}")
}
