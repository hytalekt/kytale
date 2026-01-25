package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.IntRange
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class IntRangeSerializerTest :
    FunSpec({
        test("should serialize IntRange to JSON array") {
            val range = IntRange(10, 20)
            val json = Json.encodeToString(IntRangeSerializer, range)
            json shouldBe """[10,20]"""
        }

        test("should deserialize IntRange from JSON array") {
            val json = """[10,20]"""
            val range = Json.decodeFromString(IntRangeSerializer, json)
            range.inclusiveMin shouldBe 10
            range.inclusiveMax shouldBe 20
        }

        test("should roundtrip IntRange serialization") {
            val original = IntRange(100, 200)
            val json = Json.encodeToString(IntRangeSerializer, original)
            val deserialized = Json.decodeFromString(IntRangeSerializer, json)
            deserialized.inclusiveMin shouldBe original.inclusiveMin
            deserialized.inclusiveMax shouldBe original.inclusiveMax
        }

        test("should handle negative values") {
            val range = IntRange(-100, -50)
            val json = Json.encodeToString(IntRangeSerializer, range)
            val decoded = Json.decodeFromString(IntRangeSerializer, json)
            decoded.inclusiveMin shouldBe range.inclusiveMin
            decoded.inclusiveMax shouldBe range.inclusiveMax
        }

        test("should handle zero range") {
            val range = IntRange(0, 0)
            val json = Json.encodeToString(IntRangeSerializer, range)
            val decoded = Json.decodeFromString(IntRangeSerializer, json)
            decoded.inclusiveMin shouldBe 0
            decoded.inclusiveMax shouldBe 0
        }
    })
