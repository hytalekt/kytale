package aster.amo.hexweave.dsl.systems

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.dsl.mechanics.HexweaveDamageContext
import aster.amo.hexweave.internal.system.DamageSystemDefinition
import aster.amo.hexweave.internal.system.TickContext
import aster.amo.hexweave.internal.system.TickSystemDefinition
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Builder for tick-based systems (EntityTickingSystem).
 *
 * Example:
 * ```kotlin
 * tickSystem("stamina-drain") {
 *     query { ArchetypeQuery.builder<EntityStore>().require(Velocity.getComponentType()).build() }
 *     every = 20
 *     dependencies { after<SomeSystem>() }
 *     onTick {
 *         // TickContext available here
 *         val vel = commandBuffer.get<Velocity>(chunk, index)
 *     }
 * }
 * ```
 */
@HexweaveDsl
class TickSystemBuilder(private val id: String) {
    /** Priority for execution ordering (lower = earlier). */
    var priority: Int = 0

    /** Tick interval - handler runs every N ticks. Default is 1 (every tick). */
    var every: Int = 1

    private var queryProvider: (() -> Query<EntityStore>)? = null
    @PublishedApi internal var deps: Set<Dependency<EntityStore?>> = emptySet()
    private var handlerBlock: (TickContext.() -> Unit)? = null

    /**
     * Sets the query that determines which entities this system processes.
     */
    fun query(block: () -> Query<EntityStore>) {
        queryProvider = block
    }

    /**
     * Declares system dependencies for execution ordering.
     */
    inline fun dependencies(block: DependencyBuilder.() -> Unit) {
        deps = DependencyBuilder().apply(block).build()
    }

    /**
     * Defines the handler logic that runs for each matching entity.
     */
    fun onTick(block: TickContext.() -> Unit) {
        handlerBlock = block
    }

    internal fun build(): TickSystemDefinition {
        val q = queryProvider?.invoke()
            ?: throw IllegalStateException("TickSystem '$id' requires a query")
        val h = handlerBlock
            ?: throw IllegalStateException("TickSystem '$id' requires onTick handler")

        return TickSystemDefinition(
            id = id,
            priority = priority,
            query = q,
            dependencies = deps,
            tickInterval = every,
            handler = h
        )
    }
}

/**
 * Builder for damage event systems (DamageEventSystem).
 *
 * Example:
 * ```kotlin
 * damageSystem("fall-handler") {
 *     filter { it.cause == DamageCause.FALL }
 *     dependencies { before<DamageSystems.ApplyDamage>() }
 *     onDamage {
 *         cancelDamage()
 *         playerRef?.sendMessage(Message.raw("Fall damage prevented!"))
 *     }
 * }
 * ```
 */
@HexweaveDsl
class DamageSystemBuilder(private val id: String) {
    /** Priority for execution ordering (lower = earlier). */
    var priority: Int = 0

    private var queryProvider: (() -> Query<EntityStore>)? = null
    @PublishedApi internal var deps: Set<Dependency<EntityStore?>> = emptySet()
    private var filterPredicate: (Damage) -> Boolean = { true }
    private var handlerBlock: (HexweaveDamageContext.() -> Unit)? = null

    /**
     * Sets a custom query. If not specified, uses AllLegacyLivingEntityTypesQuery.
     */
    fun query(block: () -> Query<EntityStore>) {
        queryProvider = block
    }

    /**
     * Declares system dependencies for execution ordering.
     */
    inline fun dependencies(block: DependencyBuilder.() -> Unit) {
        deps = DependencyBuilder().apply(block).build()
    }

    /**
     * Filters which damage events this handler processes.
     */
    fun filter(predicate: (Damage) -> Boolean) {
        filterPredicate = predicate
    }

    /**
     * Defines the handler logic for damage events.
     */
    fun onDamage(block: HexweaveDamageContext.() -> Unit) {
        handlerBlock = block
    }

    internal fun build(): DamageSystemDefinition {
        val h = handlerBlock
            ?: throw IllegalStateException("DamageSystem '$id' requires onDamage handler")

        return DamageSystemDefinition(
            id = id,
            priority = priority,
            query = queryProvider?.invoke(),
            dependencies = deps,
            filter = filterPredicate,
            handler = h
        )
    }
}
