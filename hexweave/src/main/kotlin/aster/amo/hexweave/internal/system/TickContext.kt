package aster.amo.hexweave.internal.system

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Context for entity tick handlers.
 * Provides access to the ECS primitives needed for tick-based processing.
 */
class TickContext(
    val deltaTime: Float,
    val tickIndex: Int,
    val index: Int,
    val chunk: ArchetypeChunk<EntityStore>,
    val store: Store<EntityStore>,
    val commandBuffer: CommandBuffer<EntityStore>
)

@Deprecated("Use TickContext instead", ReplaceWith("TickContext"))
typealias EntityTickContext = TickContext
