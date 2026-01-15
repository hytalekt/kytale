package aster.amo.kytale.extension

import aster.amo.kytale.coroutines.HytaleDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

/**
 * Extension functions for Hytale World operations.
 *
 * A World is a single game world within the Universe containing
 * terrain, entities, and players currently in that world.
 *
 * Note: Direct World extensions require the World type from
 * com.hypixel.hytale.server.core.universe when available.
 */

/**
 * Creates a coroutine dispatcher for a world's execution context.
 *
 * @param executor the executor for the world's thread
 * @return a dispatcher bound to the world's execution context
 */
fun worldDispatcher(executor: Executor): CoroutineDispatcher {
    return executor.asCoroutineDispatcher()
}

/**
 * Executes a block on the main server thread.
 *
 * @param block the code to execute
 * @return the result of the block
 */
suspend fun <T> onServerThread(block: suspend () -> T): T {
    return withContext(HytaleDispatchers.Main) { block() }
}

/**
 * Data class representing a location in a world.
 *
 * A lightweight location container for coordinate operations.
 *
 * @property worldName the name of the world
 * @property x the X coordinate
 * @property y the Y coordinate
 * @property z the Z coordinate
 * @property yaw the yaw rotation (horizontal)
 * @property pitch the pitch rotation (vertical)
 */
data class Location(
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0f,
    val pitch: Float = 0f
) {
    /**
     * Creates a new location offset by the given amounts.
     *
     * @param dx the X offset
     * @param dy the Y offset
     * @param dz the Z offset
     * @return a new location with the applied offset
     */
    fun offset(dx: Double, dy: Double, dz: Double): Location {
        return copy(x = x + dx, y = y + dy, z = z + dz)
    }

    /**
     * Gets the block coordinates of this location.
     *
     * @return a triple of (blockX, blockY, blockZ)
     */
    fun toBlockCoordinates(): Triple<Int, Int, Int> {
        return Triple(x.toInt(), y.toInt(), z.toInt())
    }

    /**
     * Gets the chunk coordinates of this location.
     *
     * @return a pair of (chunkX, chunkZ)
     */
    fun toChunkCoordinates(): Pair<Int, Int> {
        return Pair(x.toInt() shr 4, z.toInt() shr 4)
    }

    /**
     * Calculates the distance to another location.
     *
     * @param other the other location
     * @return the distance between the two locations
     */
    fun distanceTo(other: Location): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    /**
     * Calculates the squared distance to another location.
     *
     * More efficient than [distanceTo] when comparing distances.
     *
     * @param other the other location
     * @return the squared distance
     */
    fun distanceSquaredTo(other: Location): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return dx * dx + dy * dy + dz * dz
    }

    /**
     * Checks if this location is within range of another.
     *
     * @param other the other location
     * @param range the maximum distance
     * @return true if within range
     */
    fun isWithinRange(other: Location, range: Double): Boolean {
        return distanceSquaredTo(other) <= range * range
    }
}

/**
 * Creates a location from coordinates.
 *
 * @param worldName the world name
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param z the Z coordinate
 * @return a new Location instance
 */
fun location(worldName: String, x: Double, y: Double, z: Double): Location {
    return Location(worldName, x, y, z)
}

/**
 * Creates a location from integer coordinates.
 *
 * @param worldName the world name
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param z the Z coordinate
 * @return a new Location instance
 */
fun location(worldName: String, x: Int, y: Int, z: Int): Location {
    return Location(worldName, x.toDouble(), y.toDouble(), z.toDouble())
}

/**
 * Creates a location with rotation.
 *
 * @param worldName the world name
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param z the Z coordinate
 * @param yaw the horizontal rotation
 * @param pitch the vertical rotation
 * @return a new Location instance
 */
fun location(
    worldName: String,
    x: Double,
    y: Double,
    z: Double,
    yaw: Float,
    pitch: Float
): Location {
    return Location(worldName, x, y, z, yaw, pitch)
}
