package io.github.hytalekt.kytale.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.floats.shouldBeLessThan
import kotlin.math.PI

class MathTest : FunSpec({
    
    context("clamp") {
        test("clamps value below min to min") {
            (-5.0).clamp(0.0, 100.0) shouldBe 0.0
        }
        
        test("clamps value above max to max") {
            150.0.clamp(0.0, 100.0) shouldBe 100.0
        }
        
        test("returns value when within range") {
            50.0.clamp(0.0, 100.0) shouldBe 50.0
        }
        
        test("clamps int below min to min") {
            (-5).clamp(0, 100) shouldBe 0
        }
        
        test("clamps int above max to max") {
            150.clamp(0, 100) shouldBe 100
        }
        
        test("returns int value when within range") {
            50.clamp(0, 100) shouldBe 50
        }
        
        test("clamps float below min to min") {
            (-5.0f).clamp(0.0f, 100.0f) shouldBe 0.0f
        }
        
        test("clamps float above max to max") {
            150.0f.clamp(0.0f, 100.0f) shouldBe 100.0f
        }
    }
    
    context("lerp") {
        test("returns start when t is 0") {
            lerp(0.0, 100.0, 0.0) shouldBe 0.0
        }
        
        test("returns end when t is 1") {
            lerp(0.0, 100.0, 1.0) shouldBe 100.0
        }
        
        test("returns midpoint when t is 0.5") {
            lerp(0.0, 100.0, 0.5) shouldBe 50.0
        }
        
        test("works with negative values") {
            lerp(-100.0, 100.0, 0.5) shouldBe 0.0
        }
        
        test("float lerp returns start when t is 0") {
            lerp(0.0f, 100.0f, 0.0f) shouldBe 0.0f
        }
        
        test("float lerp returns end when t is 1") {
            lerp(0.0f, 100.0f, 1.0f) shouldBe 100.0f
        }
    }
    
    context("inverseLerp") {
        test("returns 0 when value equals start") {
            inverseLerp(0.0, 100.0, 0.0) shouldBe 0.0
        }
        
        test("returns 1 when value equals end") {
            inverseLerp(0.0, 100.0, 100.0) shouldBe 1.0
        }
        
        test("returns 0.5 when value is midpoint") {
            inverseLerp(0.0, 100.0, 50.0) shouldBe 0.5
        }
        
        test("works with negative ranges") {
            inverseLerp(-100.0, 100.0, 0.0) shouldBe 0.5
        }
    }
    
    context("distance2D") {
        test("returns 0 for same point") {
            distance2D(5.0, 5.0, 5.0, 5.0) shouldBe 0.0
        }
        
        test("returns correct distance for 3-4-5 triangle") {
            distance2D(0.0, 0.0, 3.0, 4.0) shouldBe 5.0
        }
        
        test("is symmetric") {
            val d1 = distance2D(0.0, 0.0, 10.0, 10.0)
            val d2 = distance2D(10.0, 10.0, 0.0, 0.0)
            d1 shouldBe d2
        }
    }
    
    context("distance3D") {
        test("returns 0 for same point") {
            distance3D(5.0, 5.0, 5.0, 5.0, 5.0, 5.0) shouldBe 0.0
        }
        
        test("returns correct distance for known vector") {
            // sqrt(1 + 4 + 4) = sqrt(9) = 3
            distance3D(0.0, 0.0, 0.0, 1.0, 2.0, 2.0) shouldBe 3.0
        }
    }
    
    context("toRadians") {
        test("converts 180 degrees to PI") {
            180.0.toRadians() shouldBe (PI plusOrMinus 0.0001)
        }
        
        test("converts 90 degrees to PI/2") {
            90.0.toRadians() shouldBe ((PI / 2) plusOrMinus 0.0001)
        }
        
        test("converts 0 degrees to 0") {
            0.0.toRadians() shouldBe 0.0
        }
        
        test("float converts 180 degrees to PI") {
            180.0f.toRadians().toDouble() shouldBe (PI plusOrMinus 0.001)
        }
    }
    
    context("toDegrees") {
        test("converts PI to 180 degrees") {
            PI.toDegrees() shouldBe (180.0 plusOrMinus 0.0001)
        }
        
        test("converts PI/2 to 90 degrees") {
            (PI / 2).toDegrees() shouldBe (90.0 plusOrMinus 0.0001)
        }
    }
    
    context("smoothstep") {
        test("returns 0 when value is at edge0") {
            smoothstep(0.0, 1.0, 0.0) shouldBe 0.0
        }
        
        test("returns 1 when value is at edge1") {
            smoothstep(0.0, 1.0, 1.0) shouldBe 1.0
        }
        
        test("returns 0.5 at midpoint") {
            smoothstep(0.0, 1.0, 0.5) shouldBe 0.5
        }
        
        test("clamps below edge0") {
            smoothstep(0.0, 1.0, -1.0) shouldBe 0.0
        }
        
        test("clamps above edge1") {
            smoothstep(0.0, 1.0, 2.0) shouldBe 1.0
        }
    }
    
    context("smootherstep") {
        test("returns 0 when value is at edge0") {
            smootherstep(0.0, 1.0, 0.0) shouldBe 0.0
        }
        
        test("returns 1 when value is at edge1") {
            smootherstep(0.0, 1.0, 1.0) shouldBe 1.0
        }
        
        test("returns 0.5 at midpoint") {
            smootherstep(0.0, 1.0, 0.5) shouldBe 0.5
        }
    }
    
    context("approxEquals") {
        test("returns true for equal doubles") {
            1.0.approxEquals(1.0) shouldBe true
        }
        
        test("returns true for nearly equal doubles") {
            1.0.approxEquals(1.0000001) shouldBe true
        }
        
        test("returns false for different doubles") {
            1.0.approxEquals(2.0) shouldBe false
        }
        
        test("returns true for equal floats") {
            1.0f.approxEquals(1.0f) shouldBe true
        }
        
        test("returns true for nearly equal floats") {
            1.0f.approxEquals(1.00001f) shouldBe true
        }
    }
    
    context("roundToMultiple") {
        test("rounds double to multiple") {
            7.0.roundToMultiple(5.0) shouldBe 5.0
            8.0.roundToMultiple(5.0) shouldBe 10.0
        }

        test("rounds int to multiple") {
            7.roundToMultiple(5) shouldBe 5
            8.roundToMultiple(5) shouldBe 10
        }

        test("returns same value when already multiple") {
            10.0.roundToMultiple(5.0) shouldBe 10.0
            15.roundToMultiple(5) shouldBe 15
        }
    }

    context("remap") {
        test("remaps value from one range to another") {
            50.0.remap(0.0, 100.0, 0.0, 1.0) shouldBe 0.5
        }

        test("remaps with different ranges") {
            5.0.remap(0.0, 10.0, 100.0, 200.0) shouldBe 150.0
        }

        test("remaps edge values correctly") {
            0.0.remap(0.0, 100.0, 0.0, 1.0) shouldBe 0.0
            100.0.remap(0.0, 100.0, 0.0, 1.0) shouldBe 1.0
        }
    }

    context("wrapDegrees") {
        test("wraps negative angle to positive") {
            (-90.0).wrapDegrees() shouldBe 270.0
        }

        test("wraps angle over 360") {
            450.0.wrapDegrees() shouldBe 90.0
        }

        test("keeps angle in range unchanged") {
            180.0.wrapDegrees() shouldBe 180.0
        }

        test("float wraps negative angle") {
            (-90.0f).wrapDegrees() shouldBe 270.0f
        }

        test("float wraps angle over 360") {
            450.0f.wrapDegrees() shouldBe 90.0f
        }
    }

    context("wrapDegreesSymmetric") {
        test("wraps to symmetric range") {
            270.0.wrapDegreesSymmetric() shouldBe -90.0
        }

        test("keeps value in range unchanged") {
            90.0.wrapDegreesSymmetric() shouldBe 90.0
        }

        test("wraps negative to symmetric") {
            (-270.0).wrapDegreesSymmetric() shouldBe 90.0
        }

        test("float wraps to symmetric range") {
            270.0f.wrapDegreesSymmetric() shouldBe -90.0f
        }
    }

    context("angleDifference") {
        test("returns shortest difference") {
            angleDifference(10.0, 20.0) shouldBe 10.0
        }

        test("handles wraparound positive to negative") {
            angleDifference(350.0, 10.0) shouldBe 20.0
        }

        test("handles wraparound negative to positive") {
            angleDifference(10.0, 350.0) shouldBe -20.0
        }

        test("float returns shortest difference") {
            angleDifference(10.0f, 20.0f) shouldBe 10.0f
        }
    }

    context("lerpAngle") {
        test("interpolates between angles") {
            lerpAngle(0.0f, 90.0f, 0.5f) shouldBe 45.0f
        }

        test("handles wraparound") {
            lerpAngle(350.0f, 10.0f, 0.5f) shouldBe 0.0f
        }
    }

    context("distanceSquared2D") {
        test("returns 0 for same point") {
            distanceSquared2D(5.0, 5.0, 5.0, 5.0) shouldBe 0.0
        }

        test("returns squared distance for 3-4-5 triangle") {
            distanceSquared2D(0.0, 0.0, 3.0, 4.0) shouldBe 25.0
        }
    }

    context("distanceSquared3D") {
        test("returns 0 for same point") {
            distanceSquared3D(5.0, 5.0, 5.0, 5.0, 5.0, 5.0) shouldBe 0.0
        }

        test("returns squared distance for known vector") {
            // 1 + 4 + 4 = 9
            distanceSquared3D(0.0, 0.0, 0.0, 1.0, 2.0, 2.0) shouldBe 9.0
        }
    }

    context("manhattanDistance3D") {
        test("returns 0 for same point") {
            manhattanDistance3D(5, 5, 5, 5, 5, 5) shouldBe 0
        }

        test("returns sum of absolute differences") {
            manhattanDistance3D(0, 0, 0, 1, 2, 3) shouldBe 6
        }

        test("handles negative coordinates") {
            manhattanDistance3D(-1, -2, -3, 1, 2, 3) shouldBe 12
        }
    }

    context("directionFromAngles") {
        test("returns forward direction for 0 yaw 0 pitch") {
            val (x, y, z) = directionFromAngles(0.0f, 0.0f)
            x.approxEquals(0.0) shouldBe true
            y.approxEquals(0.0) shouldBe true
            z.approxEquals(1.0) shouldBe true
        }

        test("returns upward for -90 pitch") {
            val (x, y, z) = directionFromAngles(0.0f, -90.0f)
            x.approxEquals(0.0) shouldBe true
            y.approxEquals(1.0) shouldBe true
            z.approxEquals(0.0, 0.001) shouldBe true
        }
    }

    context("yawBetween") {
        test("returns 0 for same position") {
            yawBetween(0.0, 0.0, 0.0, 1.0) shouldBe 0.0f
        }

        test("returns 90 for east") {
            yawBetween(0.0, 0.0, -1.0, 0.0) shouldBe 90.0f
        }
    }

    context("pitchBetween") {
        test("returns 0 for horizontal") {
            pitchBetween(0.0, 0.0, 0.0, 0.0, 0.0, 1.0).toDouble() shouldBe (0.0 plusOrMinus 0.001)
        }

        test("returns negative for looking up") {
            val pitch = pitchBetween(0.0, 0.0, 0.0, 0.0, 1.0, 0.0)
            pitch shouldBeLessThan 0.0f
        }
    }
})
