package aster.amo.hexweave.internal.system

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.concurrent.atomic.AtomicReference

/**
 * Legacy adapter that wraps EntityTickingSystem and fans out to multiple registered handlers.
 *
 * This allows multiple tick handlers to be registered through the DSL without
 * needing to create separate EntityTickingSystem implementations.
 *
 * @deprecated Use [DynamicTickSystem] with [TickSystemDefinition] instead.
 */
internal class HexweaveTickAdapter(
    private val plugin: JavaPlugin,
    initialHandlers: List<TickHandler>
) : EntityTickingSystem<EntityStore>() {

    private val handlers = AtomicReference(initialHandlers)

    override fun getQuery(): Query<EntityStore> = handlers.get().firstOrNull()?.query
        ?: throw IllegalStateException("No tick handlers registered")

    /**
     * Refreshes the list of handlers (thread-safe).
     */
    fun refresh(newHandlers: List<TickHandler>) {
        handlers.set(newHandlers)
    }

    override fun tick(
        deltaTime: Float,
        tickIndex: Int,
        archetypeChunk: ArchetypeChunk<EntityStore>,
        store: Store<EntityStore>,
        commandBuffer: CommandBuffer<EntityStore>
    ) {
        val currentHandlers = handlers.get()
        if (currentHandlers.isEmpty()) return

        val chunkSize = archetypeChunk.size()

        for (index in 0 until chunkSize) {
            val context = TickContext(
                deltaTime = deltaTime,
                tickIndex = tickIndex,
                index = index,
                chunk = archetypeChunk,
                store = store,
                commandBuffer = commandBuffer
            )

            for (handler in currentHandlers) {
                try {
                    if (handler.tickInterval > 1 && tickIndex % handler.tickInterval != 0) {
                        continue
                    }

                    handler.handler(context)
                } catch (e: Exception) {
                    plugin.logger.atSevere()
                        .withCause(e)
                        .log("Error in tick handler '${handler.id}'")
                }
            }
        }
    }
}
