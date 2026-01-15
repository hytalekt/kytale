package aster.amo.kytale.extension

import com.hypixel.hytale.component.ComponentAccessor
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.core.util.TargetUtil

/**
 * Extension functions for entity targeting and spatial queries.
 *
 * Simplifies the verbose TargetUtil API for common targeting patterns.
 */

/**
 * Gets all entities within a cylinder around a center point.
 *
 * Before:
 * ```kotlin
 * val entities = TargetUtil.getAllEntitiesInCylinder(center, radius, height, entityRef.store)
 * ```
 *
 * After:
 * ```kotlin
 * val entities = getEntitiesInCylinder(center, radius, height, entityRef.store)
 * ```
 *
 * @param center the center position of the cylinder
 * @param radius the horizontal radius
 * @param height the vertical height
 * @param store the component accessor (typically entityRef.store)
 * @return list of entity references within the cylinder
 */
fun getEntitiesInCylinder(
    center: Vector3d,
    radius: Double,
    height: Double,
    store: ComponentAccessor<EntityStore>
): List<Ref<EntityStore>> {
    return TargetUtil.getAllEntitiesInCylinder(center, radius, height, store)
}

/**
 * Gets all entities within a sphere around a center point.
 *
 * @param center the center position
 * @param radius the sphere radius
 * @param store the component accessor (typically entityRef.store)
 * @return list of entity references within the sphere
 */
fun getEntitiesInSphere(
    center: Vector3d,
    radius: Double,
    store: ComponentAccessor<EntityStore>
): List<Ref<EntityStore>> {
    return TargetUtil.getAllEntitiesInSphere(center, radius, store)
}

/**
 * Gets all entities within a box defined by min and max corners.
 *
 * @param min the minimum corner
 * @param max the maximum corner
 * @param store the component accessor (typically entityRef.store)
 * @return list of entity references within the box
 */
fun getEntitiesInBox(
    min: Vector3d,
    max: Vector3d,
    store: ComponentAccessor<EntityStore>
): List<Ref<EntityStore>> {
    return TargetUtil.getAllEntitiesInBox(min, max, store)
}

/**
 * Gets all entities near this player within a sphere.
 *
 * @param radius the search radius
 * @return list of entity references near the player, or empty if position unavailable
 */
fun PlayerRef.getNearbyEntities(radius: Double): List<Ref<EntityStore>> {
    val pos = position ?: return emptyList()
    val store = reference?.store ?: return emptyList()
    return getEntitiesInSphere(pos, radius, store)
}

/**
 * Gets all entities near this player within a cylinder.
 *
 * @param radius the horizontal radius
 * @param height the vertical height
 * @return list of entity references near the player
 */
fun PlayerRef.getNearbyEntitiesInCylinder(
    radius: Double,
    height: Double
): List<Ref<EntityStore>> {
    val pos = position ?: return emptyList()
    val store = reference?.store ?: return emptyList()
    return getEntitiesInCylinder(pos, radius, height, store)
}

/**
 * Excludes specific entity references from the list.
 *
 * @param refs the entity references to exclude
 * @return filtered list without the specified entities
 */
fun List<Ref<EntityStore>>.excluding(
    vararg refs: Ref<EntityStore>
): List<Ref<EntityStore>> {
    val excludeSet = refs.toSet()
    return filter { it !in excludeSet }
}

/**
 * Excludes a single entity reference from the list.
 *
 * @param ref the entity reference to exclude
 * @return filtered list without the specified entity
 */
fun List<Ref<EntityStore>>.excluding(
    ref: Ref<EntityStore>?
): List<Ref<EntityStore>> {
    if (ref == null) return this
    return filter { it != ref }
}

/**
 * Excludes a player's entity from the list.
 *
 * @param player the player to exclude
 * @return filtered list without the player's entity
 */
fun List<Ref<EntityStore>>.excludingPlayer(
    player: PlayerRef
): List<Ref<EntityStore>> {
    val playerRef = player.reference ?: return this
    return filter { it != playerRef }
}

/**
 * Filters to only include entities within a maximum distance from a point.
 *
 * @param center the center point
 * @param maxDistance the maximum distance
 * @param getPosition function to get entity position
 * @return filtered list of entities within distance
 */
inline fun List<Ref<EntityStore>>.withinDistance(
    center: Vector3d,
    maxDistance: Double,
    crossinline getPosition: (Ref<EntityStore>) -> Vector3d?
): List<Ref<EntityStore>> {
    val maxDistSq = maxDistance * maxDistance
    return filter { entityRef ->
        val pos = getPosition(entityRef) ?: return@filter false
        center.distanceSquaredTo(pos) <= maxDistSq
    }
}

/**
 * Gets the closest entity to a point.
 *
 * @param center the center point
 * @param getPosition function to get entity position
 * @return the closest entity reference, or null if list is empty
 */
inline fun List<Ref<EntityStore>>.closest(
    center: Vector3d,
    crossinline getPosition: (Ref<EntityStore>) -> Vector3d?
): Ref<EntityStore>? {
    return minByOrNull { entityRef ->
        val pos = getPosition(entityRef) ?: return@minByOrNull Double.MAX_VALUE
        center.distanceSquaredTo(pos)
    }
}

/**
 * Sorts entities by distance from a point (nearest first).
 *
 * @param center the center point
 * @param getPosition function to get entity position
 * @return list sorted by distance (nearest first)
 */
inline fun List<Ref<EntityStore>>.sortedByDistance(
    center: Vector3d,
    crossinline getPosition: (Ref<EntityStore>) -> Vector3d?
): List<Ref<EntityStore>> {
    return sortedBy { entityRef ->
        val pos = getPosition(entityRef) ?: return@sortedBy Double.MAX_VALUE
        center.distanceSquaredTo(pos)
    }
}

/**
 * Performs an action on each entity in the list.
 *
 * @param action the action to perform
 */
inline fun List<Ref<EntityStore>>.forEachEntity(
    action: (Ref<EntityStore>) -> Unit
) {
    forEach(action)
}

/**
 * Performs an action on entities up to a maximum count.
 *
 * @param maxCount the maximum number of entities to process
 * @param action the action to perform
 * @return the number of entities processed
 */
inline fun List<Ref<EntityStore>>.forEachEntityLimited(
    maxCount: Int,
    action: (Ref<EntityStore>) -> Unit
): Int {
    var count = 0
    for (entity in this) {
        if (count >= maxCount) break
        action(entity)
        count++
    }
    return count
}
