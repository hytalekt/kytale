package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Mat4f
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Mat4fSerializerTest :
    FunSpec({
        test("should serialize Mat4f identity matrix as 2D array") {
            val mat = Mat4f.identity()
            val json = Json.encodeToString(Mat4fSerializer, mat)
            json shouldBe """[[1.0,0.0,0.0,0.0],[0.0,1.0,0.0,0.0],[0.0,0.0,1.0,0.0],[0.0,0.0,0.0,1.0]]"""
        }

        test("should deserialize Mat4f from 2D array") {
            val json = """[[1.0,2.0,3.0,4.0],[5.0,6.0,7.0,8.0],[9.0,10.0,11.0,12.0],[13.0,14.0,15.0,16.0]]"""
            val mat = Json.decodeFromString(Mat4fSerializer, json)
            mat.m11 shouldBe 1.0f
            mat.m12 shouldBe 2.0f
            mat.m21 shouldBe 5.0f
            mat.m22 shouldBe 6.0f
            mat.m44 shouldBe 16.0f
        }

        test("should roundtrip Mat4f serialization") {
            val original =
                Mat4f(
                    1f,
                    2f,
                    3f,
                    4f,
                    5f,
                    6f,
                    7f,
                    8f,
                    9f,
                    10f,
                    11f,
                    12f,
                    13f,
                    14f,
                    15f,
                    16f,
                )
            val json = Json.encodeToString(Mat4fSerializer, original)
            val deserialized = Json.decodeFromString(Mat4fSerializer, json)
            deserialized.m11 shouldBe original.m11
            deserialized.m22 shouldBe original.m22
            deserialized.m33 shouldBe original.m33
            deserialized.m44 shouldBe original.m44
        }

        test("should handle custom scale matrix") {
            val mat = Mat4f(2f, 0f, 0f, 0f, 0f, 2f, 0f, 0f, 0f, 0f, 2f, 0f, 0f, 0f, 0f, 1f)
            val json = Json.encodeToString(Mat4fSerializer, mat)
            // Scale matrix: diagonal has 2,2,2,1
            json shouldBe """[[2.0,0.0,0.0,0.0],[0.0,2.0,0.0,0.0],[0.0,0.0,2.0,0.0],[0.0,0.0,0.0,1.0]]"""
            val decoded = Json.decodeFromString(Mat4fSerializer, json)
            decoded.m11 shouldBe 2f
            decoded.m22 shouldBe 2f
            decoded.m33 shouldBe 2f
            decoded.m44 shouldBe 1f
        }

        test("should serialize with pretty printing for readability") {
            val mat =
                Mat4f(
                    1f,
                    0f,
                    0f,
                    10f,
                    0f,
                    1f,
                    0f,
                    20f,
                    0f,
                    0f,
                    1f,
                    30f,
                    0f,
                    0f,
                    0f,
                    1f,
                )
            val json =
                Json {
                    prettyPrint = true
                }.encodeToString(Mat4fSerializer, mat)
            // Translation matrix (10, 20, 30)
            json shouldBe """[
    [
        1.0,
        0.0,
        0.0,
        10.0
    ],
    [
        0.0,
        1.0,
        0.0,
        20.0
    ],
    [
        0.0,
        0.0,
        1.0,
        30.0
    ],
    [
        0.0,
        0.0,
        0.0,
        1.0
    ]
]"""
        }
    })
