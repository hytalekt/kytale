package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector2d
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector2dSerializerTest :
    FunSpec({
        test("should serialize Vector2d to JSON") {
            val vector = Vector2d(1.5, 2.5)
            val json = Json.encodeToString(Vector2dSerializer, vector)
            json shouldBe """{"x":1.5,"y":2.5}"""
        }

        test("should deserialize Vector2d from JSON") {
            val json = """{"x":1.5,"y":2.5}"""
            val vector = Json.decodeFromString(Vector2dSerializer, json)
            vector.x shouldBe 1.5
            vector.y shouldBe 2.5
        }

        test("should roundtrip Vector2d serialization") {
            val original = Vector2d(10.0, 20.0)
            val json = Json.encodeToString(Vector2dSerializer, original)
            val deserialized = Json.decodeFromString(Vector2dSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
        }

        test("should handle negative values") {
            val vector = Vector2d(-1.0, -2.0)
            val json = Json.encodeToString(Vector2dSerializer, vector)
            val decoded = Json.decodeFromString(Vector2dSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
        }
    })
