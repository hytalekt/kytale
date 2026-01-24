package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Vec2f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vec2fSerializerTest :
    FunSpec({
        test("should serialize Vec2f to JSON") {
            val vec = Vec2f(1.5f, 2.5f)
            val json = Json.encodeToString(Vec2fSerializer, vec)
            json shouldBe """{"x":1.5,"y":2.5}"""
        }

        test("should deserialize Vec2f from JSON") {
            val json = """{"x":1.5,"y":2.5}"""
            val vec = Json.decodeFromString(Vec2fSerializer, json)
            vec.x shouldBe 1.5f
            vec.y shouldBe 2.5f
        }

        test("should roundtrip Vec2f serialization") {
            val original = Vec2f(10.0f, 20.0f)
            val json = Json.encodeToString(Vec2fSerializer, original)
            val deserialized = Json.decodeFromString(Vec2fSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
        }

        test("should handle negative values") {
            val vec = Vec2f(-1.0f, -2.0f)
            val json = Json.encodeToString(Vec2fSerializer, vec)
            val decoded = Json.decodeFromString(Vec2fSerializer, json)
            decoded.x shouldBe vec.x
            decoded.y shouldBe vec.y
        }
    })
