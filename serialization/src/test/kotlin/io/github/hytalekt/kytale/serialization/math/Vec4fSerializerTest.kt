package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Vec4f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class Vec4fSerializerTest :
    FunSpec({
        test("should serialize Vec4f to JSON") {
            val vec = Vec4f(1.5f, 2.5f, 3.5f, 4.5f)
            val json = Json.encodeToString(Vec4fSerializer, vec)
            json shouldBe """{"x":1.5,"y":2.5,"z":3.5,"w":4.5}"""
        }

        test("should deserialize Vec4f from JSON") {
            val json = """{"x":1.5,"y":2.5,"z":3.5,"w":4.5}"""
            val vec = Json.decodeFromString(Vec4fSerializer, json)
            vec.x shouldBe 1.5f
            vec.y shouldBe 2.5f
            vec.z shouldBe 3.5f
            vec.w shouldBe 4.5f
        }

        test("should roundtrip Vec4f serialization") {
            val original = Vec4f(10.0f, 20.0f, 30.0f, 40.0f)
            val json = Json.encodeToString(Vec4fSerializer, original)
            val deserialized = Json.decodeFromString(Vec4fSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
            deserialized.w shouldBe original.w
        }

        test("should handle negative values") {
            val vec = Vec4f(-1.0f, -2.0f, -3.0f, -4.0f)
            val json = Json.encodeToString(Vec4fSerializer, vec)
            val decoded = Json.decodeFromString(Vec4fSerializer, json)
            decoded.x shouldBe vec.x
            decoded.y shouldBe vec.y
            decoded.z shouldBe vec.z
            decoded.w shouldBe vec.w
        }
    })
