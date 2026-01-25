package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3l
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

object Vector3lSerializer : KSerializer<Vector3l> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3l") {
            element<Long>("x")
            element<Long>("y")
            element<Long>("z")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector3l,
    ) = encoder.encodeStructure(descriptor) {
        encodeLongElement(descriptor, 0, value.x)
        encodeLongElement(descriptor, 1, value.y)
        encodeLongElement(descriptor, 2, value.z)
    }

    override fun deserialize(decoder: Decoder): Vector3l =
        decoder.decodeStructure(descriptor) {
            var x = 0L
            var y = 0L
            var z = 0L
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeLongElement(descriptor, 0)
                    1 -> y = decodeLongElement(descriptor, 1)
                    2 -> z = decodeLongElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector3l(x, y, z)
        }
}

/**
 * Serializable typealias for [Vector3l].
 *
 * Serialized shape: `{ x, y, z: long }`
 */
typealias KVector3l =
    @Serializable(with = Vector3lSerializer::class)
    Vector3l
