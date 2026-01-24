package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Box
import com.hypixel.hytale.math.vector.Vector3d
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class BoxSerializerTest :
    FunSpec({
        test("should serialize Box to JSON") {
            val box = Box(Vector3d(0.0, 0.0, 0.0), Vector3d(10.0, 10.0, 10.0))
            val json = Json.encodeToString(BoxSerializer, box)
            json shouldBe """{"min":{"x":0.0,"y":0.0,"z":0.0},"max":{"x":10.0,"y":10.0,"z":10.0}}"""
        }

        test("should deserialize Box from JSON") {
            val json = """{"min":{"x":1.0,"y":2.0,"z":3.0},"max":{"x":4.0,"y":5.0,"z":6.0}}"""
            val box = Json.decodeFromString(BoxSerializer, json)
            box.min.x shouldBe 1.0
            box.min.y shouldBe 2.0
            box.min.z shouldBe 3.0
            box.max.x shouldBe 4.0
            box.max.y shouldBe 5.0
            box.max.z shouldBe 6.0
        }

        test("should roundtrip Box serialization") {
            val original = Box(Vector3d(-5.0, -5.0, -5.0), Vector3d(5.0, 5.0, 5.0))
            val json = Json.encodeToString(BoxSerializer, original)
            val deserialized = Json.decodeFromString(BoxSerializer, json)
            deserialized.min.x shouldBe original.min.x
            deserialized.max.x shouldBe original.max.x
        }

        test("should handle unit box") {
            val box = Box(Vector3d(0.0, 0.0, 0.0), Vector3d(1.0, 1.0, 1.0))
            val json = Json.encodeToString(BoxSerializer, box)
            val decoded = Json.decodeFromString(BoxSerializer, json)
            decoded.min.x shouldBe box.min.x
            decoded.max.x shouldBe box.max.x
        }
    })
