package io.github.hytalekt.kytale.serialization.common

import com.hypixel.hytale.common.semver.Semver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SemverSerializerTest :
    FunSpec({
        test("should serialize Semver to JSON") {
            val semver = Semver(1, 2, 3)
            val json = Json.encodeToString(SemverSerializer, semver)
            json shouldBe """{"major":1,"minor":2,"patch":3,"preRelease":null,"build":null}"""
        }

        test("should serialize Semver with preRelease to JSON") {
            val semver = Semver(1, 0, 0, arrayOf("alpha", "1"), null)
            val json = Json.encodeToString(SemverSerializer, semver)
            json shouldBe """{"major":1,"minor":0,"patch":0,"preRelease":["alpha","1"],"build":null}"""
        }

        test("should serialize Semver with build to JSON") {
            val semver = Semver(2, 1, 0, null, "build123")
            val json = Json.encodeToString(SemverSerializer, semver)
            json shouldBe """{"major":2,"minor":1,"patch":0,"preRelease":null,"build":"build123"}"""
        }

        test("should deserialize Semver from JSON") {
            val json = """{"major":1,"minor":2,"patch":3,"preRelease":null,"build":null}"""
            val semver = Json.decodeFromString(SemverSerializer, json)
            semver.major shouldBe 1
            semver.minor shouldBe 2
            semver.patch shouldBe 3
        }

        test("should roundtrip Semver serialization") {
            val original = Semver(3, 2, 1, arrayOf("beta", "2"), "build456")
            val json = Json.encodeToString(SemverSerializer, original)
            val deserialized = Json.decodeFromString(SemverSerializer, json)
            deserialized.major shouldBe original.major
            deserialized.minor shouldBe original.minor
            deserialized.patch shouldBe original.patch
            deserialized.build shouldBe original.build
        }

        test("should handle version with all fields") {
            val semver = Semver(4, 5, 6, arrayOf("rc", "1"), "metadata")
            val json = Json.encodeToString(SemverSerializer, semver)
            val decoded = Json.decodeFromString(SemverSerializer, json)
            decoded.major shouldBe semver.major
            decoded.minor shouldBe semver.minor
            decoded.patch shouldBe semver.patch
        }
    })
