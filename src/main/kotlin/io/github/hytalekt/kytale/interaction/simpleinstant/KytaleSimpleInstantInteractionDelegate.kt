package io.github.hytalekt.kytale.interaction.simpleinstant;

import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction

typealias FirstRunExecutor = (InteractionType, InteractionContext, CooldownHandler) -> Unit
typealias SimulateFirstRunExecutor = (InteractionType, InteractionContext, CooldownHandler) -> Unit

open class KytaleSimpleInstantInteractionDelegate(
    internal var firstRunExecutor: FirstRunExecutor? = null,
    internal var simulateFirstRunExecutor: SimulateFirstRunExecutor? = null
) : SimpleInstantInteraction() {
    public override fun firstRun(
        type: InteractionType,
        context: InteractionContext,
        cooldownHandler: CooldownHandler
    ) = firstRunExecutor?.invoke(type, context, cooldownHandler)
        ?: error("Uninitialized executor called, please provide an implementation using firstRun {}")

    public override fun simulateFirstRun(
        type: InteractionType,
        context: InteractionContext,
        cooldownHandler: CooldownHandler
    ) = simulateFirstRunExecutor?.invoke(type, context, cooldownHandler)
        ?: super.simulateFirstRun(type, context, cooldownHandler)
}
