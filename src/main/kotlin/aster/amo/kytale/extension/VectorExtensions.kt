package aster.amo.kytale.extension

import com.hypixel.hytale.math.vector.Vector3d
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Extension functions for Hytale Vector3d operations.
 *
 * Provides Kotlin-idiomatic vector math operations including
 * operator overloads, factory functions, and common utilities.
 */

/**
 * Creates a Vector3d from double coordinates.
 *
 * @param x the X component
 * @param y the Y component
 * @param z the Z component
 * @return a new Vector3d
 */
fun vec3(x: Double, y: Double, z: Double): Vector3d = Vector3d(x, y, z)

/**
 * Creates a Vector3d from integer coordinates.
 *
 * @param x the X component
 * @param y the Y component
 * @param z the Z component
 * @return a new Vector3d
 */
fun vec3(x: Int, y: Int, z: Int): Vector3d = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

/**
 * Creates a Vector3d from float coordinates.
 *
 * @param x the X component
 * @param y the Y component
 * @param z the Z component
 * @return a new Vector3d
 */
fun vec3(x: Float, y: Float, z: Float): Vector3d = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

/** A zero vector (0, 0, 0). */
val VECTOR_ZERO: Vector3d get() = Vector3d(0.0, 0.0, 0.0)

/** An up vector (0, 1, 0). */
val VECTOR_UP: Vector3d get() = Vector3d(0.0, 1.0, 0.0)

/** A down vector (0, -1, 0). */
val VECTOR_DOWN: Vector3d get() = Vector3d(0.0, -1.0, 0.0)

/** A north vector (0, 0, -1). */
val VECTOR_NORTH: Vector3d get() = Vector3d(0.0, 0.0, -1.0)

/** A south vector (0, 0, 1). */
val VECTOR_SOUTH: Vector3d get() = Vector3d(0.0, 0.0, 1.0)

/** An east vector (1, 0, 0). */
val VECTOR_EAST: Vector3d get() = Vector3d(1.0, 0.0, 0.0)

/** A west vector (-1, 0, 0). */
val VECTOR_WEST: Vector3d get() = Vector3d(-1.0, 0.0, 0.0)

/**
 * Adds two vectors.
 *
 * @param other the vector to add
 * @return a new vector representing the sum
 */
operator fun Vector3d.plus(other: Vector3d): Vector3d =
    Vector3d(x + other.x, y + other.y, z + other.z)

/**
 * Subtracts two vectors.
 *
 * @param other the vector to subtract
 * @return a new vector representing the difference
 */
operator fun Vector3d.minus(other: Vector3d): Vector3d =
    Vector3d(x - other.x, y - other.y, z - other.z)

/**
 * Multiplies a vector by a scalar.
 *
 * @param scalar the scalar to multiply by
 * @return a new scaled vector
 */
operator fun Vector3d.times(scalar: Double): Vector3d =
    Vector3d(x * scalar, y * scalar, z * scalar)

/**
 * Multiplies a vector by an integer scalar.
 *
 * @param scalar the scalar to multiply by
 * @return a new scaled vector
 */
operator fun Vector3d.times(scalar: Int): Vector3d =
    Vector3d(x * scalar, y * scalar, z * scalar)

/**
 * Divides a vector by a scalar.
 *
 * @param scalar the scalar to divide by
 * @return a new scaled vector
 */
operator fun Vector3d.div(scalar: Double): Vector3d =
    Vector3d(x / scalar, y / scalar, z / scalar)

/**
 * Negates a vector.
 *
 * @return a new vector with all components negated
 */
operator fun Vector3d.unaryMinus(): Vector3d =
    Vector3d(-x, -y, -z)

/**
 * Component-wise multiplication (Hadamard product).
 *
 * @param other the vector to multiply with
 * @return a new vector with component-wise products
 */
operator fun Vector3d.times(other: Vector3d): Vector3d =
    Vector3d(x * other.x, y * other.y, z * other.z)

/**
 * Computes the squared length of this vector.
 * More efficient than [length] when comparing distances.
 *
 * @return the squared length
 */
fun Vector3d.lengthSquared(): Double = x * x + y * y + z * z

/**
 * Returns a normalized (unit length) copy of this vector.
 *
 * @return a new normalized vector, or zero vector if length is zero
 */
fun Vector3d.normalized(): Vector3d {
    val len = length()
    return if (len > 0.0) this / len else VECTOR_ZERO
}

/**
 * Computes the distance to another vector.
 *
 * @param other the target vector
 * @return the distance between the two vectors
 */
fun Vector3d.distanceTo(other: Vector3d): Double = (this - other).length()

/**
 * Computes the squared distance to another vector.
 * More efficient than [distanceTo] when comparing distances.
 *
 * @param other the target vector
 * @return the squared distance
 */
fun Vector3d.distanceSquaredTo(other: Vector3d): Double = (this - other).lengthSquared()

/**
 * Computes the dot product of two vectors.
 *
 * @param other the other vector
 * @return the dot product
 */
fun Vector3d.dot(other: Vector3d): Double = x * other.x + y * other.y + z * other.z

/**
 * Computes the cross product of two vectors.
 *
 * @param other the other vector
 * @return the cross product vector
 */
fun Vector3d.cross(other: Vector3d): Vector3d = Vector3d(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
)

/**
 * Linearly interpolates between this vector and another.
 *
 * @param other the target vector
 * @param t the interpolation factor (0 = this, 1 = other)
 * @return the interpolated vector
 */
fun Vector3d.lerp(other: Vector3d, t: Double): Vector3d = Vector3d(
    x + (other.x - x) * t,
    y + (other.y - y) * t,
    z + (other.z - z) * t
)

/**
 * Returns a copy with a new X component.
 *
 * @param x the new X value
 * @return a new vector with the modified X
 */
fun Vector3d.withX(x: Double): Vector3d = Vector3d(x, y, z)

/**
 * Returns a copy with a new Y component.
 *
 * @param y the new Y value
 * @return a new vector with the modified Y
 */
fun Vector3d.withY(y: Double): Vector3d = Vector3d(x, y, z)

/**
 * Returns a copy with a new Z component.
 *
 * @param z the new Z value
 * @return a new vector with the modified Z
 */
fun Vector3d.withZ(z: Double): Vector3d = Vector3d(x, y, z)

/**
 * Returns the horizontal components only (Y = 0).
 *
 * @return a new vector with only X and Z components
 */
fun Vector3d.horizontal(): Vector3d = Vector3d(x, 0.0, z)

/**
 * Returns the horizontal length (ignoring Y).
 *
 * @return the horizontal distance
 */
fun Vector3d.horizontalLength(): Double = sqrt(x * x + z * z)

/**
 * Returns a normalized horizontal direction (Y = 0).
 *
 * @return a new normalized horizontal vector
 */
fun Vector3d.horizontalNormalized(): Vector3d {
    val len = horizontalLength()
    return if (len > 0.0) Vector3d(x / len, 0.0, z / len) else VECTOR_ZERO
}

/**
 * Creates a direction vector from yaw and pitch angles.
 *
 * @param yaw the horizontal angle in radians
 * @param pitch the vertical angle in radians
 * @return a normalized direction vector
 */
fun directionFromYawPitch(yaw: Float, pitch: Float): Vector3d {
    val cosP = cos(pitch.toDouble())
    return Vector3d(
        -sin(yaw.toDouble()) * cosP,
        -sin(pitch.toDouble()),
        cos(yaw.toDouble()) * cosP
    )
}

/**
 * Creates a direction vector from yaw and pitch angles in degrees.
 *
 * @param yawDegrees the horizontal angle in degrees
 * @param pitchDegrees the vertical angle in degrees
 * @return a normalized direction vector
 */
fun directionFromDegrees(yawDegrees: Float, pitchDegrees: Float): Vector3d {
    val yaw = Math.toRadians(yawDegrees.toDouble())
    val pitch = Math.toRadians(pitchDegrees.toDouble())
    val cosP = cos(pitch)
    return Vector3d(
        -sin(yaw) * cosP,
        -sin(pitch),
        cos(yaw) * cosP
    )
}

/**
 * Creates a horizontal direction vector from yaw angle.
 *
 * @param yaw the horizontal angle in radians
 * @return a normalized horizontal direction vector
 */
fun horizontalDirectionFromYaw(yaw: Float): Vector3d = Vector3d(
    -sin(yaw.toDouble()),
    0.0,
    cos(yaw.toDouble())
)

/**
 * Checks if this vector is within range of another.
 *
 * @param other the other vector
 * @param range the maximum distance
 * @return true if within range
 */
fun Vector3d.isWithinRange(other: Vector3d, range: Double): Boolean =
    distanceSquaredTo(other) <= range * range

/**
 * Clamps each component to the given range.
 *
 * @param min the minimum value for each component
 * @param max the maximum value for each component
 * @return a new clamped vector
 */
fun Vector3d.clamp(min: Double, max: Double): Vector3d = Vector3d(
    x.coerceIn(min, max),
    y.coerceIn(min, max),
    z.coerceIn(min, max)
)

/**
 * Clamps this vector to a maximum length.
 *
 * @param maxLength the maximum length
 * @return a new vector with length at most maxLength
 */
fun Vector3d.clampLength(maxLength: Double): Vector3d {
    val len = length()
    return if (len > maxLength && len > 0.0) this * (maxLength / len) else this
}

/**
 * Returns true if this vector is approximately zero.
 *
 * @param epsilon the maximum component magnitude to consider zero
 * @return true if all components are near zero
 */
fun Vector3d.isZero(epsilon: Double = 0.0001): Boolean =
    kotlin.math.abs(x) < epsilon && kotlin.math.abs(y) < epsilon && kotlin.math.abs(z) < epsilon

/**
 * Returns true if this vector is approximately equal to another.
 *
 * @param other the vector to compare
 * @param epsilon the maximum difference per component
 * @return true if approximately equal
 */
fun Vector3d.approxEquals(other: Vector3d, epsilon: Double = 0.0001): Boolean =
    kotlin.math.abs(x - other.x) < epsilon &&
    kotlin.math.abs(y - other.y) < epsilon &&
    kotlin.math.abs(z - other.z) < epsilon

/**
 * Converts to a formatted string with specified decimal places.
 *
 * @param decimals the number of decimal places
 * @return a formatted string like "(1.00, 2.00, 3.00)"
 */
fun Vector3d.toFormattedString(decimals: Int = 2): String =
    "(%.${decimals}f, %.${decimals}f, %.${decimals}f)".format(x, y, z)
