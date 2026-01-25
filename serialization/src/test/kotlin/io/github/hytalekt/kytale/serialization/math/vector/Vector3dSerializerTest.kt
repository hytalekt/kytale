package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3d
import io.github.hytalekt.kytale.serialization.math.vector.Vector3dSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vector3dSerializerTest :
    FunSpec({
        test("should serialize Vector3d to JSON") {
            val vector = Vector3d(1.5, 2.5, 3.5)
            val json = Json.encodeToString(Vector3dSerializer, vector)
            json shouldBe """{"x":1.5,"y":2.5,"z":3.5}"""
        }

        test("should deserialize Vector3d from JSON") {
            val json = """{"x":1.5,"y":2.5,"z":3.5}"""
            val vector = Json.decodeFromString(Vector3dSerializer, json)
            vector.x shouldBe 1.5
            vector.y shouldBe 2.5
            vector.z shouldBe 3.5
        }

        test("should roundtrip Vector3d serialization") {
            val original = Vector3d(10.0, 20.0, 30.0)
            val json = Json.encodeToString(Vector3dSerializer, original)
            val deserialized = Json.decodeFromString(Vector3dSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
        }

        test("should handle negative values") {
            val vector = Vector3d(-1.0, -2.0, -3.0)
            val json = Json.encodeToString(Vector3dSerializer, vector)
            val decoded = Json.decodeFromString(Vector3dSerializer, json)
            decoded.x shouldBe vector.x
            decoded.y shouldBe vector.y
            decoded.z shouldBe vector.z
        }
    })
