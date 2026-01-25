package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector2i
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector2iSerializerTest :
    FunSpec({
        test("should serialize Vector2i to JSON") {
            val vector = Vector2i(10, 20)
            val json = Json.encodeToString(Vector2iSerializer, vector)
            json shouldBe """{"x":10,"y":20}"""
        }

        test("should deserialize Vector2i from JSON") {
            val json = """{"x":10,"y":20}"""
            val vector = Json.decodeFromString(Vector2iSerializer, json)
            vector.x shouldBe 10
            vector.y shouldBe 20
        }

        test("should roundtrip Vector2i serialization") {
            val original = Vector2i(100, 200)
            val json = Json.encodeToString(Vector2iSerializer, original)
            val deserialized = Json.decodeFromString(Vector2iSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
        }

        test("should handle negative values") {
            val vector = Vector2i(-10, -20)
            val json = Json.encodeToString(Vector2iSerializer, vector)
            val decoded = Json.decodeFromString(Vector2iSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
        }
    })
