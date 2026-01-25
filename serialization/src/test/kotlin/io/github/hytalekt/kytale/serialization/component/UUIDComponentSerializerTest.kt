package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.entity.UUIDComponent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import java.util.UUID

class UUIDComponentSerializerTest :
    FunSpec({
        test("should serialize UUIDComponent to plain UUID string") {
            val uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
            val component = UUIDComponent(uuid)
            val json = Json.encodeToString(UUIDComponentSerializer, component)
            json shouldBe "\"550e8400-e29b-41d4-a716-446655440000\""
        }

        test("should deserialize UUIDComponent from plain UUID string") {
            val json = "\"550e8400-e29b-41d4-a716-446655440000\""
            val component = Json.decodeFromString(UUIDComponentSerializer, json)
            component.uuid shouldBe UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        }

        test("should roundtrip UUIDComponent serialization") {
            val uuid = UUID.randomUUID()
            val original = UUIDComponent(uuid)
            val json = Json.encodeToString(UUIDComponentSerializer, original)
            val deserialized = Json.decodeFromString(UUIDComponentSerializer, json)
            deserialized.uuid shouldBe original.uuid
        }

        test("should handle different UUID formats") {
            val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
            val component = UUIDComponent(uuid)
            val json = Json.encodeToString(UUIDComponentSerializer, component)
            val decoded = Json.decodeFromString(UUIDComponentSerializer, json)
            decoded.uuid shouldBe uuid
        }
    })
