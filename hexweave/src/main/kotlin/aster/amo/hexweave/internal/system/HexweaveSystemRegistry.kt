package aster.amo.hexweave.internal.system

import aster.amo.hexweave.internal.HexweaveScope
import com.hypixel.hytale.component.system.EcsEvent
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.component.system.WorldEventSystem
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Registry for Hexweave ECS system definitions.
 *
 * Collects system definitions during DSL building and registers the corresponding
 * ECS systems when [boot] is called.
 *
 * The registry supports arbitrary system types through a unified definition approach:
 * - Generic: EntityEventSystemDefinition/WorldEventSystemDefinition for ANY event type
 * - Specialized: TickSystemDefinition, DamageSystemDefinition (backward compatible)
 *
 * Custom factories can be registered for event types that need specialized handling
 * (e.g., Damage events with enriched HexweaveDamageContext).
 */
@PublishedApi
internal class HexweaveSystemRegistry(
    private val plugin: JavaPlugin
) {
    private val idCounter = AtomicInteger(0)
    private val booted = AtomicBoolean(false)

    /** Generic entity event system definitions (for ANY EcsEvent type). */
    private val entityEventSystems = ConcurrentHashMap<String, EntityEventSystemDefinition<*, *>>()

    /** Generic world event system definitions (for ANY EcsEvent type). */
    private val worldEventSystems = ConcurrentHashMap<String, WorldEventSystemDefinition<*, *>>()

    /** Custom factories for specific event types (e.g., Damage with enriched context). */
    private val entitySystemFactories = ConcurrentHashMap<Class<*>, EntityEventSystemFactory<*, *>>()

    /** Custom factories for world event types. */
    private val worldSystemFactories = ConcurrentHashMap<Class<*>, WorldEventSystemFactory<*, *>>()

    /** Tick system definitions (backward compatible). */
    private val tickSystems = ConcurrentHashMap<String, TickSystemDefinition>()

    /** Damage system definitions (backward compatible). */
    private val damageSystems = ConcurrentHashMap<String, DamageSystemDefinition>()

    // Legacy handler support
    private val legacyDamageHandlers = ConcurrentHashMap<String, DamageHandler>()
    private val legacyTickHandlers = ConcurrentHashMap<String, TickHandler>()

    // Active adapters for legacy handlers
    private var legacyDamageAdapter: HexweaveDamageAdapter? = null
    private var legacyTickAdapter: HexweaveTickAdapter? = null

    /**
     * Generates a unique ID for auto-generated handlers.
     */
    fun nextId(prefix: String): String = "$prefix-${idCounter.incrementAndGet()}"

    /**
     * Registers a generic entity event system definition.
     *
     * This allows defining handlers for ANY EcsEvent type without
     * requiring specific implementation classes.
     */
    @PublishedApi
    internal fun <STORE, EVENT : EcsEvent> registerEntityEventSystem(
        definition: EntityEventSystemDefinition<STORE, EVENT>
    ) {
        entityEventSystems[definition.id] = definition
    }

    /**
     * Registers a generic world event system definition.
     */
    @PublishedApi
    internal fun <STORE, EVENT : EcsEvent> registerWorldEventSystem(
        definition: WorldEventSystemDefinition<STORE, EVENT>
    ) {
        worldEventSystems[definition.id] = definition
    }

    /**
     * Registers a custom factory for a specific entity event type.
     *
     * Use this when an event type needs specialized context (like Damage
     * with player stats and buff access via HexweaveDamageContext).
     */
    fun <STORE, EVENT : EcsEvent> registerEntitySystemFactory(
        eventClass: Class<EVENT>,
        factory: EntityEventSystemFactory<STORE, EVENT>
    ) {
        entitySystemFactories[eventClass] = factory
    }

    /**
     * Registers a custom factory for a specific world event type.
     */
    fun <STORE, EVENT : EcsEvent> registerWorldSystemFactory(
        eventClass: Class<EVENT>,
        factory: WorldEventSystemFactory<STORE, EVENT>
    ) {
        worldSystemFactories[eventClass] = factory
    }

    /**
     * Registers a tick system definition.
     */
    fun registerTickSystem(definition: TickSystemDefinition) {
        tickSystems[definition.id] = definition
    }

    /**
     * Registers a damage system definition.
     */
    fun registerDamageSystem(definition: DamageSystemDefinition) {
        damageSystems[definition.id] = definition
    }

    /**
     * Registers a legacy damage handler.
     * @deprecated Use registerDamageSystem instead
     */
    fun addDamageHandler(handler: DamageHandler) {
        legacyDamageHandlers[handler.id] = handler
        legacyDamageAdapter?.refresh(sortedLegacyDamageHandlers())
    }

    /**
     * Registers a legacy tick handler.
     * @deprecated Use registerTickSystem instead
     */
    fun addTickHandler(handler: TickHandler) {
        legacyTickHandlers[handler.id] = handler
        legacyTickAdapter?.refresh(sortedLegacyTickHandlers())
    }

    /**
     * Removes a system/handler by ID from all registries.
     */
    fun remove(id: String) {
        // Generic systems
        entityEventSystems.remove(id)
        worldEventSystems.remove(id)

        // Specialized systems
        tickSystems.remove(id)
        damageSystems.remove(id)

        // Legacy handlers
        legacyDamageHandlers.remove(id)?.let {
            legacyDamageAdapter?.refresh(sortedLegacyDamageHandlers())
        }
        legacyTickHandlers.remove(id)?.let {
            legacyTickAdapter?.refresh(sortedLegacyTickHandlers())
        }
    }

    private fun sortedLegacyDamageHandlers() = legacyDamageHandlers.values.sortedBy { it.priority }
    private fun sortedLegacyTickHandlers() = legacyTickHandlers.values.sortedBy { it.priority }

    /**
     * Bootstraps all system adapters and registers them with the ECS.
     */
    fun boot(scope: HexweaveScope) {
        if (booted.getAndSet(true)) {
            plugin.logger.atWarning().log("HexweaveSystemRegistry.boot() called multiple times")
            return
        }

        // =====================================================================
        // Generic Entity Event Systems
        // =====================================================================
        for (definition in entityEventSystems.values.sortedBy { it.priority }) {
            val factory = entitySystemFactories[definition.eventClass]
            val system = if (factory != null) {
                factory.createUnchecked(plugin, scope, definition)
            } else {
                createDefaultEntityEventSystem(plugin, definition)
            }
            plugin.entityStoreRegistry.registerSystem(system)
            plugin.logger.atInfo().log("Registered entity event system: ${definition.id}")
        }

        // =====================================================================
        // Generic World Event Systems
        // =====================================================================
        for (definition in worldEventSystems.values.sortedBy { it.priority }) {
            val factory = worldSystemFactories[definition.eventClass]
            val system = if (factory != null) {
                factory.createUnchecked(plugin, scope, definition)
            } else {
                createDefaultWorldEventSystem(plugin, definition)
            }
            plugin.entityStoreRegistry.registerSystem(system)
            plugin.logger.atInfo().log("Registered world event system: ${definition.id}")
        }

        // =====================================================================
        // Specialized Tick Systems (backward compatible)
        // =====================================================================
        for (definition in tickSystems.values.sortedBy { it.priority }) {
            val system = DynamicTickSystem(plugin, definition)
            plugin.entityStoreRegistry.registerSystem(system)
            plugin.logger.atInfo().log("Registered tick system: ${definition.id}")
        }

        // =====================================================================
        // Specialized Damage Systems (backward compatible)
        // =====================================================================
        for (definition in damageSystems.values.sortedBy { it.priority }) {
            val system = DynamicDamageSystem(plugin, scope, definition)
            plugin.entityStoreRegistry.registerSystem(system)
            plugin.logger.atInfo().log("Registered damage system: ${definition.id}")
        }

        // =====================================================================
        // Legacy Adapters (backward compatible)
        // =====================================================================
        if (legacyDamageHandlers.isNotEmpty()) {
            val adapter = HexweaveDamageAdapter(plugin, scope, sortedLegacyDamageHandlers())
            plugin.entityStoreRegistry.registerSystem(adapter)
            legacyDamageAdapter = adapter
            plugin.logger.atInfo().log("Registered legacy damage adapter with ${legacyDamageHandlers.size} handlers")
        }

        if (legacyTickHandlers.isNotEmpty()) {
            val adapter = HexweaveTickAdapter(plugin, sortedLegacyTickHandlers())
            plugin.entityStoreRegistry.registerSystem(adapter)
            legacyTickAdapter = adapter
            plugin.logger.atInfo().log("Registered legacy tick adapter with ${legacyTickHandlers.size} handlers")
        }
    }

    /**
     * Creates a default EntityEventSystem for a generic definition.
     */
    @Suppress("UNCHECKED_CAST")
    private fun createDefaultEntityEventSystem(
        plugin: JavaPlugin,
        definition: EntityEventSystemDefinition<*, *>
    ): EntityEventSystem<EntityStore, *> {
        val def = definition as EntityEventSystemDefinition<EntityStore, EcsEvent>
        return object : DynamicEntityEventSystem<EntityStore, EcsEvent>(plugin, def) {
            override fun defaultQuery() = AllLegacyLivingEntityTypesQuery.INSTANCE
        }
    }

    /**
     * Creates a default WorldEventSystem for a generic definition.
     */
    @Suppress("UNCHECKED_CAST")
    private fun createDefaultWorldEventSystem(
        plugin: JavaPlugin,
        definition: WorldEventSystemDefinition<*, *>
    ): WorldEventSystem<EntityStore, *> {
        val def = definition as WorldEventSystemDefinition<EntityStore, EcsEvent>
        return object : DynamicWorldEventSystem<EntityStore, EcsEvent>(plugin, def) {}
    }

    /**
     * Shuts down and cleans up all registered systems.
     */
    fun shutdown() {
        // Generic systems
        entityEventSystems.clear()
        worldEventSystems.clear()
        entitySystemFactories.clear()
        worldSystemFactories.clear()

        // Specialized systems
        tickSystems.clear()
        damageSystems.clear()

        // Legacy
        legacyDamageAdapter = null
        legacyTickAdapter = null
        legacyDamageHandlers.clear()
        legacyTickHandlers.clear()

        booted.set(false)
    }
}
