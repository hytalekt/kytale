package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Location
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class LocationSerializerTest :
    FunSpec({
        test("should serialize Location to JSON") {
            val location = Location("overworld", Vector3d(1.0, 2.0, 3.0), Vector3f(90f, 180f, 0f))
            val json = Json.encodeToString(LocationSerializer, location)
            json shouldBe """{"world":"overworld","position":{"x":1.0,"y":2.0,"z":3.0},"rotation":{"x":90.0,"y":180.0,"z":0.0}}"""
        }

        test("should deserialize Location from JSON") {
            val json = """{"world":"overworld","position":{"x":1.0,"y":2.0,"z":3.0},"rotation":{"x":90.0,"y":180.0,"z":0.0}}"""
            val location = Json.decodeFromString(LocationSerializer, json)
            location.world shouldBe "overworld"
            location.position.x shouldBe 1.0
            location.position.y shouldBe 2.0
            location.position.z shouldBe 3.0
            location.rotation.x shouldBe 90f
            location.rotation.y shouldBe 180f
            location.rotation.z shouldBe 0f
        }

        test("should roundtrip Location serialization") {
            val original = Location("nether", Vector3d(10.0, 20.0, 30.0), Vector3f(45f, 90f, 0f))
            val json = Json.encodeToString(LocationSerializer, original)
            val deserialized = Json.decodeFromString(LocationSerializer, json)
            deserialized.world shouldBe original.world
            deserialized.position.x shouldBe original.position.x
            deserialized.position.y shouldBe original.position.y
            deserialized.position.z shouldBe original.position.z
            deserialized.rotation.x shouldBe original.rotation.x
            deserialized.rotation.y shouldBe original.rotation.y
            deserialized.rotation.z shouldBe original.rotation.z
        }

        test("should handle null world") {
            val location = Location(null, Vector3d(1.0, 2.0, 3.0), Vector3f(0f, 0f, 0f))
            val json = Json.encodeToString(LocationSerializer, location)
            val decoded = Json.decodeFromString(LocationSerializer, json)
            decoded.world shouldBe null
            decoded.position.x shouldBe location.position.x
            decoded.position.y shouldBe location.position.y
            decoded.position.z shouldBe location.position.z
        }
    })
