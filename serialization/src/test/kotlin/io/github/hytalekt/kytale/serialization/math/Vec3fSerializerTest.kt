package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Vec3f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vec3fSerializerTest :
    FunSpec({
        test("should serialize Vec3f to JSON") {
            val vec = Vec3f(1.5f, 2.5f, 3.5f)
            val json = Json.encodeToString(Vec3fSerializer, vec)
            json shouldBe """{"x":1.5,"y":2.5,"z":3.5}"""
        }

        test("should deserialize Vec3f from JSON") {
            val json = """{"x":1.5,"y":2.5,"z":3.5}"""
            val vec = Json.decodeFromString(Vec3fSerializer, json)
            vec.x shouldBe 1.5f
            vec.y shouldBe 2.5f
            vec.z shouldBe 3.5f
        }

        test("should roundtrip Vec3f serialization") {
            val original = Vec3f(10.0f, 20.0f, 30.0f)
            val json = Json.encodeToString(Vec3fSerializer, original)
            val deserialized = Json.decodeFromString(Vec3fSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
        }

        test("should handle negative values") {
            val vec = Vec3f(-1.0f, -2.0f, -3.0f)
            val json = Json.encodeToString(Vec3fSerializer, vec)
            val decoded = Json.decodeFromString(Vec3fSerializer, json)
            decoded.x shouldBe vec.x
            decoded.y shouldBe vec.y
            decoded.z shouldBe vec.z
        }
    })
