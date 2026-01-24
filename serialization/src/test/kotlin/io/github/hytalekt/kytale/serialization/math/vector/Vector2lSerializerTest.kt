package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector2l
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector2lSerializerTest :
    FunSpec({
        test("should serialize Vector2l to JSON") {
            val vector = Vector2l(100L, 200L)
            val json = Json.encodeToString(Vector2lSerializer, vector)
            json shouldBe """{"x":100,"y":200}"""
        }

        test("should deserialize Vector2l from JSON") {
            val json = """{"x":100,"y":200}"""
            val vector = Json.decodeFromString(Vector2lSerializer, json)
            vector.x shouldBe 100L
            vector.y shouldBe 200L
        }

        test("should roundtrip Vector2l serialization") {
            val original = Vector2l(1000L, 2000L)
            val json = Json.encodeToString(Vector2lSerializer, original)
            val deserialized = Json.decodeFromString(Vector2lSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
        }

        test("should handle negative values") {
            val vector = Vector2l(-100L, -200L)
            val json = Json.encodeToString(Vector2lSerializer, vector)
            val decoded = Json.decodeFromString(Vector2lSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
        }
    })
