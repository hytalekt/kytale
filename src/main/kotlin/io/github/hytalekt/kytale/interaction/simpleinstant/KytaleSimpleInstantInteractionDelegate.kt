package io.github.hytalekt.kytale.interaction.simpleinstant;

import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction

typealias FirstRunExecutor = (InteractionType, InteractionContext, CooldownHandler) -> Unit

open class KytaleSimpleInstantInteractionDelegate(
    internal var firstRunExecutor: FirstRunExecutor? = null,
) : SimpleInstantInteraction() {
    public override fun firstRun(
        var1: InteractionType, var2: InteractionContext, var3: CooldownHandler
    ) = firstRunExecutor?.invoke(var1, var2, var3)
        ?: error("Uninitialized executor called, please provide an implementation using firstRun {}")
}
