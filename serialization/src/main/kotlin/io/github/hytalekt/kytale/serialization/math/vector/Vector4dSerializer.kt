package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector4d
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

object Vector4dSerializer : KSerializer<Vector4d> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector4d") {
            element<Double>("x")
            element<Double>("y")
            element<Double>("z")
            element<Double>("w")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector4d,
    ) = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.x)
        encodeDoubleElement(descriptor, 1, value.y)
        encodeDoubleElement(descriptor, 2, value.z)
        encodeDoubleElement(descriptor, 3, value.w)
    }

    override fun deserialize(decoder: Decoder): Vector4d =
        decoder.decodeStructure(descriptor) {
            var x = 0.0
            var y = 0.0
            var z = 0.0
            var w = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    2 -> z = decodeDoubleElement(descriptor, 2)
                    3 -> w = decodeDoubleElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector4d(x, y, z, w)
        }
}

typealias KVector4d =
    @Serializable(with = Vector4dSerializer::class)
    Vector4d
