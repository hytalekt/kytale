@file:OptIn(ExperimentalSerializationApi::class)

package io.github.hytalekt.kytale.serialization.common

import com.hypixel.hytale.common.semver.Semver
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object SemverSerializer : KSerializer<Semver> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Semver") {
            element<Long>("major")
            element<Long>("minor")
            element<Long>("patch")
            element<Array<String>?>("preRelease")
            element<String?>("build")
        }

    override fun serialize(
        encoder: Encoder,
        value: Semver,
    ) = encoder.encodeStructure(descriptor) {
        encodeLongElement(descriptor, 0, value.major)
        encodeLongElement(descriptor, 1, value.minor)
        encodeLongElement(descriptor, 2, value.patch)
        // getPreRelease() clones the array, which throws NPE if null
        val preRelease =
            try {
                value.preRelease
            } catch (_: NullPointerException) {
                null
            }
        encodeNullableSerializableElement(
            descriptor,
            3,
            ArraySerializer(String.serializer()),
            preRelease,
        )
        encodeNullableSerializableElement(descriptor, 4, String.serializer(), value.build)
    }

    override fun deserialize(decoder: Decoder): Semver =
        decoder.decodeStructure(descriptor) {
            var major = 0L
            var minor = 0L
            var patch = 0L
            var preRelease: Array<String>? = null
            var build: String? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> {
                        major = decodeLongElement(descriptor, 0)
                    }

                    1 -> {
                        minor = decodeLongElement(descriptor, 1)
                    }

                    2 -> {
                        patch = decodeLongElement(descriptor, 2)
                    }

                    3 -> {
                        preRelease =
                            decodeNullableSerializableElement(
                                descriptor,
                                3,
                                ArraySerializer(String.serializer()),
                            )
                    }

                    4 -> {
                        build = decodeNullableSerializableElement(descriptor, 4, String.serializer())
                    }

                    CompositeDecoder.DECODE_DONE -> {
                        break
                    }

                    else -> {
                        error("Unexpected index: $index")
                    }
                }
            }
            Semver(major, minor, patch, preRelease, build)
        }
}

/**
 * Serializable typealias for [Semver].
 *
 * Serialized shape: `{ major, minor, patch: long, preRelease: string[]?, build: string? }`
 */
typealias KSemver =
    @Serializable(with = SemverSerializer::class)
    Semver
