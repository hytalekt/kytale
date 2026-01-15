package aster.amo.kytale.extension

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.ComponentAccessor
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Extension functions for dealing damage in Hytale.
 *
 * Simplifies the verbose damage API for common damage patterns.
 */

/**
 * Deals damage to an entity from another entity source.
 *
 * Before:
 * ```kotlin
 * val damage = Damage(
 *     Damage.EntitySource(sourceRef),
 *     DamageCause.PHYSICAL!!,
 *     40.0f
 * )
 * DamageSystems.executeDamage(targetRef, commandBuffer, damage)
 * ```
 *
 * After:
 * ```kotlin
 * targetRef.damage(commandBuffer, 40.0f, DamageCause.PHYSICAL!!, sourceRef)
 * ```
 *
 * @param commandBuffer the command buffer for the operation
 * @param amount the damage amount
 * @param cause the damage cause type
 * @param source the source entity that caused the damage
 */
fun Ref<EntityStore>.damage(
    commandBuffer: CommandBuffer<EntityStore>,
    amount: Float,
    cause: DamageCause,
    source: Ref<EntityStore>
) {
    val damage = Damage(Damage.EntitySource(source), cause, amount)
    DamageSystems.executeDamage(this, commandBuffer, damage)
}

/**
 * Deals physical damage to an entity.
 *
 * @param commandBuffer the command buffer
 * @param amount the damage amount
 * @param source the source entity
 */
fun Ref<EntityStore>.damagePhysical(
    commandBuffer: CommandBuffer<EntityStore>,
    amount: Float,
    source: Ref<EntityStore>
) {
    damage(commandBuffer, amount, DamageCause.PHYSICAL!!, source)
}

/**
 * Builder for creating damage with DSL syntax.
 */
class DamageBuilder {
    var amount: Float = 0f
    var cause: DamageCause = DamageCause.PHYSICAL!!
    var source: Ref<EntityStore>? = null

    @PublishedApi
    internal fun build(): Damage {
        val damageSource = source?.let { Damage.EntitySource(it) }
            ?: throw IllegalStateException("Damage source must be specified")
        return Damage(damageSource, cause, amount)
    }
}

/**
 * Deals damage using DSL syntax.
 *
 * Example:
 * ```kotlin
 * entityRef.damage(commandBuffer) {
 *     amount = 40.0f
 *     cause = DamageCause.PHYSICAL!!
 *     source = attackerRef
 * }
 * ```
 *
 * @param commandBuffer the command buffer
 * @param block the damage configuration
 */
inline fun Ref<EntityStore>.damage(
    commandBuffer: CommandBuffer<EntityStore>,
    block: DamageBuilder.() -> Unit
) {
    val builder = DamageBuilder().apply(block)
    DamageSystems.executeDamage(this, commandBuffer, builder.build())
}

/**
 * Deals damage to all entities in a list.
 *
 * @param entities the entities to damage
 * @param commandBuffer the command buffer
 * @param amount the damage amount
 * @param cause the damage cause
 * @param source the source entity
 * @param exclude entities to exclude from damage
 * @return the number of entities damaged
 */
fun damageAll(
    entities: List<Ref<EntityStore>>,
    commandBuffer: CommandBuffer<EntityStore>,
    amount: Float,
    cause: DamageCause,
    source: Ref<EntityStore>,
    exclude: Set<Ref<EntityStore>> = emptySet()
): Int {
    var count = 0
    for (entity in entities) {
        if (entity in exclude) continue
        if (entity == source) continue  // Don't damage self
        entity.damage(commandBuffer, amount, cause, source)
        count++
    }
    return count
}

/**
 * Deals AOE damage in a sphere using the entity store.
 *
 * @param store the component accessor (typically entityRef.store)
 * @param commandBuffer the command buffer
 * @param center the center position
 * @param radius the damage radius
 * @param amount the damage amount
 * @param cause the damage cause
 * @param source the source entity (will be excluded from damage)
 * @return the number of entities damaged
 */
fun damageInSphere(
    store: ComponentAccessor<EntityStore>,
    commandBuffer: CommandBuffer<EntityStore>,
    center: Vector3d,
    radius: Double,
    amount: Float,
    cause: DamageCause,
    source: Ref<EntityStore>
): Int {
    val entities = getEntitiesInSphere(center, radius, store)
    return damageAll(entities, commandBuffer, amount, cause, source, setOf(source))
}

/**
 * Deals AOE damage in a cylinder using the entity store.
 *
 * @param store the component accessor (typically entityRef.store)
 * @param commandBuffer the command buffer
 * @param center the center position
 * @param radius the horizontal radius
 * @param height the vertical height
 * @param amount the damage amount
 * @param cause the damage cause
 * @param source the source entity (will be excluded from damage)
 * @return the number of entities damaged
 */
fun damageInCylinder(
    store: ComponentAccessor<EntityStore>,
    commandBuffer: CommandBuffer<EntityStore>,
    center: Vector3d,
    radius: Double,
    height: Double,
    amount: Float,
    cause: DamageCause,
    source: Ref<EntityStore>
): Int {
    val entities = getEntitiesInCylinder(center, radius, height, store)
    return damageAll(entities, commandBuffer, amount, cause, source, setOf(source))
}

/**
 * Creates a damage instance for later use.
 *
 * @param amount the damage amount
 * @param cause the damage cause
 * @param source the source entity
 * @return the Damage instance
 */
fun createDamage(
    amount: Float,
    cause: DamageCause,
    source: Ref<EntityStore>
): Damage {
    return Damage(Damage.EntitySource(source), cause, amount)
}

/**
 * Creates physical damage.
 *
 * @param amount the damage amount
 * @param source the source entity
 * @return the Damage instance
 */
fun physicalDamage(amount: Float, source: Ref<EntityStore>): Damage {
    return createDamage(amount, DamageCause.PHYSICAL!!, source)
}
