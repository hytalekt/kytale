/**
 * # Generic Event System Definitions
 *
 * This file defines the data classes that hold configuration for generic ECS event systems.
 * These definitions are created by the DSL builders and consumed by the system registry
 * to create actual ECS systems at boot time.
 *
 * ## Architecture
 *
 * ```
 * DSL Builder → Definition (this file) → Dynamic System → Hytale ECS
 *     ↓              ↓                        ↓               ↓
 * User code    Data container         Runtime wrapper    Registered system
 * ```
 *
 * ## Definition Types
 *
 * - **[GenericEventSystemDefinition]**: Base sealed interface
 * - **[EntityEventSystemDefinition]**: For entity-level event handling
 * - **[WorldEventSystemDefinition]**: For world-level event handling
 *
 * ## Internal API
 *
 * These classes are internal to Hexweave. Users interact with them indirectly through
 * the DSL builders in [aster.amo.hexweave.dsl.systems.GenericEventSystemBuilder].
 *
 * @see aster.amo.hexweave.dsl.systems.EntityEventSystemBuilder
 * @see aster.amo.hexweave.dsl.systems.WorldEventSystemBuilder
 * @see aster.amo.hexweave.internal.system.HexweaveSystemRegistry
 */
package aster.amo.hexweave.internal.system

import aster.amo.hexweave.dsl.systems.context.EntityEventContext
import aster.amo.hexweave.dsl.systems.context.WorldEventContext
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EcsEvent

/**
 * Base sealed interface for all generic event system definitions.
 *
 * This interface defines the common properties shared by all event system definitions,
 * enabling Hexweave to handle ANY [EcsEvent] type without requiring specific
 * implementation classes for each event type.
 *
 * ## Extensibility
 *
 * When Hytale adds new event types, Hexweave automatically supports them through this
 * generic abstraction. No changes to Hexweave are needed - users simply specify the
 * new event type as a type parameter:
 *
 * ```kotlin
 * // Works with any EcsEvent subtype, present or future
 * entityEventSystem<EntityStore, SomeNewEvent>("handler") { ... }
 * ```
 *
 * ## Properties
 *
 * | Property | Description |
 * |----------|-------------|
 * | [id] | Unique identifier for logging and debugging |
 * | [priority] | Execution order (lower runs first) |
 * | [eventClass] | The event class this system handles (for runtime type checking) |
 * | [query] | Optional entity filter (for EntityEventSystems) |
 * | [dependencies] | System ordering constraints (before/after other systems) |
 * | [filter] | Runtime event filter predicate |
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles (must extend [EcsEvent])
 *
 * @see EntityEventSystemDefinition for entity-level handling
 * @see WorldEventSystemDefinition for world-level handling
 */
@PublishedApi
internal sealed interface GenericEventSystemDefinition<STORE, EVENT : EcsEvent> {
    /**
     * Unique identifier for this system definition.
     *
     * Used for:
     * - Logging and debugging
     * - Removal/replacement of systems
     * - Dependency references
     */
    val id: String

    /**
     * Priority for system ordering (lower values execute first).
     *
     * Systems with the same priority are executed in registration order.
     * Default is 0. Use negative values for early execution, positive for late.
     */
    val priority: Int

    /**
     * The event class this system handles.
     *
     * Used at runtime by Hytale's ECS to route events to the correct system.
     * Captured from the reified type parameter in the DSL.
     */
    val eventClass: Class<EVENT>

    /**
     * Optional query to filter which entities this system processes.
     *
     * For [EntityEventSystemDefinition], this filters which entity archetypes
     * receive the event. If null, a default query is used.
     *
     * For [WorldEventSystemDefinition], this is typically null as world systems
     * don't process individual entities.
     */
    val query: Query<STORE>?

    /**
     * System dependencies for ordering relative to other systems.
     *
     * Use the `dependencies` block in the DSL to specify these:
     * ```kotlin
     * dependencies {
     *     before<SomeSystem>()  // Run before SomeSystem
     *     after<OtherSystem>()  // Run after OtherSystem
     * }
     * ```
     */
    val dependencies: Set<Dependency<STORE?>>

    /**
     * Filter predicate to decide whether to process a specific event.
     *
     * Called for each event before the handler. If returns false, the event
     * is skipped for this system.
     *
     * ```kotlin
     * filter { it.amount > 0 }  // Only process if amount is positive
     * ```
     */
    val filter: (EVENT) -> Boolean
}

/**
 * Definition for an [EntityEventSystem] that handles events at the entity level.
 *
 * EntityEventSystems process events for each matching entity within archetype chunks.
 * They implement `QuerySystem` to filter which entities they process based on their
 * component composition.
 *
 * ## How It Works
 *
 * 1. When an [EcsEvent] of type [EVENT] is dispatched
 * 2. Hytale's ECS routes it to this system
 * 3. The [filter] is checked - if false, event is skipped
 * 4. For each entity matching the [query], the [handler] is invoked
 * 5. The handler receives an [EntityEventContext] with access to the entity
 *
 * ## Usage via DSL
 *
 * Users create these definitions through the DSL, not directly:
 *
 * ```kotlin
 * systems {
 *     entityEventSystem<EntityStore, Damage>("damage-handler") {
 *         // Optional: custom query (default matches all living entities)
 *         query {
 *             ArchetypeQuery.builder<EntityStore>()
 *                 .require(Health.getComponentType())
 *                 .build()
 *         }
 *
 *         // Optional: execution ordering
 *         dependencies {
 *             before<DamageSystems.ApplyDamage>()
 *         }
 *
 *         // Optional: event filter
 *         filter { it.amount > 0 }
 *
 *         // Required: the handler
 *         onEvent {
 *             val health = commandBuffer.get<Health>(chunk, index)
 *             logger.info { "Entity took ${event.amount} damage" }
 *         }
 *     }
 * }
 * ```
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property handler The handler function invoked for each matching entity.
 *   Receives an [EntityEventContext] with access to entity index, chunk, store,
 *   command buffer, and the event.
 *
 * @see GenericEventSystemDefinition for common properties
 * @see EntityEventContext for the handler context
 * @see aster.amo.hexweave.dsl.systems.EntityEventSystemBuilder for the DSL
 */
@PublishedApi
internal data class EntityEventSystemDefinition<STORE, EVENT : EcsEvent>(
    override val id: String,
    override val eventClass: Class<EVENT>,
    override val priority: Int = 0,
    override val query: Query<STORE>? = null,
    override val dependencies: Set<Dependency<STORE?>> = emptySet(),
    override val filter: (EVENT) -> Boolean = { true },
    /**
     * Handler invoked for each matching entity when the event occurs.
     *
     * The handler receives an [EntityEventContext] providing access to:
     * - `index`: Entity index within the chunk
     * - `chunk`: The archetype chunk containing the entity
     * - `store`: The entity store
     * - `commandBuffer`: For queuing entity modifications
     * - `event`: The event being processed
     */
    val handler: EntityEventContext<STORE, EVENT>.() -> Unit
) : GenericEventSystemDefinition<STORE, EVENT>

/**
 * Definition for a [WorldEventSystem] that handles events at the world level.
 *
 * WorldEventSystems process events that affect the entire world rather than
 * specific entities. They are invoked once per event, not per-entity.
 *
 * ## When to Use WorldEventSystem
 *
 * Use WorldEventSystem for events that:
 * - Affect the entire world (time changes, weather, world state)
 * - Don't need per-entity processing
 * - Are global notifications (player count changes, server events)
 *
 * For entity-specific event handling, use [EntityEventSystemDefinition] instead.
 *
 * ## How It Works
 *
 * 1. When an [EcsEvent] of type [EVENT] is dispatched
 * 2. Hytale's ECS routes it to this system
 * 3. The [filter] is checked - if false, event is skipped
 * 4. The [handler] is invoked once with a [WorldEventContext]
 *
 * ## Usage via DSL
 *
 * Users create these definitions through the DSL, not directly:
 *
 * ```kotlin
 * systems {
 *     worldEventSystem<EntityStore, TimeChangeEvent>("time-handler") {
 *         // Optional: event filter
 *         filter { it.newTime == DayTime.DAWN }
 *
 *         // Optional: execution ordering
 *         dependencies {
 *             after<SomeOtherSystem>()
 *         }
 *
 *         // Required: the handler
 *         onEvent {
 *             logger.info { "Dawn breaks! Time: ${event.newTime}" }
 *             // Can still access store and commandBuffer for world-wide changes
 *         }
 *     }
 * }
 * ```
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property handler The handler function invoked once when the event occurs.
 *   Receives a [WorldEventContext] with access to store, command buffer, and the event.
 *
 * @see GenericEventSystemDefinition for common properties
 * @see WorldEventContext for the handler context
 * @see aster.amo.hexweave.dsl.systems.WorldEventSystemBuilder for the DSL
 */
@PublishedApi
internal data class WorldEventSystemDefinition<STORE, EVENT : EcsEvent>(
    override val id: String,
    override val eventClass: Class<EVENT>,
    override val priority: Int = 0,
    override val query: Query<STORE>? = null,
    override val dependencies: Set<Dependency<STORE?>> = emptySet(),
    override val filter: (EVENT) -> Boolean = { true },
    /**
     * Handler invoked once when the event occurs at the world level.
     *
     * The handler receives a [WorldEventContext] providing access to:
     * - `store`: The entity store
     * - `commandBuffer`: For queuing entity modifications
     * - `event`: The event being processed
     *
     * Note: Unlike [EntityEventSystemDefinition], this handler is called once
     * per event, not once per matching entity.
     */
    val handler: WorldEventContext<STORE, EVENT>.() -> Unit
) : GenericEventSystemDefinition<STORE, EVENT>
