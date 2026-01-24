package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3l
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector3lSerializerTest :
    FunSpec({
        test("should serialize Vector3l to JSON") {
            val vector = Vector3l(100L, 200L, 300L)
            val json = Json.encodeToString(Vector3lSerializer, vector)
            json shouldBe """{"x":100,"y":200,"z":300}"""
        }

        test("should deserialize Vector3l from JSON") {
            val json = """{"x":100,"y":200,"z":300}"""
            val vector = Json.decodeFromString(Vector3lSerializer, json)
            vector.x shouldBe 100L
            vector.y shouldBe 200L
            vector.z shouldBe 300L
        }

        test("should roundtrip Vector3l serialization") {
            val original = Vector3l(1000L, 2000L, 3000L)
            val json = Json.encodeToString(Vector3lSerializer, original)
            val deserialized = Json.decodeFromString(Vector3lSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
        }

        test("should handle negative values") {
            val vector = Vector3l(-100L, -200L, -300L)
            val json = Json.encodeToString(Vector3lSerializer, vector)
            val decoded = Json.decodeFromString(Vector3lSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
            decoded.z shouldBe vector.z
        }
    })
