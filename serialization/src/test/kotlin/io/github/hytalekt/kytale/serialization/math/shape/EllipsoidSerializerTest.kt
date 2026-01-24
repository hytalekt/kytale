package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Ellipsoid
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class EllipsoidSerializerTest :
    FunSpec({
        test("should serialize Ellipsoid to JSON") {
            val ellipsoid = Ellipsoid(1.0, 2.0, 3.0)
            val json = Json.encodeToString(EllipsoidSerializer, ellipsoid)
            json shouldBe """{"radiusX":1.0,"radiusY":2.0,"radiusZ":3.0}"""
        }

        test("should deserialize Ellipsoid from JSON") {
            val json = """{"radiusX":1.0,"radiusY":2.0,"radiusZ":3.0}"""
            val ellipsoid = Json.decodeFromString(EllipsoidSerializer, json)
            ellipsoid.radiusX shouldBe 1.0
            ellipsoid.radiusY shouldBe 2.0
            ellipsoid.radiusZ shouldBe 3.0
        }

        test("should roundtrip Ellipsoid serialization") {
            val original = Ellipsoid(5.5, 10.5, 15.5)
            val json = Json.encodeToString(EllipsoidSerializer, original)
            val deserialized = Json.decodeFromString(EllipsoidSerializer, json)
            deserialized.radiusX shouldBe original.radiusX
            deserialized.radiusY shouldBe original.radiusY
            deserialized.radiusZ shouldBe original.radiusZ
        }

        test("should handle sphere (equal radii)") {
            val ellipsoid = Ellipsoid(5.0, 5.0, 5.0)
            val json = Json.encodeToString(EllipsoidSerializer, ellipsoid)
            val decoded = Json.decodeFromString(EllipsoidSerializer, json)
            decoded.radiusX shouldBe 5.0
            decoded.radiusY shouldBe 5.0
            decoded.radiusZ shouldBe 5.0
        }
    })
