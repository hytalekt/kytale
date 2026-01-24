package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector3i
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

object Vector3iSerializer : KSerializer<Vector3i> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3i") {
            element<Int>("x")
            element<Int>("y")
            element<Int>("z")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector3i,
    ) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.x)
        encodeIntElement(descriptor, 1, value.y)
        encodeIntElement(descriptor, 2, value.z)
    }

    override fun deserialize(decoder: Decoder): Vector3i =
        decoder.decodeStructure(descriptor) {
            var x = 0
            var y = 0
            var z = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeIntElement(descriptor, 0)
                    1 -> y = decodeIntElement(descriptor, 1)
                    2 -> z = decodeIntElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector3i(x, y, z)
        }
}

typealias KVector3i =
    @Serializable(with = Vector3iSerializer::class)
    Vector3i
