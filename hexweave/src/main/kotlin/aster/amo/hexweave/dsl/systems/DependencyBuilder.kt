package aster.amo.hexweave.dsl.systems

import aster.amo.hexweave.dsl.HexweaveDsl
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.dependency.Order
import com.hypixel.hytale.component.dependency.SystemDependency
import com.hypixel.hytale.component.system.ISystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * DSL builder for declaring system dependencies.
 *
 * Example:
 * ```kotlin
 * dependencies {
 *     before<DamageSystems.ApplyDamage>()
 *     after<SomeOtherSystem>()
 * }
 * ```
 */
@HexweaveDsl
class DependencyBuilder @PublishedApi internal constructor() {
    @PublishedApi
    internal val deps = mutableSetOf<Dependency<EntityStore?>>()

    /**
     * Declares this system should run BEFORE the specified system.
     */
    inline fun <reified T : ISystem<*>> before() {
        @Suppress("UNCHECKED_CAST")
        deps.add(SystemDependency(Order.BEFORE, T::class.java as Class<out ISystem<EntityStore?>>))
    }

    /**
     * Declares this system should run AFTER the specified system.
     */
    inline fun <reified T : ISystem<*>> after() {
        @Suppress("UNCHECKED_CAST")
        deps.add(SystemDependency(Order.AFTER, T::class.java as Class<out ISystem<EntityStore?>>))
    }

    /**
     * Adds a raw dependency directly.
     */
    fun add(dependency: Dependency<EntityStore?>) {
        deps.add(dependency)
    }

    @PublishedApi
    internal fun build(): Set<Dependency<EntityStore?>> = deps.toSet()
}
