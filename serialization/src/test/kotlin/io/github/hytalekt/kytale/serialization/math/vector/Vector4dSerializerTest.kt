package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector4d
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector4dSerializerTest :
    FunSpec({
        test("should serialize Vector4d to JSON") {
            val vector = Vector4d(1.5, 2.5, 3.5, 4.5)
            val json = Json.encodeToString(Vector4dSerializer, vector)
            json shouldBe """{"x":1.5,"y":2.5,"z":3.5,"w":4.5}"""
        }

        test("should deserialize Vector4d from JSON") {
            val json = """{"x":1.5,"y":2.5,"z":3.5,"w":4.5}"""
            val vector = Json.decodeFromString(Vector4dSerializer, json)
            vector.x shouldBe 1.5
            vector.y shouldBe 2.5
            vector.z shouldBe 3.5
            vector.w shouldBe 4.5
        }

        test("should roundtrip Vector4d serialization") {
            val original = Vector4d(10.0, 20.0, 30.0, 40.0)
            val json = Json.encodeToString(Vector4dSerializer, original)
            val deserialized = Json.decodeFromString(Vector4dSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
            deserialized.w shouldBe original.w
        }

        test("should handle negative values") {
            val vector = Vector4d(-1.0, -2.0, -3.0, -4.0)
            val json = Json.encodeToString(Vector4dSerializer, vector)
            val decoded = Json.decodeFromString(Vector4dSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
            decoded.z shouldBe vector.z
            decoded.w shouldBe vector.w
        }
    })
