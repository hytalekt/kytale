package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.inventory.ItemStack
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

typealias InteractWithBlockExecutor = (
    world: World, commandBuffer: CommandBuffer<EntityStore?>, type: InteractionType, context: InteractionContext, itemStack: ItemStack?, blockPos: Vector3i, cooldownHandler: CooldownHandler
) -> Unit

typealias SimulateInteractWithBlockExecutor = (
    type: InteractionType, context: InteractionContext, itemStack: ItemStack?, world: World, blockPos: Vector3i
) -> Unit

open class KytaleSimpleBlockInteractionDelegate(
    var interactWithBlockExecutor: InteractWithBlockExecutor? = null,
    var simulateInteractWithBlockExecutor: SimulateInteractWithBlockExecutor? = null
) : SimpleBlockInteraction() {

    public override fun interactWithBlock(
        p0: World,
        p1: CommandBuffer<EntityStore?>,
        p2: InteractionType,
        p3: InteractionContext,
        p4: ItemStack?,
        p5: Vector3i,
        p6: CooldownHandler
    ) = interactWithBlockExecutor?.invoke(p0, p1, p2, p3, p4, p5, p6)
        ?: error("Uninitialized executor called, please provide an implementation using interactWithBlock {}")

    public override fun simulateInteractWithBlock(
        p0: InteractionType,
        p1: InteractionContext,
        p2: ItemStack?,
        p3: World,
        p4: Vector3i
    ) = simulateInteractWithBlockExecutor?.invoke(p0, p1, p2, p3, p4)
        ?: error("Uninitialized executor called, please provide an implementation using simulateInteractWithBlock {}")
}
