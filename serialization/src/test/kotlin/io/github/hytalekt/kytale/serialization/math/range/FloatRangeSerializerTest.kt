package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.FloatRange
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class FloatRangeSerializerTest :
    FunSpec({
        test("should serialize FloatRange to JSON array") {
            val range = FloatRange(0.5f, 1.5f)
            val json = Json.encodeToString(FloatRangeSerializer, range)
            json shouldBe """[0.5,1.5]"""
        }

        test("should deserialize FloatRange from JSON array") {
            val json = """[0.5,1.5]"""
            val range = Json.decodeFromString(FloatRangeSerializer, json)
            range.inclusiveMin shouldBe 0.5f
            range.inclusiveMax shouldBe 1.5f
        }

        test("should roundtrip FloatRange serialization") {
            val original = FloatRange(10.5f, 20.5f)
            val json = Json.encodeToString(FloatRangeSerializer, original)
            val deserialized = Json.decodeFromString(FloatRangeSerializer, json)
            deserialized.inclusiveMin shouldBe original.inclusiveMin
            deserialized.inclusiveMax shouldBe original.inclusiveMax
        }

        test("should handle negative values") {
            val range = FloatRange(-10.5f, -5.5f)
            val json = Json.encodeToString(FloatRangeSerializer, range)
            val decoded = Json.decodeFromString(FloatRangeSerializer, json)
            decoded.inclusiveMin shouldBe range.inclusiveMin
            decoded.inclusiveMax shouldBe range.inclusiveMax
        }

        test("should handle zero range") {
            val range = FloatRange(0.0f, 0.0f)
            val json = Json.encodeToString(FloatRangeSerializer, range)
            val decoded = Json.decodeFromString(FloatRangeSerializer, json)
            decoded.inclusiveMin shouldBe 0.0f
            decoded.inclusiveMax shouldBe 0.0f
        }
    })
