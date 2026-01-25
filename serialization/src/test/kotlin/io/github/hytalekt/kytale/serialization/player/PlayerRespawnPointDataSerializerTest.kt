package io.github.hytalekt.kytale.serialization.player

import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerRespawnPointData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class PlayerRespawnPointDataSerializerTest :
    FunSpec({
        test("should serialize PlayerRespawnPointData to JSON") {
            val data = PlayerRespawnPointData(
                Vector3i(100, 64, 200),
                Vector3d(100.5, 64.0, 200.5),
                "Home",
            )
            val json = Json.encodeToString(PlayerRespawnPointDataSerializer, data)
            json shouldBe """{"blockPosition":{"x":100,"y":64,"z":200},"respawnPosition":{"x":100.5,"y":64.0,"z":200.5},"name":"Home"}"""
        }

        test("should deserialize PlayerRespawnPointData from JSON") {
            val json = """{"blockPosition":{"x":100,"y":64,"z":200},"respawnPosition":{"x":100.5,"y":64.0,"z":200.5},"name":"Home"}"""
            val data = Json.decodeFromString(PlayerRespawnPointDataSerializer, json)
            data.blockPosition.x shouldBe 100
            data.blockPosition.y shouldBe 64
            data.blockPosition.z shouldBe 200
            data.respawnPosition.x shouldBe 100.5
            data.respawnPosition.y shouldBe 64.0
            data.respawnPosition.z shouldBe 200.5
            data.name shouldBe "Home"
        }

        test("should roundtrip PlayerRespawnPointData serialization") {
            val original = PlayerRespawnPointData(
                Vector3i(50, 70, 30),
                Vector3d(50.5, 70.0, 30.5),
                "Spawn Point",
            )
            val json = Json.encodeToString(PlayerRespawnPointDataSerializer, original)
            val deserialized = Json.decodeFromString(PlayerRespawnPointDataSerializer, json)
            deserialized.blockPosition.x shouldBe original.blockPosition.x
            deserialized.blockPosition.y shouldBe original.blockPosition.y
            deserialized.blockPosition.z shouldBe original.blockPosition.z
            deserialized.respawnPosition.x shouldBe original.respawnPosition.x
            deserialized.respawnPosition.y shouldBe original.respawnPosition.y
            deserialized.respawnPosition.z shouldBe original.respawnPosition.z
            deserialized.name shouldBe original.name
        }

        test("should handle negative coordinates") {
            val data = PlayerRespawnPointData(
                Vector3i(-100, 64, -200),
                Vector3d(-100.5, 64.0, -200.5),
                "Nether Base",
            )
            val json = Json.encodeToString(PlayerRespawnPointDataSerializer, data)
            val decoded = Json.decodeFromString(PlayerRespawnPointDataSerializer, json)
            decoded.blockPosition.x shouldBe -100
            decoded.blockPosition.z shouldBe -200
            decoded.respawnPosition.x shouldBe -100.5
            decoded.respawnPosition.z shouldBe -200.5
        }
    })
