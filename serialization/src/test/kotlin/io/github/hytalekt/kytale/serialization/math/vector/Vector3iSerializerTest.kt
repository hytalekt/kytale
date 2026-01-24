package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3i
import io.github.hytalekt.kytale.serialization.math.vector.Vector3iSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector3iSerializerTest :
    FunSpec({
        test("should serialize Vector3i to JSON") {
            val vector = Vector3i(1, 2, 3)
            val json = Json.encodeToString(Vector3iSerializer, vector)
            json shouldBe """{"x":1,"y":2,"z":3}"""
        }

        test("should deserialize Vector3i from JSON") {
            val json = """{"x":10,"y":20,"z":30}"""
            val vector = Json.decodeFromString(Vector3iSerializer, json)
            vector.x shouldBe 10
            vector.y shouldBe 20
            vector.z shouldBe 30
        }

        test("should roundtrip Vector3i serialization") {
            val original = Vector3i(100, 200, 300)
            val json = Json.encodeToString(Vector3iSerializer, original)
            val deserialized = Json.decodeFromString(Vector3iSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
        }

        test("should handle negative values") {
            val vector = Vector3i(-10, -20, -30)
            val json = Json.encodeToString(Vector3iSerializer, vector)
            val decoded = Json.decodeFromString(Vector3iSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
            decoded.z shouldBe vector.z
        }
    })
