package aster.amo.kytale.extension

import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.server.core.universe.PlayerRef

/**
 * Extension functions for PlayerRef operations.
 *
 * Provides convenient shortcuts for common player property access.
 * Note: More advanced entity/component operations should be done
 * directly in your mod where the full Hytale API is available.
 */

/**
 * Gets the player's current position.
 *
 * @return the position Vector3d, or null if not available
 */
val PlayerRef.position: Vector3d?
    get() = try {
        transform?.position
    } catch (e: Exception) {
        null
    }

/**
 * Gets the player's current world UUID.
 *
 * @return the world UUID, or null if not in a world
 */
val PlayerRef.currentWorldUuid: java.util.UUID?
    get() = worldUuid

/**
 * Checks if this player is in the same world as another.
 *
 * @param other the other player
 * @return true if both players are in the same world
 */
fun PlayerRef.isInSameWorld(other: PlayerRef): Boolean {
    val myWorld = worldUuid ?: return false
    val otherWorld = other.worldUuid ?: return false
    return myWorld == otherWorld
}

/**
 * Calculates the distance to another player.
 *
 * @param other the other player
 * @return the distance, or null if positions are unavailable
 */
fun PlayerRef.distanceTo(other: PlayerRef): Double? {
    val myPos = position ?: return null
    val otherPos = other.position ?: return null
    return myPos.distanceTo(otherPos)
}

/**
 * Checks if this player is within range of another.
 *
 * @param other the other player
 * @param range the maximum distance
 * @return true if within range, false if out of range or positions unavailable
 */
fun PlayerRef.isWithinRange(other: PlayerRef, range: Double): Boolean {
    val distance = distanceTo(other) ?: return false
    return distance <= range
}

/**
 * Safely executes a block if the player has a valid reference.
 *
 * @param block the block to execute
 */
inline fun PlayerRef.ifValid(block: PlayerRef.() -> Unit) {
    try {
        if (reference != null) {
            block()
        }
    } catch (e: Exception) {
    }
}

/**
 * Gets a property safely, returning null on any error.
 *
 * @param getter the property getter
 * @return the property value, or null if an error occurred
 */
inline fun <T> PlayerRef.safeGet(getter: PlayerRef.() -> T?): T? {
    return try {
        getter()
    } catch (e: Exception) {
        null
    }
}
