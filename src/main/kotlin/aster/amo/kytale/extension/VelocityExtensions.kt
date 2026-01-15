package aster.amo.kytale.extension

import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.protocol.ChangeVelocityType
import com.hypixel.hytale.server.core.modules.physics.component.Velocity
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig

/**
 * Extension functions for Hytale Velocity component manipulation.
 *
 * Simplifies the verbose velocity API for common movement patterns.
 */

private val defaultVelocityConfig = VelocityConfig()

/**
 * Sets the velocity to the specified vector.
 *
 * Before: velocity.addInstruction(newVel, VelocityConfig(), ChangeVelocityType.Set)
 * After:  velocity.set(newVel)
 *
 * @param velocity the new velocity vector
 * @param config optional velocity config (uses default if not specified)
 */
fun Velocity.set(velocity: Vector3d, config: VelocityConfig = defaultVelocityConfig) {
    addInstruction(velocity, config, ChangeVelocityType.Set)
}

/**
 * Adds to the current velocity.
 *
 * Before: velocity.addInstruction(delta, VelocityConfig(), ChangeVelocityType.Add)
 * After:  velocity.add(delta)
 *
 * @param delta the velocity to add
 * @param config optional velocity config
 */
fun Velocity.add(delta: Vector3d, config: VelocityConfig = defaultVelocityConfig) {
    addInstruction(delta, config, ChangeVelocityType.Add)
}

/**
 * Sets only the horizontal velocity components.
 *
 * @param x the X velocity
 * @param z the Z velocity
 * @param config optional velocity config
 */
fun Velocity.setHorizontal(x: Double, z: Double, config: VelocityConfig = defaultVelocityConfig) {
    addInstruction(Vector3d(x, velocity.y, z), config, ChangeVelocityType.Set)
}

/**
 * Sets the Y velocity component while preserving horizontal velocity.
 *
 * @param y the new Y velocity
 * @param config optional velocity config
 */
fun Velocity.setVertical(y: Double, config: VelocityConfig = defaultVelocityConfig) {
    addInstruction(Vector3d(velocity.x, y, velocity.z), config, ChangeVelocityType.Set)
}

/**
 * Performs a dash in the given direction.
 *
 * Before:
 * ```kotlin
 * val dashVelocity = (dir * magnitude).withY(upBoost)
 * velocity.addInstruction(dashVelocity, VelocityConfig(), ChangeVelocityType.Set)
 * ```
 *
 * After:
 * ```kotlin
 * velocity.dash(dir, magnitude, upBoost)
 * ```
 *
 * @param direction the horizontal direction to dash (will be normalized)
 * @param magnitude the dash speed
 * @param upBoost optional upward velocity component (default 0.2)
 * @param config optional velocity config
 */
fun Velocity.dash(
    direction: Vector3d,
    magnitude: Double,
    upBoost: Double = 0.2,
    config: VelocityConfig = defaultVelocityConfig
) {
    val normalizedDir = direction.horizontalNormalized()
    val dashVelocity = (normalizedDir * magnitude).withY(upBoost)
    addInstruction(dashVelocity, config, ChangeVelocityType.Set)
}

/**
 * Launches the entity upward.
 *
 * @param force the upward force
 * @param preserveHorizontal whether to preserve horizontal velocity (default true)
 * @param config optional velocity config
 */
fun Velocity.launch(
    force: Double,
    preserveHorizontal: Boolean = true,
    config: VelocityConfig = defaultVelocityConfig
) {
    val launchVelocity = if (preserveHorizontal) {
        Vector3d(velocity.x, force, velocity.z)
    } else {
        Vector3d(0.0, force, 0.0)
    }
    addInstruction(launchVelocity, config, ChangeVelocityType.Set)
}

/**
 * Performs a ground slam (rapid downward movement).
 *
 * Before:
 * ```kotlin
 * velocity.addInstruction(VECTOR_DOWN * magnitude, VelocityConfig(), ChangeVelocityType.Add)
 * ```
 *
 * After:
 * ```kotlin
 * velocity.slam(magnitude)
 * ```
 *
 * @param force the downward force (positive value)
 * @param cancelHorizontal whether to zero out horizontal velocity (default false)
 * @param config optional velocity config
 */
fun Velocity.slam(
    force: Double,
    cancelHorizontal: Boolean = false,
    config: VelocityConfig = defaultVelocityConfig
) {
    val slamVelocity = if (cancelHorizontal) {
        Vector3d(0.0, -force, 0.0)
    } else {
        Vector3d(velocity.x, -force, velocity.z)
    }
    addInstruction(slamVelocity, config, ChangeVelocityType.Add)
}

/**
 * Applies an impulse in a direction with specified magnitude.
 *
 * @param direction the direction of the impulse (will be normalized)
 * @param magnitude the strength of the impulse
 * @param config optional velocity config
 */
fun Velocity.impulse(
    direction: Vector3d,
    magnitude: Double,
    config: VelocityConfig = defaultVelocityConfig
) {
    val impulseVelocity = direction.normalized() * magnitude
    addInstruction(impulseVelocity, config, ChangeVelocityType.Add)
}

/**
 * Applies a knockback effect (typically from taking damage).
 *
 * @param fromPosition the position the knockback originates from
 * @param toPosition the position being knocked back
 * @param horizontalForce the horizontal knockback force
 * @param verticalForce the vertical knockback force (default 0.4)
 * @param config optional velocity config
 */
fun Velocity.knockback(
    fromPosition: Vector3d,
    toPosition: Vector3d,
    horizontalForce: Double,
    verticalForce: Double = 0.4,
    config: VelocityConfig = defaultVelocityConfig
) {
    val direction = (toPosition - fromPosition).horizontalNormalized()
    val knockbackVelocity = (direction * horizontalForce).withY(verticalForce)
    addInstruction(knockbackVelocity, config, ChangeVelocityType.Add)
}

/**
 * Gets the current horizontal speed.
 *
 * @return the horizontal speed (ignoring Y component)
 */
fun Velocity.horizontalSpeed(): Double = velocity.horizontalLength()

/**
 * Gets the current total speed.
 *
 * @return the total velocity magnitude
 */
fun Velocity.speed(): Double = velocity.length()

/**
 * Checks if the entity is moving horizontally.
 *
 * @param threshold minimum speed to consider as moving (default 0.01)
 * @return true if horizontal speed exceeds threshold
 */
fun Velocity.isMovingHorizontally(threshold: Double = 0.01): Boolean =
    horizontalSpeed() > threshold

/**
 * Checks if the entity is moving upward.
 *
 * @param threshold minimum upward speed (default 0.01)
 * @return true if moving upward
 */
fun Velocity.isMovingUp(threshold: Double = 0.01): Boolean = velocity.y > threshold

/**
 * Checks if the entity is moving downward (falling).
 *
 * @param threshold minimum downward speed (default 0.01)
 * @return true if moving downward
 */
fun Velocity.isMovingDown(threshold: Double = 0.01): Boolean = velocity.y < -threshold

/**
 * Gets the horizontal movement direction.
 *
 * @return normalized horizontal direction, or zero vector if not moving
 */
fun Velocity.horizontalDirection(): Vector3d = velocity.horizontalNormalized()
