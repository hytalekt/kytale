/**
 * # ECS Event Context Hierarchy
 *
 * This file defines the context classes that are passed to ECS event system handlers.
 * These contexts provide access to ECS resources (store, command buffer, event) in a
 * type-safe manner.
 *
 * ## Context Types
 *
 * - **[EcsEventContext]**: Base sealed interface for all contexts
 * - **[EntityEventContext]**: For [EntityEventSystem] handlers - includes entity index and chunk
 * - **[WorldEventContext]**: For [WorldEventSystem] handlers - world-level resources only
 *
 * ## Usage
 *
 * Contexts are automatically created and passed to your handler functions when you define
 * event systems using the Hexweave DSL:
 *
 * ```kotlin
 * systems {
 *     entityEventSystem<EntityStore, Damage>("damage-handler") {
 *         onEvent {
 *             // 'this' is EntityEventContext<EntityStore, Damage>
 *             val health = commandBuffer.get<Health>(chunk, index)
 *             event.amount // Access the damage amount
 *         }
 *     }
 *
 *     worldEventSystem<EntityStore, TimeChangeEvent>("time-handler") {
 *         onEvent {
 *             // 'this' is WorldEventContext<EntityStore, TimeChangeEvent>
 *             event.newTime // Access the new time
 *         }
 *     }
 * }
 * ```
 *
 * @see EntityEventContext
 * @see WorldEventContext
 * @see aster.amo.hexweave.dsl.systems.HexweaveSystemsScope.entityEventSystem
 * @see aster.amo.hexweave.dsl.systems.HexweaveSystemsScope.worldEventSystem
 */
package aster.amo.hexweave.dsl.systems.context

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.EcsEvent

/**
 * Base context interface for all ECS event system handlers.
 *
 * This sealed interface provides access to common ECS resources that are available
 * during event processing. It is extended by [EntityEventContext] and [WorldEventContext]
 * to provide additional context-specific resources.
 *
 * ## Common Properties
 *
 * All ECS event contexts provide:
 * - **[store]**: The entity store containing all entities and their components
 * - **[commandBuffer]**: A buffer for queuing entity modifications (add/remove components)
 * - **[event]**: The ECS event being processed
 *
 * ## Thread Safety
 *
 * ECS event handlers run on the server tick thread. The [commandBuffer] is the safe way
 * to modify entity state during event processing - direct component modification should
 * be done through the command buffer to ensure thread safety.
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type being handled (must extend [EcsEvent])
 *
 * @see EntityEventContext for entity-level event handling
 * @see WorldEventContext for world-level event handling
 */
sealed interface EcsEventContext<STORE, EVENT : EcsEvent> {
    /** The entity store containing all entities. */
    val store: Store<STORE>

    /** Command buffer for queuing entity modifications. */
    val commandBuffer: CommandBuffer<STORE>

    /** The event being processed. */
    val event: EVENT
}

/**
 * Context for [EntityEventSystem] handlers.
 *
 * Provides access to the specific entity being processed within an archetype chunk,
 * in addition to the common ECS resources from [EcsEventContext].
 *
 * ## Entity Access
 *
 * The entity is identified by its [index] within the [chunk]. To read or modify
 * entity components, use the [commandBuffer]:
 *
 * ```kotlin
 * // Reading a component
 * val health = commandBuffer.get<Health>(chunk, index)
 *
 * // Modifying a component
 * commandBuffer.set(chunk, index, Health(100f))
 *
 * // Adding a component
 * commandBuffer.add(chunk, index, NewComponent())
 *
 * // Removing a component
 * commandBuffer.remove<OldComponent>(chunk, index)
 * ```
 *
 * ## Full Example
 *
 * ```kotlin
 * systems {
 *     entityEventSystem<EntityStore, Damage>("damage-logger") {
 *         filter { it.amount > 0 }
 *         onEvent {
 *             // Access entity-specific data
 *             val health = commandBuffer.get<Health>(chunk, index)
 *             logger.info { "Entity at $index took ${event.amount} damage, health: $health" }
 *         }
 *     }
 * }
 * ```
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type being handled
 * @property index Index of the entity within the archetype chunk (0-based)
 * @property chunk The archetype chunk containing the entity and its components
 *
 * @see EcsEventContext for common properties
 * @see WorldEventContext for world-level events without entity context
 */
class EntityEventContext<STORE, EVENT : EcsEvent>(
    /**
     * Index of the entity within the archetype chunk.
     *
     * Use this with [chunk] and [commandBuffer] to access entity components:
     * `commandBuffer.get<Component>(chunk, index)`
     */
    val index: Int,

    /**
     * The archetype chunk containing the entity.
     *
     * Archetype chunks group entities with the same component composition.
     * Use this with [index] and [commandBuffer] to access entity components.
     */
    val chunk: ArchetypeChunk<STORE>,

    override val store: Store<STORE>,
    override val commandBuffer: CommandBuffer<STORE>,
    override val event: EVENT
) : EcsEventContext<STORE, EVENT>

/**
 * Context for [WorldEventSystem] handlers.
 *
 * Provides access to world-level ECS resources without entity-specific context.
 * Used for events that affect the entire world rather than specific entities,
 * such as time changes, weather events, or world state transitions.
 *
 * ## When to Use WorldEventSystem
 *
 * Use [WorldEventSystem] (and thus [WorldEventContext]) when:
 * - The event affects the entire world, not specific entities
 * - You don't need to iterate over entities
 * - You want to respond to global state changes
 *
 * For entity-specific event handling, use [EntityEventContext] instead.
 *
 * ## Accessing World Data
 *
 * While you don't have direct entity access, you can still query the [store]
 * to find and modify entities if needed:
 *
 * ```kotlin
 * worldEventSystem<EntityStore, TimeChangeEvent>("time-handler") {
 *     onEvent {
 *         // React to the event
 *         if (event.newTime == DayTime.NIGHT) {
 *             logger.info { "Night has fallen!" }
 *             // Could query store and modify entities via commandBuffer
 *         }
 *     }
 * }
 * ```
 *
 * ## Full Example
 *
 * ```kotlin
 * systems {
 *     worldEventSystem<EntityStore, MoonPhaseChangeEvent>("moon-handler") {
 *         filter { it.newPhase == MoonPhase.FULL }
 *         onEvent {
 *             logger.info { "Full moon! Werewolves awaken..." }
 *             // Trigger world-wide effects
 *         }
 *     }
 * }
 * ```
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type being handled
 *
 * @see EcsEventContext for common properties
 * @see EntityEventContext for entity-level events with entity context
 */
class WorldEventContext<STORE, EVENT : EcsEvent>(
    override val store: Store<STORE>,
    override val commandBuffer: CommandBuffer<STORE>,
    override val event: EVENT
) : EcsEventContext<STORE, EVENT>
