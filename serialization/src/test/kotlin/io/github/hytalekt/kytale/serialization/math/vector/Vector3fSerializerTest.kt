package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector3fSerializerTest :
    FunSpec({
        test("should serialize Vector3f to JSON") {
            val vector = Vector3f(1.5f, 2.5f, 3.5f)
            val json = Json.encodeToString(Vector3fSerializer, vector)
            json shouldBe """{"x":1.5,"y":2.5,"z":3.5}"""
        }

        test("should deserialize Vector3f from JSON") {
            val json = """{"x":1.5,"y":2.5,"z":3.5}"""
            val vector = Json.decodeFromString(Vector3fSerializer, json)
            vector.x shouldBe 1.5f
            vector.y shouldBe 2.5f
            vector.z shouldBe 3.5f
        }

        test("should roundtrip Vector3f serialization") {
            val original = Vector3f(10.0f, 20.0f, 30.0f)
            val json = Json.encodeToString(Vector3fSerializer, original)
            val deserialized = Json.decodeFromString(Vector3fSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
        }

        test("should handle negative values") {
            val vector = Vector3f(-1.0f, -2.0f, -3.0f)
            val json = Json.encodeToString(Vector3fSerializer, vector)
            val decoded = Json.decodeFromString(Vector3fSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
            decoded.z shouldBe vector.z
        }
    })
