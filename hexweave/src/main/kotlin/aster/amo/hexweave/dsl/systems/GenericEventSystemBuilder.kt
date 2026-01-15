/**
 * # Generic Event System Builders
 *
 * This file contains the DSL builder classes for creating generic ECS event systems.
 * These builders allow users to define event handlers for **ANY** [EcsEvent] type
 * without Hexweave needing specific implementations for each event type.
 *
 * ## Architecture
 *
 * ```
 * User DSL → Builder (this file) → Definition → Registry → Dynamic System → ECS
 *    ↓              ↓                    ↓           ↓            ↓
 * entityEventSystem<>()  Collects config   Data class   Stores     Actual system
 * worldEventSystem<>()   Validates         Pure data    Boots      Hytale interface
 * ```
 *
 * ## Builder Types
 *
 * | Builder | Output | Use Case |
 * |---------|--------|----------|
 * | [EntityEventSystemBuilder] | [EntityEventSystemDefinition] | Per-entity event handling |
 * | [WorldEventSystemBuilder] | [WorldEventSystemDefinition] | World-level event handling |
 *
 * ## DSL Pattern
 *
 * Builders are annotated with [@HexweaveDsl] to prevent scope leakage in nested DSL blocks.
 * Users never instantiate builders directly - they use the DSL methods in [HexweaveSystemsScope].
 *
 * ## Generic Type Parameters
 *
 * Both builders take two type parameters:
 * - `STORE`: The entity store type (typically `EntityStore`)
 * - `EVENT`: The specific [EcsEvent] subtype being handled
 *
 * The event type is captured as a reified parameter in the DSL methods and stored
 * as `Class<EVENT>` for runtime type checking by Hytale's ECS.
 *
 * @see EntityEventSystemBuilder for per-entity event handling
 * @see WorldEventSystemBuilder for world-level event handling
 * @see HexweaveSystemsScope.entityEventSystem for DSL entry point
 * @see HexweaveSystemsScope.worldEventSystem for DSL entry point
 */
package aster.amo.hexweave.dsl.systems

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.dsl.systems.context.EntityEventContext
import aster.amo.hexweave.dsl.systems.context.WorldEventContext
import aster.amo.hexweave.internal.system.EntityEventSystemDefinition
import aster.amo.hexweave.internal.system.WorldEventSystemDefinition
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EcsEvent

/**
 * Builder for creating [EntityEventSystem] definitions through the Hexweave DSL.
 *
 * This builder collects all configuration for an entity-level event system and
 * produces an [EntityEventSystemDefinition] when [build] is called. The builder
 * works with **ANY** [EcsEvent] type through generic type parameters.
 *
 * ## Usage
 *
 * Entity event systems are defined within the `systems` DSL block:
 *
 * ```kotlin
 * systems {
 *     entityEventSystem<EntityStore, PlaceBlockEvent>("block-handler") {
 *         // Optional: Set execution priority (lower = earlier)
 *         priority = 10
 *
 *         // Optional: Filter which entities receive this event
 *         query {
 *             ArchetypeQuery.builder<EntityStore>()
 *                 .require(SomeComponent.getComponentType())
 *                 .build()
 *         }
 *
 *         // Optional: Filter which events to process
 *         filter { !it.isCancelled }
 *
 *         // Optional: Declare execution ordering constraints
 *         dependencies {
 *             before<SomeOtherSystem>()
 *             after<AnotherSystem>()
 *         }
 *
 *         // Required: Define the handler logic
 *         onEvent {
 *             // EntityEventContext provides:
 *             // - index: Entity index within chunk
 *             // - chunk: The archetype chunk
 *             // - store: The entity store
 *             // - commandBuffer: For queuing modifications
 *             // - event: The event being processed
 *             val component = commandBuffer.get<SomeComponent>(chunk, index)
 *             logger.info { "Block placed: ${event.blockType}" }
 *         }
 *     }
 * }
 * ```
 *
 * ## Configuration Options
 *
 * | Option | Required | Default | Description |
 * |--------|----------|---------|-------------|
 * | [priority] | No | 0 | Execution order (lower runs first) |
 * | [query] | No | All living entities | Which entities process this event |
 * | [filter] | No | Accept all | Runtime predicate on events |
 * | [dependencies] | No | None | Execution ordering constraints |
 * | [onEvent] | **Yes** | - | The handler logic |
 *
 * ## Error Handling
 *
 * - Calling [build] without defining [onEvent] throws [IllegalStateException]
 * - Exceptions in handlers are caught and logged (see [DynamicEntityEventSystem])
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property id Unique identifier for this system (used for logging and removal)
 * @property eventClass The event class captured from reified type parameter
 *
 * @see WorldEventSystemBuilder for world-level events
 * @see EntityEventContext for the handler context
 * @see EntityEventSystemDefinition for the output data class
 */
@HexweaveDsl
class EntityEventSystemBuilder<STORE, EVENT : EcsEvent>(
    private val id: String,
    private val eventClass: Class<EVENT>
) {
    /**
     * Priority for system execution ordering.
     *
     * Lower values execute first. Systems with equal priority execute in
     * registration order. Use negative values for early execution, positive
     * for late execution.
     *
     * ## Examples
     *
     * ```kotlin
     * priority = -100  // Run very early (e.g., damage prevention)
     * priority = 0     // Default - normal execution order
     * priority = 100   // Run late (e.g., logging, cleanup)
     * ```
     *
     * @see dependencies for fine-grained ordering between specific systems
     */
    var priority: Int = 0

    private var queryProvider: (() -> Query<STORE>)? = null
    @PublishedApi internal var deps: Set<Dependency<STORE?>> = emptySet()
    private var filterPredicate: (EVENT) -> Boolean = { true }
    private var handlerBlock: (EntityEventContext<STORE, EVENT>.() -> Unit)? = null

    /**
     * Sets the query that determines which entities this system processes.
     *
     * The query filters entities by their component composition (archetype).
     * Only entities matching the query will have their events processed by
     * this system's handler.
     *
     * ## Default Behavior
     *
     * If not specified, the system uses a default query:
     * - Generic systems default to `AllLegacyLivingEntityTypesQuery`
     * - Custom factories may provide event-type-specific defaults
     *
     * ## Usage
     *
     * ```kotlin
     * query {
     *     ArchetypeQuery.builder<EntityStore>()
     *         .require(Health.getComponentType())      // Must have Health
     *         .require(Position.getComponentType())    // Must have Position
     *         .exclude(Invulnerable.getComponentType()) // Must NOT have Invulnerable
     *         .build()
     * }
     * ```
     *
     * @param block Lambda that returns the query. Evaluated once at build time.
     *
     * @see com.hypixel.hytale.component.query.ArchetypeQuery
     */
    fun query(block: () -> Query<STORE>) {
        queryProvider = block
    }

    /**
     * Declares system dependencies for execution ordering relative to other systems.
     *
     * Dependencies provide fine-grained control over when this system executes
     * relative to specific other systems. This is more precise than [priority]
     * which only provides global ordering.
     *
     * ## Usage
     *
     * ```kotlin
     * dependencies {
     *     before<DamageSystems.ApplyDamage>()  // Run BEFORE damage is applied
     *     after<BuffSystems.CalculateStats>()  // Run AFTER stats are calculated
     * }
     * ```
     *
     * ## Dependency Types
     *
     * - `before<T>()` - This system must execute before system T
     * - `after<T>()` - This system must execute after system T
     *
     * ## Combining with Priority
     *
     * Dependencies take precedence over priority. Use priority for general
     * ordering and dependencies for specific constraints between systems.
     *
     * @param block DSL block for declaring dependencies
     *
     * @see DependencyBuilder for available dependency methods
     */
    inline fun dependencies(block: DependencyBuilder.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        deps = DependencyBuilder().apply(block).build() as Set<Dependency<STORE?>>
    }

    /**
     * Filters which events this handler processes at runtime.
     *
     * The filter predicate is evaluated for each event before the handler
     * is invoked. If the predicate returns `false`, the event is skipped
     * for this system.
     *
     * ## Usage
     *
     * ```kotlin
     * // Only process non-cancelled events
     * filter { !it.isCancelled }
     *
     * // Only process damage events over a threshold
     * filter { it.amount > 10 }
     *
     * // Complex filtering
     * filter { event ->
     *     event.source != null && event.amount > 0
     * }
     * ```
     *
     * ## Performance Note
     *
     * Filters run on every event, so keep predicates efficient. For
     * component-based filtering, prefer [query] which filters at the
     * archetype level.
     *
     * @param predicate Function that returns `true` to process, `false` to skip
     */
    fun filter(predicate: (EVENT) -> Boolean) {
        filterPredicate = predicate
    }

    /**
     * Defines the handler logic that executes for each matching entity-event pair.
     *
     * This is the **required** method that defines what happens when the event
     * occurs for a matching entity. The handler receives an [EntityEventContext]
     * as its receiver, providing access to all relevant ECS data.
     *
     * ## Context Properties
     *
     * | Property | Type | Description |
     * |----------|------|-------------|
     * | `index` | `Int` | Entity index within the archetype chunk |
     * | `chunk` | `ArchetypeChunk<STORE>` | The chunk containing the entity |
     * | `store` | `Store<STORE>` | The entity store |
     * | `commandBuffer` | `CommandBuffer<STORE>` | For queuing modifications |
     * | `event` | `EVENT` | The event being processed |
     *
     * ## Example
     *
     * ```kotlin
     * onEvent {
     *     // Read a component
     *     val health = commandBuffer.get<Health>(chunk, index)
     *
     *     // Modify based on event
     *     if (health != null) {
     *         val newHealth = health.copy(current = health.current - event.amount)
     *         commandBuffer.set(chunk, index, newHealth)
     *     }
     *
     *     // Access event data
     *     logger.info { "Entity $index took ${event.amount} damage" }
     * }
     * ```
     *
     * ## Error Handling
     *
     * Exceptions thrown in the handler are caught and logged by
     * [DynamicEntityEventSystem]. The server continues running and
     * other systems are not affected.
     *
     * @param block The handler logic with [EntityEventContext] as receiver
     * @throws IllegalStateException at build time if not called
     */
    fun onEvent(block: EntityEventContext<STORE, EVENT>.() -> Unit) {
        handlerBlock = block
    }

    /**
     * Builds the [EntityEventSystemDefinition] from the configured options.
     *
     * This method is called internally by [HexweaveSystemsScope.entityEventSystem]
     * after the builder DSL block completes. It validates that required options
     * are set and creates an immutable definition object.
     *
     * @return The complete definition ready for registration
     * @throws IllegalStateException if [onEvent] was not called
     */
    @PublishedApi
    internal fun build(): EntityEventSystemDefinition<STORE, EVENT> {
        val h = handlerBlock
            ?: throw IllegalStateException("EntityEventSystem '$id' requires onEvent handler")

        return EntityEventSystemDefinition(
            id = id,
            eventClass = eventClass,
            priority = priority,
            query = queryProvider?.invoke(),
            dependencies = deps,
            filter = filterPredicate,
            handler = h
        )
    }
}

/**
 * Builder for creating [WorldEventSystem] definitions through the Hexweave DSL.
 *
 * This builder collects configuration for a world-level event system and produces
 * a [WorldEventSystemDefinition] when [build] is called. World event systems handle
 * events that affect the entire game world rather than specific entities.
 *
 * ## Differences from EntityEventSystemBuilder
 *
 * | Aspect | EntityEventSystem | WorldEventSystem |
 * |--------|-------------------|------------------|
 * | Handler invocation | Once per matching entity | Once per event |
 * | Query support | Yes (filters entities) | No (not applicable) |
 * | Context | [EntityEventContext] with index/chunk | [WorldEventContext] |
 * | Use case | Per-entity processing | Global state changes |
 *
 * ## Usage
 *
 * World event systems are defined within the `systems` DSL block:
 *
 * ```kotlin
 * systems {
 *     worldEventSystem<EntityStore, TimeChangeEvent>("time-handler") {
 *         // Optional: Set execution priority
 *         priority = 10
 *
 *         // Optional: Filter which events to process
 *         filter { it.newTime == DayTime.DAWN }
 *
 *         // Optional: Declare execution ordering
 *         dependencies {
 *             after<LightingSystem>()
 *         }
 *
 *         // Required: Define the handler logic
 *         onEvent {
 *             // WorldEventContext provides:
 *             // - store: The entity store
 *             // - commandBuffer: For queuing modifications
 *             // - event: The event being processed
 *             logger.info { "Dawn has arrived!" }
 *
 *             // Can still query entities if needed
 *             // store.query(SomeQuery.INSTANCE).forEach { ... }
 *         }
 *     }
 * }
 * ```
 *
 * ## When to Use WorldEventSystem
 *
 * Use WorldEventSystem for events that:
 * - Affect global game state (time, weather, world settings)
 * - Don't need per-entity iteration
 * - Are notifications about world-wide changes
 * - Trigger responses that span multiple entities
 *
 * For events that should be processed per-entity, use [EntityEventSystemBuilder].
 *
 * ## Configuration Options
 *
 * | Option | Required | Default | Description |
 * |--------|----------|---------|-------------|
 * | [priority] | No | 0 | Execution order (lower runs first) |
 * | [filter] | No | Accept all | Runtime predicate on events |
 * | [dependencies] | No | None | Execution ordering constraints |
 * | [onEvent] | **Yes** | - | The handler logic |
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property id Unique identifier for this system (used for logging and removal)
 * @property eventClass The event class captured from reified type parameter
 *
 * @see EntityEventSystemBuilder for per-entity event handling
 * @see WorldEventContext for the handler context
 * @see WorldEventSystemDefinition for the output data class
 */
@HexweaveDsl
class WorldEventSystemBuilder<STORE, EVENT : EcsEvent>(
    private val id: String,
    private val eventClass: Class<EVENT>
) {
    /**
     * Priority for system execution ordering.
     *
     * Lower values execute first. Systems with equal priority execute in
     * registration order.
     *
     * @see EntityEventSystemBuilder.priority for detailed documentation
     * @see dependencies for fine-grained ordering
     */
    var priority: Int = 0

    @PublishedApi internal var deps: Set<Dependency<STORE?>> = emptySet()
    private var filterPredicate: (EVENT) -> Boolean = { true }
    private var handlerBlock: (WorldEventContext<STORE, EVENT>.() -> Unit)? = null

    /**
     * Declares system dependencies for execution ordering relative to other systems.
     *
     * ## Usage
     *
     * ```kotlin
     * dependencies {
     *     before<WeatherRenderingSystem>()  // Run before rendering updates
     *     after<TimeCalculationSystem>()    // Run after time is calculated
     * }
     * ```
     *
     * @param block DSL block for declaring dependencies
     * @see DependencyBuilder for available dependency methods
     * @see EntityEventSystemBuilder.dependencies for detailed documentation
     */
    inline fun dependencies(block: DependencyBuilder.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        deps = DependencyBuilder().apply(block).build() as Set<Dependency<STORE?>>
    }

    /**
     * Filters which events this handler processes at runtime.
     *
     * ## Usage
     *
     * ```kotlin
     * // Only process specific time changes
     * filter { it.newTime == DayTime.DAWN || it.newTime == DayTime.DUSK }
     *
     * // Only process full moon phases
     * filter { it.moonPhase == MoonPhase.FULL }
     * ```
     *
     * @param predicate Function that returns `true` to process, `false` to skip
     * @see EntityEventSystemBuilder.filter for detailed documentation
     */
    fun filter(predicate: (EVENT) -> Boolean) {
        filterPredicate = predicate
    }

    /**
     * Defines the handler logic that executes once per event.
     *
     * This is the **required** method that defines what happens when the event
     * occurs. Unlike [EntityEventSystemBuilder.onEvent], this handler is invoked
     * **once per event**, not once per entity.
     *
     * ## Context Properties
     *
     * | Property | Type | Description |
     * |----------|------|-------------|
     * | `store` | `Store<STORE>` | The entity store for queries |
     * | `commandBuffer` | `CommandBuffer<STORE>` | For queuing modifications |
     * | `event` | `EVENT` | The event being processed |
     *
     * ## Example
     *
     * ```kotlin
     * onEvent {
     *     // React to the event
     *     when (event.newPhase) {
     *         MoonPhase.NEW -> logger.info { "New moon - darkness falls" }
     *         MoonPhase.FULL -> {
     *             logger.info { "Full moon - creatures stir" }
     *             // Could trigger world-wide effects here
     *         }
     *     }
     *
     *     // Can still access entities through the store
     *     // store.query(PlayerQuery.INSTANCE).forEach { ... }
     * }
     * ```
     *
     * ## Error Handling
     *
     * Exceptions thrown in the handler are caught and logged by
     * [DynamicWorldEventSystem]. The server continues running.
     *
     * @param block The handler logic with [WorldEventContext] as receiver
     * @throws IllegalStateException at build time if not called
     */
    fun onEvent(block: WorldEventContext<STORE, EVENT>.() -> Unit) {
        handlerBlock = block
    }

    /**
     * Builds the [WorldEventSystemDefinition] from the configured options.
     *
     * This method is called internally by [HexweaveSystemsScope.worldEventSystem]
     * after the builder DSL block completes.
     *
     * @return The complete definition ready for registration
     * @throws IllegalStateException if [onEvent] was not called
     */
    @PublishedApi
    internal fun build(): WorldEventSystemDefinition<STORE, EVENT> {
        val h = handlerBlock
            ?: throw IllegalStateException("WorldEventSystem '$id' requires onEvent handler")

        return WorldEventSystemDefinition(
            id = id,
            eventClass = eventClass,
            priority = priority,
            query = null, // WorldEventSystems don't use queries
            dependencies = deps,
            filter = filterPredicate,
            handler = h
        )
    }
}
