@file:Suppress("NOTHING_TO_INLINE")

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

// Create a new anonymous class + anonymous object to delegate the firstRun call.
// Since this function is inline, a new class will be created for each call, circumventing the
// restriction of one class per interactionId.
inline fun createDelegatedSimpleBlockInteraction(): KytaleSimpleBlockInteractionDelegate =
    object : KytaleSimpleBlockInteractionDelegate() {}
