package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import io.github.hytalekt.kytale.serialization.math.vector.TransformSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class TransformSerializerTest :
    FunSpec({
        test("should serialize Transform to JSON") {
            val transform = Transform(Vector3d(1.0, 2.0, 3.0), Vector3f(0.5f, 1.0f, 1.5f))
            val json = Json.encodeToString(TransformSerializer, transform)
            json shouldBe """{"position":{"x":1.0,"y":2.0,"z":3.0},"rotation":{"x":0.5,"y":1.0,"z":1.5}}"""
        }

        test("should deserialize Transform from JSON") {
            val json = """{"position":{"x":1.0,"y":2.0,"z":3.0},"rotation":{"x":0.5,"y":1.0,"z":1.5}}"""
            val transform = Json.decodeFromString(TransformSerializer, json)
            transform.position.x shouldBe 1.0
            transform.position.y shouldBe 2.0
            transform.position.z shouldBe 3.0
            transform.rotation.x shouldBe 0.5f
            transform.rotation.y shouldBe 1.0f
            transform.rotation.z shouldBe 1.5f
        }

        test("should roundtrip Transform serialization") {
            val original = Transform(Vector3d(10.0, 20.0, 30.0), Vector3f(1.0f, 2.0f, 3.0f))
            val json = Json.encodeToString(TransformSerializer, original)
            val deserialized = Json.decodeFromString(TransformSerializer, json)
            deserialized.position.x shouldBe original.position.x
            deserialized.position.y shouldBe original.position.y
            deserialized.position.z shouldBe original.position.z
            deserialized.rotation.x shouldBe original.rotation.x
            deserialized.rotation.y shouldBe original.rotation.y
            deserialized.rotation.z shouldBe original.rotation.z
        }

        test("should handle zero values") {
            val transform = Transform(Vector3d(0.0, 0.0, 0.0), Vector3f(0.0f, 0.0f, 0.0f))
            val json = Json.encodeToString(TransformSerializer, transform)
            val decoded = Json.decodeFromString(TransformSerializer, json)
            decoded.position.x shouldBe transform.position.x
            decoded.rotation.x shouldBe transform.rotation.x
        }
    })
