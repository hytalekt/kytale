package aster.amo.hexweave.dsl.systems

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.dsl.mechanics.HexweaveDamageContext
import aster.amo.hexweave.dsl.mechanics.HexweaveDamageScope
import aster.amo.hexweave.internal.HexweaveScope
import aster.amo.hexweave.internal.system.DamageHandler
import aster.amo.hexweave.internal.system.TickContext
import aster.amo.hexweave.internal.system.TickHandler
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EcsEvent
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Entry point for Hexweave ECS system DSL.
 *
 * Provides a unified way to define arbitrary systems:
 *
 * ## Generic Event Systems (Recommended)
 * - [entityEventSystem] - for ANY EntityEventSystem type
 * - [worldEventSystem] - for ANY WorldEventSystem type
 *
 * ## Specialized Systems (Backward Compatible)
 * - [tickSystem] - for EntityTickingSystem (query, dependencies, tick interval)
 * - [damageSystem] - for DamageEventSystem (query, dependencies, filter)
 *
 * ## Legacy DSL (Deprecated)
 * - [damage] - older damage handler DSL
 * - [entityTick] - older tick handler DSL
 *
 * Example:
 * ```kotlin
 * systems {
 *     // Generic EntityEventSystem for ANY event type
 *     entityEventSystem<EntityStore, PlaceBlockEvent>("block-handler") {
 *         filter { !it.isCancelled }
 *         dependencies { before<SomeOtherSystem>() }
 *         onEvent {
 *             // EntityEventContext: index, chunk, store, commandBuffer, event
 *         }
 *     }
 *
 *     // Generic WorldEventSystem
 *     worldEventSystem<EntityStore, MoonPhaseChangeEvent>("moon-handler") {
 *         onEvent {
 *             // WorldEventContext: store, commandBuffer, event
 *         }
 *     }
 *
 *     // Specialized tick system (backward compatible)
 *     tickSystem("stamina-drain") {
 *         query { ArchetypeQuery.builder<EntityStore>().require(Velocity.getComponentType()).build() }
 *         every = 20
 *         dependencies { after<SomeSystem>() }
 *         onTick {
 *             // TickContext available
 *         }
 *     }
 *
 *     // Specialized damage system (backward compatible)
 *     damageSystem("fall-handler") {
 *         filter { it.cause == DamageCause.FALL }
 *         dependencies { before<DamageSystems.ApplyDamage>() }
 *         onDamage {
 *             cancelDamage()
 *         }
 *     }
 * }
 * ```
 */
@HexweaveDsl
class HexweaveSystemsScope internal constructor(
    private val plugin: JavaPlugin,
    @PublishedApi internal val scope: HexweaveScope
) {
    // =========================================================================
    // Generic Event System DSL (for ANY EcsEvent type)
    // =========================================================================

    /**
     * Defines an [EntityEventSystem] for ANY [EcsEvent] type.
     *
     * This is the recommended way to define event systems as it works with
     * any event type without requiring specific support in Hexweave.
     *
     * Example:
     * ```kotlin
     * entityEventSystem<EntityStore, PlaceBlockEvent>("block-handler") {
     *     filter { !it.isCancelled }
     *     dependencies { before<SomeOtherSystem>() }
     *     onEvent {
     *         // EntityEventContext: index, chunk, store, commandBuffer, event
     *         val component = commandBuffer.get<SomeComponent>(chunk, index)
     *     }
     * }
     * ```
     *
     * @param STORE the entity store type (typically EntityStore)
     * @param EVENT the ECS event type to handle
     * @param id unique identifier for this system
     * @param block builder for configuring the system
     */
    inline fun <STORE, reified EVENT : EcsEvent> entityEventSystem(
        id: String,
        block: EntityEventSystemBuilder<STORE, EVENT>.() -> Unit
    ) {
        val builder = EntityEventSystemBuilder<STORE, EVENT>(id, EVENT::class.java)
        builder.apply(block)
        scope.systems.registerEntityEventSystem(builder.build())
    }

    /**
     * Defines a [WorldEventSystem] for ANY [EcsEvent] type.
     *
     * World event systems handle events at the world level, without
     * entity-specific context. Use this for events that affect the
     * entire world rather than specific entities.
     *
     * Example:
     * ```kotlin
     * worldEventSystem<EntityStore, MoonPhaseChangeEvent>("moon-handler") {
     *     onEvent {
     *         // WorldEventContext: store, commandBuffer, event
     *         logger.info { "Moon phase: ${event.newMoonPhase}" }
     *     }
     * }
     * ```
     *
     * @param STORE the entity store type (typically EntityStore)
     * @param EVENT the ECS event type to handle
     * @param id unique identifier for this system
     * @param block builder for configuring the system
     */
    inline fun <STORE, reified EVENT : EcsEvent> worldEventSystem(
        id: String,
        block: WorldEventSystemBuilder<STORE, EVENT>.() -> Unit
    ) {
        val builder = WorldEventSystemBuilder<STORE, EVENT>(id, EVENT::class.java)
        builder.apply(block)
        scope.systems.registerWorldEventSystem(builder.build())
    }

    // =========================================================================
    // Specialized System DSL (backward compatible)
    // =========================================================================

    /**
     * Defines a tick-based system (EntityTickingSystem).
     *
     * Example:
     * ```kotlin
     * tickSystem("stamina-drain") {
     *     query { ArchetypeQuery.builder<EntityStore>().require(Velocity.getComponentType()).build() }
     *     every = 20
     *     dependencies { after<SomeSystem>() }
     *     onTick {
     *         // TickContext available: deltaTime, tickIndex, index, chunk, store, commandBuffer
     *     }
     * }
     * ```
     */
    fun tickSystem(id: String, block: TickSystemBuilder.() -> Unit) {
        val builder = TickSystemBuilder(id)
        builder.apply(block)
        scope.systems.registerTickSystem(builder.build())
    }

    /**
     * Defines a damage event system (DamageEventSystem).
     *
     * Example:
     * ```kotlin
     * damageSystem("fall-handler") {
     *     filter { it.cause == DamageCause.FALL }
     *     dependencies { before<DamageSystems.ApplyDamage>() }
     *     onDamage {
     *         // HexweaveDamageContext available
     *         cancelDamage()
     *         playerRef?.sendMessage(Message.raw("Fall damage prevented!"))
     *     }
     * }
     * ```
     */
    fun damageSystem(id: String, block: DamageSystemBuilder.() -> Unit) {
        val builder = DamageSystemBuilder(id)
        builder.apply(block)
        scope.systems.registerDamageSystem(builder.build())
    }

    // =========================================================================
    // Legacy DSL (for backward compatibility)
    // =========================================================================

    /**
     * Gives access to the legacy damage system DSL.
     * @deprecated Use [damageSystem] instead for full control over query/dependencies.
     */
    fun damage(block: HexweaveDamageScope.() -> Unit) {
        HexweaveDamageScope(plugin, scope).apply(block)
    }

    /**
     * Convenience for `damage { fall { ... } }`.
     * @deprecated Use [damageSystem] instead.
     */
    fun fallDamage(priority: Int = 0, handler: HexweaveDamageContext.() -> Unit) {
        damage {
            fall(priority, handler)
        }
    }

    /**
     * Convenience for filtering by cause.
     * @deprecated Use [damageSystem] with filter instead.
     */
    fun damageCause(
        cause: DamageCause,
        priority: Int = 0,
        handler: HexweaveDamageContext.() -> Unit
    ) {
        damage {
            cause(cause, priority, handler)
        }
    }

    /**
     * Registers a legacy per-entity tick handler.
     * @deprecated Use [tickSystem] instead for full control over query/dependencies.
     */
    fun entityTick(
        id: String,
        block: EntityTickBuilder.() -> Unit
    ) {
        val builder = EntityTickBuilder(id)
        builder.apply(block)
        scope.systems.addTickHandler(builder.build())
    }
}

/**
 * Legacy builder for entity tick handlers.
 * @deprecated Use [TickSystemBuilder] instead.
 */
@HexweaveDsl
class EntityTickBuilder(private val id: String) {
    /** The query that determines which entities this handler processes. */
    var query: Query<EntityStore>? = null

    /** Priority for execution ordering (lower = earlier). */
    var priority: Int = 0

    /** Tick interval - handler runs every N ticks. Default is 1 (every tick). */
    var every: Int = 1

    private var handlerBlock: (TickContext.() -> Unit)? = null

    /**
     * Defines the handler logic that runs for each matching entity.
     */
    fun handle(block: TickContext.() -> Unit) {
        handlerBlock = block
    }

    internal fun build(): TickHandler {
        val q = query ?: throw IllegalStateException("EntityTick '$id' requires a query")
        val h = handlerBlock ?: throw IllegalStateException("EntityTick '$id' requires a handler")

        return TickHandler(
            id = id,
            priority = priority,
            query = q,
            tickInterval = every,
            handler = h
        )
    }
}
