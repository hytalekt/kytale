package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Cylinder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class CylinderSerializerTest :
    FunSpec({
        test("should serialize Cylinder to JSON") {
            val cylinder = Cylinder(10.0, 2.0, 3.0)
            val json = Json.encodeToString(CylinderSerializer, cylinder)
            json shouldBe """{"height":10.0,"radiusX":2.0,"radiusZ":3.0}"""
        }

        test("should deserialize Cylinder from JSON") {
            val json = """{"height":10.0,"radiusX":2.0,"radiusZ":3.0}"""
            val cylinder = Json.decodeFromString(CylinderSerializer, json)
            cylinder.height shouldBe 10.0
            cylinder.radiusX shouldBe 2.0
            cylinder.radiusZ shouldBe 3.0
        }

        test("should roundtrip Cylinder serialization") {
            val original = Cylinder(20.5, 5.5, 7.5)
            val json = Json.encodeToString(CylinderSerializer, original)
            val deserialized = Json.decodeFromString(CylinderSerializer, json)
            deserialized.height shouldBe original.height
            deserialized.radiusX shouldBe original.radiusX
            deserialized.radiusZ shouldBe original.radiusZ
        }

        test("should handle circular cylinder (equal radii)") {
            val cylinder = Cylinder(15.0, 5.0, 5.0)
            val json = Json.encodeToString(CylinderSerializer, cylinder)
            val decoded = Json.decodeFromString(CylinderSerializer, json)
            decoded.height shouldBe 15.0
            decoded.radiusX shouldBe 5.0
            decoded.radiusZ shouldBe 5.0
        }
    })
