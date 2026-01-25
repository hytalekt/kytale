package io.github.hytalekt.kytale.serialization.player

import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class PlayerDeathPositionDataSerializerTest :
    FunSpec({
        test("should serialize PlayerDeathPositionData to JSON") {
            val transform = Transform(Vector3d(100.0, 64.0, 200.0), Vector3f(0f, 90f, 0f))
            val data = PlayerDeathPositionData("marker1", transform, 5)
            val json = Json.encodeToString(PlayerDeathPositionDataSerializer, data)
            json shouldBe """{"markerId":"marker1","transform":{"position":{"x":100.0,"y":64.0,"z":200.0},"rotation":{"x":0.0,"y":90.0,"z":0.0}},"day":5}"""
        }

        test("should deserialize PlayerDeathPositionData from JSON") {
            val json = """{"markerId":"marker1","transform":{"position":{"x":100.0,"y":64.0,"z":200.0},"rotation":{"x":0.0,"y":90.0,"z":0.0}},"day":5}"""
            val data = Json.decodeFromString(PlayerDeathPositionDataSerializer, json)
            data.markerId shouldBe "marker1"
            data.transform.position.x shouldBe 100.0
            data.transform.position.y shouldBe 64.0
            data.transform.position.z shouldBe 200.0
            data.transform.rotation.x shouldBe 0f
            data.transform.rotation.y shouldBe 90f
            data.transform.rotation.z shouldBe 0f
            data.day shouldBe 5
        }

        test("should roundtrip PlayerDeathPositionData serialization") {
            val transform = Transform(Vector3d(50.0, 70.0, 30.0), Vector3f(45f, 180f, 0f))
            val original = PlayerDeathPositionData("death_marker_2", transform, 10)
            val json = Json.encodeToString(PlayerDeathPositionDataSerializer, original)
            val deserialized = Json.decodeFromString(PlayerDeathPositionDataSerializer, json)
            deserialized.markerId shouldBe original.markerId
            deserialized.transform.position.x shouldBe original.transform.position.x
            deserialized.transform.position.y shouldBe original.transform.position.y
            deserialized.transform.position.z shouldBe original.transform.position.z
            deserialized.transform.rotation.x shouldBe original.transform.rotation.x
            deserialized.transform.rotation.y shouldBe original.transform.rotation.y
            deserialized.transform.rotation.z shouldBe original.transform.rotation.z
            deserialized.day shouldBe original.day
        }

        test("should handle day zero") {
            val transform = Transform(Vector3d(0.0, 0.0, 0.0), Vector3f(0f, 0f, 0f))
            val data = PlayerDeathPositionData("marker_day0", transform, 0)
            val json = Json.encodeToString(PlayerDeathPositionDataSerializer, data)
            val decoded = Json.decodeFromString(PlayerDeathPositionDataSerializer, json)
            decoded.day shouldBe 0
        }
    })
