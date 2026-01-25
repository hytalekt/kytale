package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3d
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

object Vector3dSerializer : KSerializer<Vector3d> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3d") {
            element<Double>("x")
            element<Double>("y")
            element<Double>("z")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector3d,
    ) = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.x)
        encodeDoubleElement(descriptor, 1, value.y)
        encodeDoubleElement(descriptor, 2, value.z)
    }

    override fun deserialize(decoder: Decoder): Vector3d =
        decoder.decodeStructure(descriptor) {
            var x = 0.0
            var y = 0.0
            var z = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    2 -> z = decodeDoubleElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector3d(x, y, z)
        }
}

/**
 * Serializable typealias for [Vector3d].
 *
 * Serialized shape: `{ x, y, z: double }`
 */
typealias KVector3d =
    @Serializable(with = Vector3dSerializer::class)
    Vector3d
