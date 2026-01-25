package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3f
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object Vector3fSerializer : KSerializer<Vector3f> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3f") {
            element<Float>("x")
            element<Float>("y")
            element<Float>("z")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector3f,
    ) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.x)
        encodeFloatElement(descriptor, 1, value.y)
        encodeFloatElement(descriptor, 2, value.z)
    }

    override fun deserialize(decoder: Decoder): Vector3f =
        decoder.decodeStructure(descriptor) {
            var x = 0f
            var y = 0f
            var z = 0f
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeFloatElement(descriptor, 0)
                    1 -> y = decodeFloatElement(descriptor, 1)
                    2 -> z = decodeFloatElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector3f(x, y, z)
        }
}

/**
 * Serializable typealias for [Vector3f].
 *
 * Serialized shape: `{ x, y, z: float }`
 */
typealias KVector3f =
    @Serializable(with = Vector3fSerializer::class)
    Vector3f
