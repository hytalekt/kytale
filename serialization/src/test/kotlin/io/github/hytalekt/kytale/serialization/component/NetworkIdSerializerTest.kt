package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class NetworkIdSerializerTest :
    FunSpec({
        test("should serialize NetworkId to plain integer") {
            val networkId = NetworkId(123)
            val json = Json.encodeToString(NetworkIdSerializer, networkId)
            json shouldBe "123"
        }

        test("should deserialize NetworkId from plain integer") {
            val json = "123"
            val networkId = Json.decodeFromString(NetworkIdSerializer, json)
            networkId.id shouldBe 123
        }

        test("should roundtrip NetworkId serialization") {
            val original = NetworkId(456)
            val json = Json.encodeToString(NetworkIdSerializer, original)
            val deserialized = Json.decodeFromString(NetworkIdSerializer, json)
            deserialized.id shouldBe original.id
        }

        test("should handle zero value") {
            val networkId = NetworkId(0)
            val json = Json.encodeToString(NetworkIdSerializer, networkId)
            val decoded = Json.decodeFromString(NetworkIdSerializer, json)
            decoded.id shouldBe 0
        }

        test("should handle large values") {
            val networkId = NetworkId(Int.MAX_VALUE)
            val json = Json.encodeToString(NetworkIdSerializer, networkId)
            val decoded = Json.decodeFromString(NetworkIdSerializer, json)
            decoded.id shouldBe Int.MAX_VALUE
        }
    })
