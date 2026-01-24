package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Box2D
import com.hypixel.hytale.math.vector.Vector2d
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Box2DSerializerTest :
    FunSpec({
        test("should serialize Box2D to JSON") {
            val box = Box2D(Vector2d(0.0, 0.0), Vector2d(10.0, 10.0))
            val json = Json.encodeToString(Box2DSerializer, box)
            json shouldBe """{"min":{"x":0.0,"y":0.0},"max":{"x":10.0,"y":10.0}}"""
        }

        test("should deserialize Box2D from JSON") {
            val json = """{"min":{"x":0.0,"y":0.0},"max":{"x":10.0,"y":10.0}}"""
            val box = Json.decodeFromString(Box2DSerializer, json)
            box.min.x shouldBe 0.0
            box.min.y shouldBe 0.0
            box.max.x shouldBe 10.0
            box.max.y shouldBe 10.0
        }

        test("should roundtrip Box2D serialization") {
            val original = Box2D(Vector2d(1.0, 2.0), Vector2d(5.0, 6.0))
            val json = Json.encodeToString(Box2DSerializer, original)
            val deserialized = Json.decodeFromString(Box2DSerializer, json)
            deserialized.min.x shouldBe original.min.x
            deserialized.min.y shouldBe original.min.y
            deserialized.max.x shouldBe original.max.x
            deserialized.max.y shouldBe original.max.y
        }

        test("should handle negative values") {
            val box = Box2D(Vector2d(-10.0, -10.0), Vector2d(-5.0, -5.0))
            val json = Json.encodeToString(Box2DSerializer, box)
            val decoded = Json.decodeFromString(Box2DSerializer, json)
            decoded.min.x shouldBe box.min.x
            decoded.min.y shouldBe box.min.y
            decoded.max.x shouldBe box.max.x
            decoded.max.y shouldBe box.max.y
        }
    })
