package aster.amo.kytale.util

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Mathematical utilities for game development.
 *
 * These utilities provide common math operations for game plugins.
 * For vector and position types, use Hytale's native types or JOML.
 */

/**
 * Clamps a value to a range.
 *
 * @param min the minimum value (inclusive)
 * @param max the maximum value (inclusive)
 * @return the clamped value
 */
fun Double.clamp(min: Double, max: Double): Double = coerceIn(min, max)

/**
 * Clamps a value to a range.
 *
 * @param min the minimum value (inclusive)
 * @param max the maximum value (inclusive)
 * @return the clamped value
 */
fun Int.clamp(min: Int, max: Int): Int = coerceIn(min, max)

/**
 * Clamps a value to a range.
 *
 * @param min the minimum value (inclusive)
 * @param max the maximum value (inclusive)
 * @return the clamped value
 */
fun Float.clamp(min: Float, max: Float): Float = coerceIn(min, max)

/**
 * Linearly interpolates between two values.
 *
 * @param a the start value
 * @param b the end value
 * @param t the interpolation factor (0 = a, 1 = b)
 * @return the interpolated value
 */
fun lerp(a: Double, b: Double, t: Double): Double = a + (b - a) * t

/**
 * Linearly interpolates between two values.
 *
 * @param a the start value
 * @param b the end value
 * @param t the interpolation factor (0 = a, 1 = b)
 * @return the interpolated value
 */
fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t

/**
 * Inverse lerp - finds t given the endpoints and a value.
 *
 * @param a the start value
 * @param b the end value
 * @param value the value between a and b
 * @return the interpolation factor
 */
fun inverseLerp(a: Double, b: Double, value: Double): Double {
    return if (a != b) (value - a) / (b - a) else 0.0
}

/**
 * Remaps a value from one range to another.
 *
 * @param fromMin the source range minimum
 * @param fromMax the source range maximum
 * @param toMin the target range minimum
 * @param toMax the target range maximum
 * @return the remapped value
 */
fun Double.remap(fromMin: Double, fromMax: Double, toMin: Double, toMax: Double): Double {
    return lerp(toMin, toMax, inverseLerp(fromMin, fromMax, this))
}

/**
 * Converts degrees to radians.
 *
 * @return the angle in radians
 */
fun Double.toRadians(): Double = this * PI / 180.0

/**
 * Converts radians to degrees.
 *
 * @return the angle in degrees
 */
fun Double.toDegrees(): Double = this * 180.0 / PI

/**
 * Converts degrees to radians.
 *
 * @return the angle in radians
 */
fun Float.toRadians(): Float = (this * PI / 180.0).toFloat()

/**
 * Converts radians to degrees.
 *
 * @return the angle in degrees
 */
fun Float.toDegrees(): Float = (this * 180.0 / PI).toFloat()

/**
 * Wraps an angle to the range [0, 360).
 *
 * @return the wrapped angle
 */
fun Float.wrapDegrees(): Float = ((this % 360f) + 360f) % 360f

/**
 * Wraps an angle to the range [0, 360).
 *
 * @return the wrapped angle
 */
fun Double.wrapDegrees(): Double = ((this % 360.0) + 360.0) % 360.0

/**
 * Wraps an angle to the range [-180, 180).
 *
 * @return the wrapped angle
 */
fun Float.wrapDegreesSymmetric(): Float {
    val wrapped = wrapDegrees()
    return if (wrapped >= 180f) wrapped - 360f else wrapped
}

/**
 * Wraps an angle to the range [-180, 180).
 *
 * @return the wrapped angle
 */
fun Double.wrapDegreesSymmetric(): Double {
    val wrapped = wrapDegrees()
    return if (wrapped >= 180.0) wrapped - 360.0 else wrapped
}

/**
 * Returns the shortest angle difference between two angles.
 *
 * @param from the starting angle
 * @param to the target angle
 * @return the shortest angular distance
 */
fun angleDifference(from: Float, to: Float): Float {
    return (to - from).wrapDegreesSymmetric()
}

/**
 * Returns the shortest angle difference between two angles.
 *
 * @param from the starting angle
 * @param to the target angle
 * @return the shortest angular distance
 */
fun angleDifference(from: Double, to: Double): Double {
    return (to - from).wrapDegreesSymmetric()
}

/**
 * Smoothly interpolates between angles, handling wraparound.
 *
 * @param from the starting angle
 * @param to the target angle
 * @param t the interpolation factor
 * @return the interpolated angle
 */
fun lerpAngle(from: Float, to: Float, t: Float): Float {
    val diff = angleDifference(from, to)
    return (from + diff * t).wrapDegrees()
}

/**
 * Computes the distance between two 2D points.
 *
 * @param x1 first point X
 * @param y1 first point Y
 * @param x2 second point X
 * @param y2 second point Y
 * @return the Euclidean distance
 */
fun distance2D(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val dx = x2 - x1
    val dy = y2 - y1
    return sqrt(dx * dx + dy * dy)
}

/**
 * Computes the squared distance between two 2D points.
 *
 * More efficient than [distance2D] when comparing distances.
 *
 * @param x1 first point X
 * @param y1 first point Y
 * @param x2 second point X
 * @param y2 second point Y
 * @return the squared Euclidean distance
 */
fun distanceSquared2D(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val dx = x2 - x1
    val dy = y2 - y1
    return dx * dx + dy * dy
}

/**
 * Computes the distance between two 3D points.
 *
 * @param x1 first point X
 * @param y1 first point Y
 * @param z1 first point Z
 * @param x2 second point X
 * @param y2 second point Y
 * @param z2 second point Z
 * @return the Euclidean distance
 */
fun distance3D(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
    val dx = x2 - x1
    val dy = y2 - y1
    val dz = z2 - z1
    return sqrt(dx * dx + dy * dy + dz * dz)
}

/**
 * Computes the squared distance between two 3D points.
 *
 * More efficient than [distance3D] when comparing distances.
 *
 * @param x1 first point X
 * @param y1 first point Y
 * @param z1 first point Z
 * @param x2 second point X
 * @param y2 second point Y
 * @param z2 second point Z
 * @return the squared Euclidean distance
 */
fun distanceSquared3D(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
    val dx = x2 - x1
    val dy = y2 - y1
    val dz = z2 - z1
    return dx * dx + dy * dy + dz * dz
}

/**
 * Computes the Manhattan distance between two 3D points.
 *
 * @param x1 first point X
 * @param y1 first point Y
 * @param z1 first point Z
 * @param x2 second point X
 * @param y2 second point Y
 * @param z2 second point Z
 * @return the Manhattan distance
 */
fun manhattanDistance3D(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int {
    return abs(x2 - x1) + abs(y2 - y1) + abs(z2 - z1)
}

/**
 * Computes a direction vector from yaw and pitch angles.
 *
 * @param yaw the horizontal angle in degrees
 * @param pitch the vertical angle in degrees
 * @return a triple of (x, y, z) direction components
 */
fun directionFromAngles(yaw: Float, pitch: Float): Triple<Double, Double, Double> {
    val yawRad = yaw.toRadians().toDouble()
    val pitchRad = pitch.toRadians().toDouble()
    val cosP = cos(pitchRad)
    return Triple(
        -sin(yawRad) * cosP,
        -sin(pitchRad),
        cos(yawRad) * cosP
    )
}

/**
 * Computes the yaw angle to look from one point to another.
 *
 * @param fromX origin X
 * @param fromZ origin Z
 * @param toX target X
 * @param toZ target Z
 * @return the yaw angle in degrees
 */
fun yawBetween(fromX: Double, fromZ: Double, toX: Double, toZ: Double): Float {
    val dx = toX - fromX
    val dz = toZ - fromZ
    return ((atan2(-dx, dz) * 180.0 / PI) + 360.0).toFloat() % 360f
}

/**
 * Computes the pitch angle to look from one point to another.
 *
 * @param fromX origin X
 * @param fromY origin Y
 * @param fromZ origin Z
 * @param toX target X
 * @param toY target Y
 * @param toZ target Z
 * @return the pitch angle in degrees
 */
fun pitchBetween(
    fromX: Double, fromY: Double, fromZ: Double,
    toX: Double, toY: Double, toZ: Double
): Float {
    val dx = toX - fromX
    val dy = toY - fromY
    val dz = toZ - fromZ
    val horizontalDistance = sqrt(dx * dx + dz * dz)
    return (-atan2(dy, horizontalDistance) * 180.0 / PI).toFloat()
}

/**
 * Checks if a value is approximately equal to another within epsilon.
 *
 * @param other the value to compare
 * @param epsilon the maximum allowed difference
 * @return true if approximately equal
 */
fun Double.approxEquals(other: Double, epsilon: Double = 0.0001): Boolean {
    return abs(this - other) < epsilon
}

/**
 * Checks if a value is approximately equal to another within epsilon.
 *
 * @param other the value to compare
 * @param epsilon the maximum allowed difference
 * @return true if approximately equal
 */
fun Float.approxEquals(other: Float, epsilon: Float = 0.0001f): Boolean {
    return abs(this - other) < epsilon
}

/**
 * Rounds to the nearest multiple.
 *
 * @param multiple the value to round to multiples of
 * @return the rounded value
 */
fun Double.roundToMultiple(multiple: Double): Double {
    return kotlin.math.round(this / multiple) * multiple
}

/**
 * Rounds to the nearest multiple.
 *
 * @param multiple the value to round to multiples of
 * @return the rounded value
 */
fun Int.roundToMultiple(multiple: Int): Int {
    return ((this + multiple / 2) / multiple) * multiple
}

/**
 * Smoothstep interpolation for smooth transitions.
 *
 * @param edge0 the lower edge
 * @param edge1 the upper edge
 * @param x the value to interpolate
 * @return smoothly interpolated value between 0 and 1
 */
fun smoothstep(edge0: Double, edge1: Double, x: Double): Double {
    val t = ((x - edge0) / (edge1 - edge0)).clamp(0.0, 1.0)
    return t * t * (3 - 2 * t)
}

/**
 * Smootherstep interpolation (Ken Perlin's version).
 *
 * @param edge0 the lower edge
 * @param edge1 the upper edge
 * @param x the value to interpolate
 * @return smoothly interpolated value between 0 and 1
 */
fun smootherstep(edge0: Double, edge1: Double, x: Double): Double {
    val t = ((x - edge0) / (edge1 - edge0)).clamp(0.0, 1.0)
    return t * t * t * (t * (t * 6 - 15) + 10)
}

