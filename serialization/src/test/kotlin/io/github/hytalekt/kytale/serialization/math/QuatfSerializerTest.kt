package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Quatf
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuatfSerializerTest :
    FunSpec({
        test("should serialize Quatf to JSON") {
            val quat = Quatf(0.5f, 0.5f, 0.5f, 0.5f)
            val json = Json.encodeToString(QuatfSerializer, quat)
            json shouldBe """{"x":0.5,"y":0.5,"z":0.5,"w":0.5}"""
        }

        test("should deserialize Quatf from JSON") {
            val json = """{"x":0.1,"y":0.2,"z":0.3,"w":0.4}"""
            val quat = Json.decodeFromString(QuatfSerializer, json)
            quat.x shouldBe 0.1f
            quat.y shouldBe 0.2f
            quat.z shouldBe 0.3f
            quat.w shouldBe 0.4f
        }

        test("should roundtrip Quatf serialization") {
            val original = Quatf(0.7f, 0.0f, 0.7f, 0.0f)
            val json = Json.encodeToString(QuatfSerializer, original)
            val deserialized = Json.decodeFromString(QuatfSerializer, json)
            deserialized.x shouldBe original.x
            deserialized.y shouldBe original.y
            deserialized.z shouldBe original.z
            deserialized.w shouldBe original.w
        }

        test("should handle identity quaternion") {
            val quat = Quatf(0.0f, 0.0f, 0.0f, 1.0f)
            val json = Json.encodeToString(QuatfSerializer, quat)
            val decoded = Json.decodeFromString(QuatfSerializer, json)
            decoded.x shouldBe quat.x
            decoded.y shouldBe quat.y
            decoded.z shouldBe quat.z
            decoded.w shouldBe quat.w
        }
    })
