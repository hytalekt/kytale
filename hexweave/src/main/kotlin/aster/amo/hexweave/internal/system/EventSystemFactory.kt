/**
 * # Event System Factory Pattern
 *
 * This file provides the factory pattern interfaces for creating ECS event systems
 * from DSL definitions. The factory pattern enables **specialized handling** for
 * specific event types while maintaining a unified registration mechanism.
 *
 * ## Architecture
 *
 * ```
 * Definition → Registry → Factory? → System
 *                  ↓           ↓
 *           Has factory?    Custom system
 *                  ↓           (enriched context)
 *           No factory?
 *                  ↓
 *           Default system
 *           (generic context)
 * ```
 *
 * ## Why Factories?
 *
 * The generic event system provides a basic [EntityEventContext] or [WorldEventContext]
 * with raw ECS data. Some event types benefit from **enriched contexts** with
 * additional data or pre-processing.
 *
 * ## Factory Registration
 *
 * Factories are registered with [HexweaveSystemRegistry.registerEntitySystemFactory]:
 *
 * ```kotlin
 * registry.registerEntitySystemFactory(
 *     MyEvent::class.java,
 *     MyEventSystemFactory()
 * )
 * ```
 *
 * ## Internal API
 *
 * These interfaces are internal to Hexweave. Users interact with them indirectly
 * through the DSL. The [HexweaveSystemRegistry] uses factories during the boot phase.
 *
 * @see EntityEventSystemFactory for entity-level event handling
 * @see WorldEventSystemFactory for world-level event handling
 * @see HexweaveSystemRegistry for factory registration and system creation
 */
package aster.amo.hexweave.internal.system

import aster.amo.hexweave.internal.HexweaveScope
import com.hypixel.hytale.component.system.EcsEvent
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.component.system.WorldEventSystem
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Factory interface for creating [EntityEventSystem] instances from definitions.
 *
 * This interface enables **specialized system creation** for specific event types.
 * When an event type has a registered factory, the [HexweaveSystemRegistry] uses
 * that factory instead of creating a default [DynamicEntityEventSystem].
 *
 * ## When to Implement
 *
 * Implement this interface when you need to:
 * - Provide an enriched context beyond [EntityEventContext]
 * - Apply event-type-specific default behavior
 * - Perform pre-processing or validation
 *
 * ## Default Behavior
 *
 * If no factory is registered for an event type, [HexweaveSystemRegistry] creates
 * a default system using [DynamicEntityEventSystem] with generic [EntityEventContext].
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this factory handles
 *
 * @see WorldEventSystemFactory for world-level events
 * @see DynamicEntityEventSystem for the default implementation
 * @see HexweaveSystemRegistry.registerEntitySystemFactory for registration
 */
internal interface EntityEventSystemFactory<STORE, EVENT : EcsEvent> {
    /**
     * Creates an [EntityEventSystem] from the given definition.
     *
     * This method is called by [HexweaveSystemRegistry.boot] when registering
     * systems with Hytale's ECS. The implementation should:
     *
     * 1. Extract configuration from the definition
     * 2. Create an appropriate [EntityEventSystem] subclass
     * 3. Optionally enrich the handler context with additional data
     *
     * ## Implementation Notes
     *
     * - The returned system will be registered with `plugin.entityStoreRegistry`
     * - The [scope] provides access to Hexweave services
     * - The [definition] contains all user-specified configuration from the DSL
     * - Errors in handler execution should be caught and logged, not rethrown
     *
     * @param plugin The plugin that owns this system
     * @param scope The Hexweave scope providing access to services
     * @param definition The system definition created by the DSL builder
     * @return A fully configured [EntityEventSystem] ready for registration
     */
    fun create(
        plugin: JavaPlugin,
        scope: HexweaveScope,
        definition: EntityEventSystemDefinition<STORE, EVENT>
    ): EntityEventSystem<STORE, EVENT>
}

/**
 * Factory interface for creating [WorldEventSystem] instances from definitions.
 *
 * Similar to [EntityEventSystemFactory], but for world-level events that don't
 * iterate over individual entities. World events affect the entire game world
 * rather than specific entities.
 *
 * ## When to Implement
 *
 * Implement this interface when you need to:
 * - Provide enriched context for world events
 * - Integrate world events with global services
 * - Apply world-state-specific default behavior
 *
 * ## Example: Time Change Factory
 *
 * ```kotlin
 * class TimeChangeSystemFactory(
 *     private val lightingService: LightingService
 * ) : WorldEventSystemFactory<EntityStore, TimeChangeEvent> {
 *
 *     override fun create(
 *         plugin: JavaPlugin,
 *         scope: HexweaveScope,
 *         definition: WorldEventSystemDefinition<EntityStore, TimeChangeEvent>
 *     ): WorldEventSystem<EntityStore, TimeChangeEvent> {
 *         return object : DynamicWorldEventSystem<EntityStore, TimeChangeEvent>(plugin, definition) {
 *             override fun handle(store: Store<EntityStore>, commandBuffer: CommandBuffer<EntityStore>, event: TimeChangeEvent) {
 *                 // Pre-process: update lighting calculations
 *                 lightingService.precomputeForTime(event.newTime)
 *                 super.handle(store, commandBuffer, event)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ## Default Behavior
 *
 * If no factory is registered, [HexweaveSystemRegistry] creates a default system
 * using [DynamicWorldEventSystem] with generic [WorldEventContext].
 *
 * @param STORE the entity store type (typically `EntityStore`)
 * @param EVENT the ECS event type this factory handles
 *
 * @see EntityEventSystemFactory for entity-level events
 * @see DynamicWorldEventSystem for the default implementation
 * @see HexweaveSystemRegistry.registerWorldSystemFactory for registration
 */
internal interface WorldEventSystemFactory<STORE, EVENT : EcsEvent> {
    /**
     * Creates a [WorldEventSystem] from the given definition.
     *
     * This method is called by [HexweaveSystemRegistry.boot] when registering
     * world-level systems with Hytale's ECS.
     *
     * ## Implementation Notes
     *
     * - The returned system will be registered with `plugin.entityStoreRegistry`
     * - World systems are invoked once per event, not per-entity
     * - The handler can still query entities through the store if needed
     *
     * @param plugin The plugin that owns this system. Used for logging and
     *   registration with the entity store registry.
     * @param scope The Hexweave scope providing access to services.
     * @param definition The system definition created by the DSL builder,
     *   containing id, priority, dependencies, filter, and handler.
     * @return A fully configured [WorldEventSystem] ready for registration
     */
    fun create(
        plugin: JavaPlugin,
        scope: HexweaveScope,
        definition: WorldEventSystemDefinition<STORE, EVENT>
    ): WorldEventSystem<STORE, EVENT>
}

/**
 * Type-erased wrapper for calling [EntityEventSystemFactory.create] with wildcard types.
 *
 * This extension function enables the [HexweaveSystemRegistry] to invoke factory
 * methods when the generic types have been erased to wildcards. The registry stores
 * factories keyed by event class, so the casts are safe at runtime.
 *
 * ## Why This Exists
 *
 * The registry needs to store factories for ANY event type in a single map:
 * ```kotlin
 * private val factories = ConcurrentHashMap<Class<*>, EntityEventSystemFactory<*, *>>()
 * ```
 *
 * When retrieving a factory, the types are wildcards. This function performs the
 * necessary unchecked casts to invoke the strongly-typed `create()` method.
 *
 * ## Safety Guarantee
 *
 * The casts are safe because:
 * 1. Factories are registered with `registerEntitySystemFactory(eventClass, factory)`
 * 2. The `eventClass` key ensures type consistency
 * 3. Definitions are matched to factories by their `eventClass` property
 *
 * @param plugin The plugin that owns this system
 * @param scope The Hexweave scope with services
 * @param definition The system definition (type-erased)
 * @return The created system, typed as `EntityEventSystem<EntityStore, *>`
 *
 * @see HexweaveSystemRegistry.boot for usage context
 */
@Suppress("UNCHECKED_CAST")
internal fun EntityEventSystemFactory<*, *>.createUnchecked(
    plugin: JavaPlugin,
    scope: HexweaveScope,
    definition: EntityEventSystemDefinition<*, *>
): EntityEventSystem<EntityStore, *> {
    val factory = this as EntityEventSystemFactory<EntityStore, EcsEvent>
    val def = definition as EntityEventSystemDefinition<EntityStore, EcsEvent>
    return factory.create(plugin, scope, def)
}

/**
 * Type-erased wrapper for calling [WorldEventSystemFactory.create] with wildcard types.
 *
 * This extension function enables the [HexweaveSystemRegistry] to invoke factory
 * methods when the generic types have been erased to wildcards. The registry stores
 * factories keyed by event class, so the casts are safe at runtime.
 *
 * ## Why This Exists
 *
 * Same rationale as [EntityEventSystemFactory.createUnchecked] - the registry
 * stores factories with wildcards but needs to invoke strongly-typed methods.
 *
 * ## Safety Guarantee
 *
 * The casts are safe because factories and definitions are matched by event class.
 *
 * @param plugin The plugin that owns this system
 * @param scope The Hexweave scope with services
 * @param definition The system definition (type-erased)
 * @return The created system, typed as `WorldEventSystem<EntityStore, *>`
 *
 * @see HexweaveSystemRegistry.boot for usage context
 */
@Suppress("UNCHECKED_CAST")
internal fun WorldEventSystemFactory<*, *>.createUnchecked(
    plugin: JavaPlugin,
    scope: HexweaveScope,
    definition: WorldEventSystemDefinition<*, *>
): WorldEventSystem<EntityStore, *> {
    val factory = this as WorldEventSystemFactory<EntityStore, EcsEvent>
    val def = definition as WorldEventSystemDefinition<EntityStore, EcsEvent>
    return factory.create(plugin, scope, def)
}
