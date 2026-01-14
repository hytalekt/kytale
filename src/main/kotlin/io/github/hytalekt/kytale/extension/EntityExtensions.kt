package io.github.hytalekt.kytale.extension

import com.hypixel.hytale.server.core.entity.Entity

/**
 * Extension functions for Entity objects.
 */

/**
 * Gets the entity's position as a triple
 */
val Entity.position: Triple<Double, Double, Double>
    get() = TODO("Get entity position")

/**
 * Gets the entity's X coordinate
 */
val Entity.x: Double
    get() = TODO("Get X")

/**
 * Gets the entity's Y coordinate
 */
val Entity.y: Double
    get() = TODO("Get Y")

/**
 * Gets the entity's Z coordinate
 */
val Entity.z: Double
    get() = TODO("Get Z")

/**
 * Teleports the entity to coordinates
 */
fun Entity.teleport(
    x: Double,
    y: Double,
    z: Double,
) {
    // TODO: Implement teleport
}

/**
 * Teleports the entity to another entity
 */
fun Entity.teleportTo(target: Entity) {
    // TODO: Implement teleport to entity
}

/**
 * Removes the entity from the world
 */
fun Entity.remove() {
    // TODO: Implement remove
}

/**
 * Checks if the entity is alive
 */
val Entity.isAlive: Boolean
    get() = TODO("Check if alive")

/**
 * Gets the distance to another entity
 */
fun Entity.distanceTo(other: Entity): Double {
    // TODO: Implement distance calculation
    return 0.0
}

/**
 * Gets the distance to coordinates
 */
fun Entity.distanceTo(
    x: Double,
    y: Double,
    z: Double,
): Double {
    // TODO: Implement distance calculation
    return 0.0
}

/**
 * Applies velocity to the entity
 */
fun Entity.applyVelocity(
    vx: Double,
    vy: Double,
    vz: Double,
) {
    // TODO: Implement velocity
}

/**
 * Applies knockback to the entity
 */
fun Entity.knockback(
    strength: Double,
    angle: Float,
) {
    // TODO: Implement knockback
}

/**
 * Gets entities within a radius
 */
fun Entity.getNearbyEntities(radius: Double): List<Entity> {
    // TODO: Implement nearby entities
    return emptyList()
}

/**
 * Sets a custom name for the entity
 */
fun Entity.setCustomName(
    name: String,
    visible: Boolean = true,
) {
    // TODO: Implement custom name
}

/**
 * Makes the entity face a location
 */
fun Entity.lookAt(
    x: Double,
    y: Double,
    z: Double,
) {
    // TODO: Implement look at
}

/**
 * Makes the entity face another entity
 */
fun Entity.lookAt(target: Entity) {
    // TODO: Implement look at entity
}
