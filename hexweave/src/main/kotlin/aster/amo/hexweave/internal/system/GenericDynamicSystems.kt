/**
 * # Generic Dynamic Event Systems
 *
 * This file contains the abstract base classes that wrap DSL-defined event systems
 * into actual Hytale ECS systems. These classes bridge the gap between the user's
 * DSL configuration and Hytale's ECS infrastructure.
 *
 * ## Architecture
 *
 * ```
 * User DSL → Definition → Dynamic System (this file) → Hytale ECS
 *                              ↓
 *                         Wraps definition
 *                         Implements Hytale's interface
 *                         Provides error handling
 *                         Creates context objects
 * ```
 *
 * ## Classes
 *
 * - **[DynamicEntityEventSystem]**: Wraps [EntityEventSystemDefinition] into [EntityEventSystem]
 * - **[DynamicWorldEventSystem]**: Wraps [WorldEventSystemDefinition] into [WorldEventSystem]
 *
 * ## Internal API
 *
 * These classes are internal to Hexweave. The [HexweaveSystemRegistry] creates instances
 * of these during the boot phase. Users never interact with these directly.
 *
 * @see EntityEventSystemDefinition
 * @see WorldEventSystemDefinition
 * @see HexweaveSystemRegistry
 */
package aster.amo.hexweave.internal.system

import aster.amo.hexweave.dsl.systems.context.EntityEventContext
import aster.amo.hexweave.dsl.systems.context.WorldEventContext
import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EcsEvent
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.component.system.WorldEventSystem
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * Abstract base class for dynamically created [EntityEventSystem] implementations.
 *
 * This class bridges Hexweave's DSL-based system definitions with Hytale's ECS.
 * It allows creating EntityEventSystems for ANY [EcsEvent] type without requiring
 * specific implementation classes for each event type.
 *
 * ## Responsibilities
 *
 * 1. **Query Resolution**: Returns the definition's query or falls back to [defaultQuery]
 * 2. **Dependency Resolution**: Returns definition dependencies or [defaultDependencies]
 * 3. **Event Filtering**: Applies the definition's filter before calling the handler
 * 4. **Context Creation**: Creates [EntityEventContext] for the handler
 * 5. **Error Handling**: Catches and logs exceptions from user handlers
 *
 * ## Subclassing
 *
 * Subclasses must implement [defaultQuery] to provide a sensible default when users
 * don't specify a query. For example, the default implementation uses
 * `AllLegacyLivingEntityTypesQuery` to match all living entities.
 *
 * Subclasses may override [defaultDependencies] to provide event-type-specific
 * default system ordering.
 *
 * ## Error Handling
 *
 * Exceptions thrown by user handlers are caught and logged with the system ID.
 * This prevents a buggy handler from crashing the server or disrupting other systems.
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property plugin The plugin that owns this system (for logging)
 * @property definition The DSL-created definition containing all configuration
 *
 * @see EntityEventSystemDefinition
 * @see EntityEventContext
 * @see DynamicWorldEventSystem for world-level events
 */
internal abstract class DynamicEntityEventSystem<STORE, EVENT : EcsEvent>(
    /** The plugin that owns this system. Used for logging errors. */
    protected val plugin: JavaPlugin,
    /** The definition created by the DSL builder. */
    protected val definition: EntityEventSystemDefinition<STORE, EVENT>
) : EntityEventSystem<STORE, EVENT>(definition.eventClass) {

    /**
     * Returns the query that determines which entities this system processes.
     *
     * Uses the definition's query if specified, otherwise falls back to [defaultQuery].
     */
    override fun getQuery(): Query<STORE> = definition.query ?: defaultQuery()

    /**
     * Provides the default query when none is specified in the definition.
     *
     * Subclasses must implement this to return an appropriate default for their
     * event type. The default implementation in [HexweaveSystemRegistry] uses
     * `AllLegacyLivingEntityTypesQuery` to match all living entities.
     *
     * @return A query that matches the appropriate entity archetypes
     */
    protected abstract fun defaultQuery(): Query<STORE>

    /**
     * Returns the system dependencies for execution ordering.
     *
     * Uses the definition's dependencies if specified, otherwise falls back
     * to [defaultDependencies].
     */
    override fun getDependencies(): Set<Dependency<STORE?>?> {
        if (definition.dependencies.isEmpty()) {
            return defaultDependencies()
        }
        @Suppress("UNCHECKED_CAST")
        return definition.dependencies as Set<Dependency<STORE?>?>
    }

    /**
     * Provides default dependencies when none are specified in the definition.
     *
     * Override to add event-type-specific default dependencies. For example,
     * a damage handler might default to running before the damage application system.
     *
     * @return Set of default dependencies, empty by default
     */
    protected open fun defaultDependencies(): Set<Dependency<STORE?>?> = emptySet()

    /**
     * Handles an event for a specific entity.
     *
     * This method is called by Hytale's ECS for each entity matching the query.
     * It:
     * 1. Applies the filter predicate - skips if filter returns false
     * 2. Creates an [EntityEventContext] with all relevant data
     * 3. Invokes the user's handler with the context
     * 4. Catches and logs any exceptions
     *
     * @param index Index of the entity within the archetype chunk
     * @param archetypeChunk The chunk containing the entity
     * @param store The entity store
     * @param commandBuffer Buffer for queuing entity modifications
     * @param event The event being processed
     */
    override fun handle(
        index: Int,
        archetypeChunk: ArchetypeChunk<STORE>,
        store: Store<STORE>,
        commandBuffer: CommandBuffer<STORE>,
        event: EVENT
    ) {
        if (!definition.filter(event)) {
            return
        }

        try {
            val context = EntityEventContext(
                index = index,
                chunk = archetypeChunk,
                store = store,
                commandBuffer = commandBuffer,
                event = event
            )
            definition.handler(context)
        } catch (e: Exception) {
            plugin.logger.atSevere()
                .withCause(e)
                .log("Error in entity event system '${definition.id}'")
        }
    }
}

/**
 * Abstract base class for dynamically created [WorldEventSystem] implementations.
 *
 * This class bridges Hexweave's DSL-based system definitions with Hytale's ECS
 * for world-level events. Unlike [DynamicEntityEventSystem], this handles events
 * that affect the entire world rather than specific entities.
 *
 * ## Responsibilities
 *
 * 1. **Dependency Resolution**: Returns definition dependencies or [defaultDependencies]
 * 2. **Event Filtering**: Applies the definition's filter before calling the handler
 * 3. **Context Creation**: Creates [WorldEventContext] for the handler
 * 4. **Error Handling**: Catches and logs exceptions from user handlers
 *
 * ## Differences from EntityEventSystem
 *
 * - No query - world events don't iterate over entities
 * - No index/chunk - no specific entity context
 * - Handler called once per event, not once per matching entity
 *
 * ## Error Handling
 *
 * Exceptions thrown by user handlers are caught and logged with the system ID.
 * This prevents a buggy handler from crashing the server or disrupting other systems.
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this system handles
 * @property plugin The plugin that owns this system (for logging)
 * @property definition The DSL-created definition containing all configuration
 *
 * @see WorldEventSystemDefinition
 * @see WorldEventContext
 * @see DynamicEntityEventSystem for entity-level events
 */
internal abstract class DynamicWorldEventSystem<STORE, EVENT : EcsEvent>(
    /** The plugin that owns this system. Used for logging errors. */
    protected val plugin: JavaPlugin,
    /** The definition created by the DSL builder. */
    protected val definition: WorldEventSystemDefinition<STORE, EVENT>
) : WorldEventSystem<STORE, EVENT>(definition.eventClass) {

    /**
     * Returns the system dependencies for execution ordering.
     *
     * Uses the definition's dependencies if specified, otherwise falls back
     * to [defaultDependencies].
     */
    override fun getDependencies(): Set<Dependency<STORE?>?> {
        if (definition.dependencies.isEmpty()) {
            return defaultDependencies()
        }
        @Suppress("UNCHECKED_CAST")
        return definition.dependencies as Set<Dependency<STORE?>?>
    }

    /**
     * Provides default dependencies when none are specified in the definition.
     *
     * Override to add event-type-specific default dependencies.
     *
     * @return Set of default dependencies, empty by default
     */
    protected open fun defaultDependencies(): Set<Dependency<STORE?>?> = emptySet()

    /**
     * Handles a world-level event.
     *
     * This method is called by Hytale's ECS once per event (not per entity).
     * It:
     * 1. Applies the filter predicate - skips if filter returns false
     * 2. Creates a [WorldEventContext] with world-level data
     * 3. Invokes the user's handler with the context
     * 4. Catches and logs any exceptions
     *
     * @param store The entity store
     * @param commandBuffer Buffer for queuing entity modifications
     * @param event The event being processed
     */
    override fun handle(
        store: Store<STORE>,
        commandBuffer: CommandBuffer<STORE>,
        event: EVENT
    ) {
        if (!definition.filter(event)) {
            return
        }

        try {
            val context = WorldEventContext(
                store = store,
                commandBuffer = commandBuffer,
                event = event
            )
            definition.handler(context)
        } catch (e: Exception) {
            plugin.logger.atSevere()
                .withCause(e)
                .log("Error in world event system '${definition.id}'")
        }
    }
}
