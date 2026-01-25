package io.github.hytalekt.kytale.ext

import com.hypixel.hytale.math.vector.Vector2d
import com.hypixel.hytale.math.vector.Vector2i
import com.hypixel.hytale.math.vector.Vector2l
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.math.vector.Vector3l
import com.hypixel.hytale.math.vector.Vector4d

/** @see Vector2i.add */
operator fun Vector2i.plus(other: Vector2i): Vector2i = clone().add(other)

/** @see Vector2i.add */
operator fun Vector2i.plusAssign(other: Vector2i) {
    add(other)
}

/** @see Vector2i.subtract */
operator fun Vector2i.minus(other: Vector2i): Vector2i = clone().subtract(other)

/** @see Vector2i.subtract */
operator fun Vector2i.minusAssign(other: Vector2i) {
    subtract(other)
}

/** @see Vector2i.scale */
operator fun Vector2i.times(other: Vector2i): Vector2i = clone().scale(other)

/** @see Vector2i.scale */
operator fun Vector2i.times(scalar: Int): Vector2i = clone().scale(scalar)

/** @see Vector2i.scale */
operator fun Vector2i.timesAssign(other: Vector2i) {
    scale(other)
}

/** @see Vector2i.scale */
operator fun Vector2i.timesAssign(scalar: Int) {
    scale(scalar)
}

/** @see Vector2i.negate */
operator fun Vector2i.unaryMinus(): Vector2i = clone().negate()

/** Destructuring support for [Vector2i] */
operator fun Vector2i.component1(): Int = x

/** Destructuring support for [Vector2i] */
operator fun Vector2i.component2(): Int = y

/** @see Vector2d.add */
operator fun Vector2d.plus(other: Vector2d): Vector2d = clone().add(other)

/** @see Vector2d.add */
operator fun Vector2d.plusAssign(other: Vector2d) {
    add(other)
}

/** @see Vector2d.subtract */
operator fun Vector2d.minus(other: Vector2d): Vector2d = clone().subtract(other)

/** @see Vector2d.subtract */
operator fun Vector2d.minusAssign(other: Vector2d) {
    subtract(other)
}

/** @see Vector2d.scale */
operator fun Vector2d.times(other: Vector2d): Vector2d = clone().scale(other)

/** @see Vector2d.scale */
operator fun Vector2d.times(scalar: Double): Vector2d = clone().scale(scalar)

/** @see Vector2d.scale */
operator fun Vector2d.timesAssign(other: Vector2d) {
    scale(other)
}

/** @see Vector2d.scale */
operator fun Vector2d.timesAssign(scalar: Double) {
    scale(scalar)
}

/** @see Vector2d.negate */
operator fun Vector2d.unaryMinus(): Vector2d = clone().negate()

/** Destructuring support for [Vector2d] */
operator fun Vector2d.component1(): Double = x

/** Destructuring support for [Vector2d] */
operator fun Vector2d.component2(): Double = y

/** @see Vector2l.add */
operator fun Vector2l.plus(other: Vector2l): Vector2l = clone().add(other)

/** @see Vector2l.add */
operator fun Vector2l.plusAssign(other: Vector2l) {
    add(other)
}

/** @see Vector2l.subtract */
operator fun Vector2l.minus(other: Vector2l): Vector2l = clone().subtract(other)

/** @see Vector2l.subtract */
operator fun Vector2l.minusAssign(other: Vector2l) {
    subtract(other)
}

/** @see Vector2l.scale */
operator fun Vector2l.times(other: Vector2l): Vector2l = clone().scale(other)

/** @see Vector2l.scale */
operator fun Vector2l.times(scalar: Long): Vector2l = clone().scale(scalar)

/** @see Vector2l.scale */
operator fun Vector2l.timesAssign(other: Vector2l) {
    scale(other)
}

/** @see Vector2l.scale */
operator fun Vector2l.timesAssign(scalar: Long) {
    scale(scalar)
}

/** @see Vector2l.negate */
operator fun Vector2l.unaryMinus(): Vector2l = clone().negate()

/** Destructuring support for [Vector2l] */
operator fun Vector2l.component1(): Long = x

/** Destructuring support for [Vector2l] */
operator fun Vector2l.component2(): Long = y

/** @see Vector3i.add */
operator fun Vector3i.plus(other: Vector3i): Vector3i = clone().add(other)

/** @see Vector3i.add */
operator fun Vector3i.plusAssign(other: Vector3i) {
    add(other)
}

/** @see Vector3i.subtract */
operator fun Vector3i.minus(other: Vector3i): Vector3i = clone().subtract(other)

/** @see Vector3i.subtract */
operator fun Vector3i.minusAssign(other: Vector3i) {
    subtract(other)
}

/** @see Vector3i.scale */
operator fun Vector3i.times(other: Vector3i): Vector3i = clone().scale(other)

/** @see Vector3i.scale */
operator fun Vector3i.times(scalar: Int): Vector3i = clone().scale(scalar)

/** @see Vector3i.scale */
operator fun Vector3i.timesAssign(other: Vector3i) {
    scale(other)
}

/** @see Vector3i.scale */
operator fun Vector3i.timesAssign(scalar: Int) {
    scale(scalar)
}

/** @see Vector3i.negate */
operator fun Vector3i.unaryMinus(): Vector3i = clone().negate()

/** Destructuring support for [Vector3i] */
operator fun Vector3i.component1(): Int = x

/** Destructuring support for [Vector3i] */
operator fun Vector3i.component2(): Int = y

/** Destructuring support for [Vector3i] */
operator fun Vector3i.component3(): Int = z

/** @see Vector3l.add */
operator fun Vector3l.plus(other: Vector3l): Vector3l = clone().add(other)

/** @see Vector3l.add */
operator fun Vector3l.plusAssign(other: Vector3l) {
    add(other)
}

/** @see Vector3l.subtract */
operator fun Vector3l.minus(other: Vector3l): Vector3l = clone().subtract(other)

/** @see Vector3l.subtract */
operator fun Vector3l.minusAssign(other: Vector3l) {
    subtract(other)
}

/** @see Vector3l.scale */
operator fun Vector3l.times(other: Vector3l): Vector3l = clone().scale(other)

/** @see Vector3l.scale */
operator fun Vector3l.times(scalar: Long): Vector3l = clone().scale(scalar)

/** @see Vector3l.scale */
operator fun Vector3l.timesAssign(other: Vector3l) {
    scale(other)
}

/** @see Vector3l.scale */
operator fun Vector3l.timesAssign(scalar: Long) {
    scale(scalar)
}

/** @see Vector3l.negate */
operator fun Vector3l.unaryMinus(): Vector3l = clone().negate()

/** Destructuring support for [Vector3l] */
operator fun Vector3l.component1(): Long = x

/** Destructuring support for [Vector3l] */
operator fun Vector3l.component2(): Long = y

/** Destructuring support for [Vector3l] */
operator fun Vector3l.component3(): Long = z

/** @see Vector3d.add */
operator fun Vector3d.plus(other: Vector3d): Vector3d = clone().add(other)

/** @see Vector3d.add */
operator fun Vector3d.plus(other: Vector3i): Vector3d = clone().add(other)

/** @see Vector3d.add */
operator fun Vector3d.plusAssign(other: Vector3d) {
    add(other)
}

/** @see Vector3d.add */
operator fun Vector3d.plusAssign(other: Vector3i) {
    add(other)
}

/** @see Vector3d.subtract */
operator fun Vector3d.minus(other: Vector3d): Vector3d = clone().subtract(other)

/** @see Vector3d.subtract */
operator fun Vector3d.minus(other: Vector3i): Vector3d = clone().subtract(other)

/** @see Vector3d.subtract */
operator fun Vector3d.minusAssign(other: Vector3d) {
    subtract(other)
}

/** @see Vector3d.subtract */
operator fun Vector3d.minusAssign(other: Vector3i) {
    subtract(other)
}

/** @see Vector3d.scale */
operator fun Vector3d.times(other: Vector3d): Vector3d = clone().scale(other)

/** @see Vector3d.scale */
operator fun Vector3d.times(scalar: Double): Vector3d = clone().scale(scalar)

/** @see Vector3d.scale */
operator fun Vector3d.timesAssign(other: Vector3d) {
    scale(other)
}

/** @see Vector3d.scale */
operator fun Vector3d.timesAssign(scalar: Double) {
    scale(scalar)
}

/** @see Vector3d.negate */
operator fun Vector3d.unaryMinus(): Vector3d = clone().negate()

/** Destructuring support for [Vector3d] */
operator fun Vector3d.component1(): Double = x

/** Destructuring support for [Vector3d] */
operator fun Vector3d.component2(): Double = y

/** Destructuring support for [Vector3d] */
operator fun Vector3d.component3(): Double = z

/** @see Vector3f.add */
operator fun Vector3f.plus(other: Vector3f): Vector3f = clone().add(other)

/** @see Vector3f.add */
operator fun Vector3f.plus(other: Vector3i): Vector3f = clone().add(other)

/** @see Vector3f.add */
operator fun Vector3f.plusAssign(other: Vector3f) {
    add(other)
}

/** @see Vector3f.add */
operator fun Vector3f.plusAssign(other: Vector3i) {
    add(other)
}

/** @see Vector3f.subtract */
operator fun Vector3f.minus(other: Vector3f): Vector3f = clone().subtract(other)

/** @see Vector3f.subtract */
operator fun Vector3f.minus(other: Vector3i): Vector3f = clone().subtract(other)

/** @see Vector3f.subtract */
operator fun Vector3f.minusAssign(other: Vector3f) {
    subtract(other)
}

/** @see Vector3f.subtract */
operator fun Vector3f.minusAssign(other: Vector3i) {
    subtract(other)
}

/** @see Vector3f.scale */
operator fun Vector3f.times(other: Vector3f): Vector3f = clone().scale(other)

/** @see Vector3f.scale */
operator fun Vector3f.times(scalar: Float): Vector3f = clone().scale(scalar)

/** @see Vector3f.scale */
operator fun Vector3f.timesAssign(other: Vector3f) {
    scale(other)
}

/** @see Vector3f.scale */
operator fun Vector3f.timesAssign(scalar: Float) {
    scale(scalar)
}

/** @see Vector3f.negate */
operator fun Vector3f.unaryMinus(): Vector3f = clone().negate()

/** Destructuring support for [Vector3f] */
operator fun Vector3f.component1(): Float = x

/** Destructuring support for [Vector3f] */
operator fun Vector3f.component2(): Float = y

/** Destructuring support for [Vector3f] */
operator fun Vector3f.component3(): Float = z

/** Destructuring support for [Vector4d] */
operator fun Vector4d.component1(): Double = x

/** Destructuring support for [Vector4d] */
operator fun Vector4d.component2(): Double = y

/** Destructuring support for [Vector4d] */
operator fun Vector4d.component3(): Double = z

/** Destructuring support for [Vector4d] */
operator fun Vector4d.component4(): Double = w
