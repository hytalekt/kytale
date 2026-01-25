package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.component.WorldGenId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class WorldGenIdSerializerTest :
    FunSpec({
        test("should serialize WorldGenId to plain integer") {
            val worldGenId = WorldGenId(456)
            val json = Json.encodeToString(WorldGenIdSerializer, worldGenId)
            json shouldBe "456"
        }

        test("should deserialize WorldGenId from plain integer") {
            val json = "456"
            val worldGenId = Json.decodeFromString(WorldGenIdSerializer, json)
            worldGenId.worldGenId shouldBe 456
        }

        test("should roundtrip WorldGenId serialization") {
            val original = WorldGenId(789)
            val json = Json.encodeToString(WorldGenIdSerializer, original)
            val deserialized = Json.decodeFromString(WorldGenIdSerializer, json)
            deserialized.worldGenId shouldBe original.worldGenId
        }

        test("should handle zero value") {
            val worldGenId = WorldGenId(0)
            val json = Json.encodeToString(WorldGenIdSerializer, worldGenId)
            val decoded = Json.decodeFromString(WorldGenIdSerializer, json)
            decoded.worldGenId shouldBe 0
        }

        test("should handle large values") {
            val worldGenId = WorldGenId(Int.MAX_VALUE)
            val json = Json.encodeToString(WorldGenIdSerializer, worldGenId)
            val decoded = Json.decodeFromString(WorldGenIdSerializer, json)
            decoded.worldGenId shouldBe Int.MAX_VALUE
        }
    })
