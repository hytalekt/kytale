package io.github.hytalekt.kytale.extension

import com.hypixel.hytale.math.vector.Vector3d
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.sqrt

class VectorExtensionsTest : FunSpec({

    context("vec3 factory functions") {
        test("vec3 with doubles creates correct vector") {
            val v = vec3(1.0, 2.0, 3.0)

            v.x shouldBe 1.0
            v.y shouldBe 2.0
            v.z shouldBe 3.0
        }

        test("vec3 with ints creates correct vector") {
            val v = vec3(1, 2, 3)

            v.x shouldBe 1.0
            v.y shouldBe 2.0
            v.z shouldBe 3.0
        }

        test("vec3 with floats creates correct vector") {
            val v = vec3(1.5f, 2.5f, 3.5f)

            v.x shouldBe 1.5
            v.y shouldBe 2.5
            v.z shouldBe 3.5
        }
    }

    context("vector constants") {
        test("VECTOR_ZERO is (0, 0, 0)") {
            VECTOR_ZERO.x shouldBe 0.0
            VECTOR_ZERO.y shouldBe 0.0
            VECTOR_ZERO.z shouldBe 0.0
        }

        test("VECTOR_UP is (0, 1, 0)") {
            VECTOR_UP.x shouldBe 0.0
            VECTOR_UP.y shouldBe 1.0
            VECTOR_UP.z shouldBe 0.0
        }

        test("VECTOR_DOWN is (0, -1, 0)") {
            VECTOR_DOWN.x shouldBe 0.0
            VECTOR_DOWN.y shouldBe (-1.0)
            VECTOR_DOWN.z shouldBe 0.0
        }

        test("VECTOR_NORTH is (0, 0, -1)") {
            VECTOR_NORTH.z shouldBe (-1.0)
        }

        test("VECTOR_SOUTH is (0, 0, 1)") {
            VECTOR_SOUTH.z shouldBe 1.0
        }

        test("VECTOR_EAST is (1, 0, 0)") {
            VECTOR_EAST.x shouldBe 1.0
        }

        test("VECTOR_WEST is (-1, 0, 0)") {
            VECTOR_WEST.x shouldBe (-1.0)
        }
    }

    context("operator plus") {
        test("adds two vectors correctly") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(4.0, 5.0, 6.0)

            val result = a + b

            result.x shouldBe 5.0
            result.y shouldBe 7.0
            result.z shouldBe 9.0
        }

        test("adding zero vector returns same values") {
            val a = vec3(1.0, 2.0, 3.0)

            val result = a + VECTOR_ZERO

            result.x shouldBe 1.0
            result.y shouldBe 2.0
            result.z shouldBe 3.0
        }
    }

    context("operator minus") {
        test("subtracts two vectors correctly") {
            val a = vec3(5.0, 7.0, 9.0)
            val b = vec3(1.0, 2.0, 3.0)

            val result = a - b

            result.x shouldBe 4.0
            result.y shouldBe 5.0
            result.z shouldBe 6.0
        }

        test("subtracting from itself gives zero vector") {
            val a = vec3(5.0, 7.0, 9.0)

            val result = a - a

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }
    }

    context("operator times (scalar)") {
        test("multiplies vector by double scalar") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v * 2.0

            result.x shouldBe 2.0
            result.y shouldBe 4.0
            result.z shouldBe 6.0
        }

        test("multiplies vector by int scalar") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v * 3

            result.x shouldBe 3.0
            result.y shouldBe 6.0
            result.z shouldBe 9.0
        }

        test("multiplying by zero gives zero vector") {
            val v = vec3(5.0, 10.0, 15.0)

            val result = v * 0.0

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }

        test("multiplying by negative inverts") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v * (-1.0)

            result.x shouldBe (-1.0)
            result.y shouldBe (-2.0)
            result.z shouldBe (-3.0)
        }
    }

    context("operator times (component-wise)") {
        test("multiplies vectors component-wise") {
            val a = vec3(2.0, 3.0, 4.0)
            val b = vec3(5.0, 6.0, 7.0)

            val result = a * b

            result.x shouldBe 10.0
            result.y shouldBe 18.0
            result.z shouldBe 28.0
        }
    }

    context("operator div") {
        test("divides vector by scalar") {
            val v = vec3(6.0, 9.0, 12.0)

            val result = v / 3.0

            result.x shouldBe 2.0
            result.y shouldBe 3.0
            result.z shouldBe 4.0
        }
    }

    context("operator unaryMinus") {
        test("negates all components") {
            val v = vec3(1.0, -2.0, 3.0)

            val result = -v

            result.x shouldBe (-1.0)
            result.y shouldBe 2.0
            result.z shouldBe (-3.0)
        }
    }

    context("lengthSquared") {
        test("calculates squared length correctly") {
            val v = vec3(3.0, 4.0, 0.0)

            v.lengthSquared() shouldBe 25.0
        }

        test("zero vector has zero squared length") {
            VECTOR_ZERO.lengthSquared() shouldBe 0.0
        }
    }

    context("normalized") {
        test("returns unit vector") {
            val v = vec3(3.0, 0.0, 4.0)

            val result = v.normalized()

            result.length() shouldBe (1.0 plusOrMinus 0.0001)
        }

        test("maintains direction") {
            val v = vec3(10.0, 0.0, 0.0)

            val result = v.normalized()

            result.x shouldBe 1.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }

        test("zero vector returns zero vector") {
            val result = VECTOR_ZERO.normalized()

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }
    }

    context("distanceTo") {
        test("calculates distance between two points") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(3.0, 4.0, 0.0)

            a.distanceTo(b) shouldBe 5.0
        }

        test("distance to same point is zero") {
            val a = vec3(5.0, 5.0, 5.0)

            a.distanceTo(a) shouldBe 0.0
        }

        test("distance is symmetric") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(4.0, 5.0, 6.0)

            a.distanceTo(b) shouldBe b.distanceTo(a)
        }
    }

    context("distanceSquaredTo") {
        test("calculates squared distance correctly") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(3.0, 4.0, 0.0)

            a.distanceSquaredTo(b) shouldBe 25.0
        }
    }

    context("dot product") {
        test("calculates dot product correctly") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(4.0, 5.0, 6.0)

            a.dot(b) shouldBe 32.0 // 1*4 + 2*5 + 3*6 = 4 + 10 + 18
        }

        test("dot product of perpendicular vectors is zero") {
            val a = vec3(1.0, 0.0, 0.0)
            val b = vec3(0.0, 1.0, 0.0)

            a.dot(b) shouldBe 0.0
        }

        test("dot product of parallel vectors equals product of lengths") {
            val a = vec3(2.0, 0.0, 0.0)
            val b = vec3(3.0, 0.0, 0.0)

            a.dot(b) shouldBe 6.0
        }
    }

    context("cross product") {
        test("cross of X and Y axes gives Z axis") {
            val x = vec3(1.0, 0.0, 0.0)
            val y = vec3(0.0, 1.0, 0.0)

            val result = x.cross(y)

            result.x shouldBe (0.0 plusOrMinus 0.0001)
            result.y shouldBe (0.0 plusOrMinus 0.0001)
            result.z shouldBe (1.0 plusOrMinus 0.0001)
        }

        test("cross product is anti-commutative") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(4.0, 5.0, 6.0)

            val ab = a.cross(b)
            val ba = b.cross(a)

            ab.x shouldBe ((-ba.x) plusOrMinus 0.0001)
            ab.y shouldBe ((-ba.y) plusOrMinus 0.0001)
            ab.z shouldBe ((-ba.z) plusOrMinus 0.0001)
        }

        test("cross of parallel vectors is zero") {
            val a = vec3(1.0, 0.0, 0.0)
            val b = vec3(2.0, 0.0, 0.0)

            val result = a.cross(b)

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }
    }

    context("lerp") {
        test("lerp at 0 returns start vector") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(10.0, 10.0, 10.0)

            val result = a.lerp(b, 0.0)

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }

        test("lerp at 1 returns end vector") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(10.0, 10.0, 10.0)

            val result = a.lerp(b, 1.0)

            result.x shouldBe 10.0
            result.y shouldBe 10.0
            result.z shouldBe 10.0
        }

        test("lerp at 0.5 returns midpoint") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(10.0, 20.0, 30.0)

            val result = a.lerp(b, 0.5)

            result.x shouldBe 5.0
            result.y shouldBe 10.0
            result.z shouldBe 15.0
        }
    }

    context("withX, withY, withZ") {
        test("withX creates new vector with changed X") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v.withX(10.0)

            result.x shouldBe 10.0
            result.y shouldBe 2.0
            result.z shouldBe 3.0
        }

        test("withY creates new vector with changed Y") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v.withY(10.0)

            result.x shouldBe 1.0
            result.y shouldBe 10.0
            result.z shouldBe 3.0
        }

        test("withZ creates new vector with changed Z") {
            val v = vec3(1.0, 2.0, 3.0)

            val result = v.withZ(10.0)

            result.x shouldBe 1.0
            result.y shouldBe 2.0
            result.z shouldBe 10.0
        }
    }

    context("horizontal") {
        test("zeroes out Y component") {
            val v = vec3(5.0, 10.0, 15.0)

            val result = v.horizontal()

            result.x shouldBe 5.0
            result.y shouldBe 0.0
            result.z shouldBe 15.0
        }
    }

    context("horizontalLength") {
        test("calculates horizontal distance (ignores Y)") {
            val v = vec3(3.0, 100.0, 4.0)

            v.horizontalLength() shouldBe 5.0
        }
    }

    context("horizontalNormalized") {
        test("normalizes horizontal components only") {
            val v = vec3(3.0, 100.0, 4.0)

            val result = v.horizontalNormalized()

            result.x shouldBe (0.6 plusOrMinus 0.0001)
            result.y shouldBe 0.0
            result.z shouldBe (0.8 plusOrMinus 0.0001)
        }

        test("zero horizontal components return zero vector") {
            val v = vec3(0.0, 100.0, 0.0)

            val result = v.horizontalNormalized()

            result.x shouldBe 0.0
            result.y shouldBe 0.0
            result.z shouldBe 0.0
        }
    }

    context("isWithinRange") {
        test("returns true when within range") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(3.0, 4.0, 0.0)

            a.isWithinRange(b, 10.0) shouldBe true
        }

        test("returns true when exactly at range") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(3.0, 4.0, 0.0)

            a.isWithinRange(b, 5.0) shouldBe true
        }

        test("returns false when outside range") {
            val a = vec3(0.0, 0.0, 0.0)
            val b = vec3(3.0, 4.0, 0.0)

            a.isWithinRange(b, 4.0) shouldBe false
        }
    }

    context("clamp") {
        test("clamps components to range") {
            val v = vec3(-5.0, 50.0, 5.0)

            val result = v.clamp(0.0, 10.0)

            result.x shouldBe 0.0
            result.y shouldBe 10.0
            result.z shouldBe 5.0
        }
    }

    context("clampLength") {
        test("clamps vector to max length") {
            val v = vec3(10.0, 0.0, 0.0)

            val result = v.clampLength(5.0)

            result.length() shouldBe (5.0 plusOrMinus 0.0001)
        }

        test("does not modify vector shorter than max") {
            val v = vec3(3.0, 0.0, 0.0)

            val result = v.clampLength(5.0)

            result.length() shouldBe 3.0
        }
    }

    context("isZero") {
        test("returns true for zero vector") {
            VECTOR_ZERO.isZero() shouldBe true
        }

        test("returns true for near-zero vector within epsilon") {
            val v = vec3(0.00001, 0.00001, 0.00001)

            v.isZero() shouldBe true
        }

        test("returns false for non-zero vector") {
            val v = vec3(1.0, 0.0, 0.0)

            v.isZero() shouldBe false
        }
    }

    context("approxEquals") {
        test("returns true for approximately equal vectors") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(1.00001, 2.00001, 3.00001)

            a.approxEquals(b) shouldBe true
        }

        test("returns false for different vectors") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(1.1, 2.0, 3.0)

            a.approxEquals(b) shouldBe false
        }

        test("uses custom epsilon") {
            val a = vec3(1.0, 2.0, 3.0)
            val b = vec3(1.05, 2.05, 3.05)

            a.approxEquals(b, 0.1) shouldBe true
        }
    }

    context("toFormattedString") {
        test("formats with default 2 decimal places") {
            val v = vec3(1.234567, 2.345678, 3.456789)

            v.toFormattedString() shouldBe "(1.23, 2.35, 3.46)"
        }

        test("formats with custom decimal places") {
            val v = vec3(1.234567, 2.345678, 3.456789)

            v.toFormattedString(4) shouldBe "(1.2346, 2.3457, 3.4568)"
        }
    }

    context("directionFromYawPitch") {
        test("zero yaw and pitch points north-ish (positive Z)") {
            val dir = directionFromYawPitch(0f, 0f)

            dir.z shouldBe (1.0 plusOrMinus 0.0001)
            dir.y shouldBe (0.0 plusOrMinus 0.0001)
        }

        test("pitch of PI/2 points straight down") {
            val dir = directionFromYawPitch(0f, (Math.PI / 2).toFloat())

            dir.y shouldBe ((-1.0) plusOrMinus 0.0001)
        }
    }

    context("directionFromDegrees") {
        test("0 degrees points north-ish") {
            val dir = directionFromDegrees(0f, 0f)

            dir.z shouldBe (1.0 plusOrMinus 0.0001)
        }

        test("90 degrees pitch points straight down") {
            val dir = directionFromDegrees(0f, 90f)

            dir.y shouldBe ((-1.0) plusOrMinus 0.0001)
        }
    }

    context("horizontalDirectionFromYaw") {
        test("zero yaw points positive Z") {
            val dir = horizontalDirectionFromYaw(0f)

            dir.z shouldBe (1.0 plusOrMinus 0.0001)
            dir.x shouldBe (0.0 plusOrMinus 0.0001)
            dir.y shouldBe 0.0
        }
    }
})
