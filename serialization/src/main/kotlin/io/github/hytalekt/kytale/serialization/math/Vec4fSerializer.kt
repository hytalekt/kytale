package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Vec4f
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

object Vec4fSerializer : KSerializer<Vec4f> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vec4f") {
            element<Float>("x")
            element<Float>("y")
            element<Float>("z")
            element<Float>("w")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vec4f,
    ) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.x)
        encodeFloatElement(descriptor, 1, value.y)
        encodeFloatElement(descriptor, 2, value.z)
        encodeFloatElement(descriptor, 3, value.w)
    }

    override fun deserialize(decoder: Decoder): Vec4f =
        decoder.decodeStructure(descriptor) {
            var x = 0f
            var y = 0f
            var z = 0f
            var w = 0f
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeFloatElement(descriptor, 0)
                    1 -> y = decodeFloatElement(descriptor, 1)
                    2 -> z = decodeFloatElement(descriptor, 2)
                    3 -> w = decodeFloatElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vec4f(x, y, z, w)
        }
}

typealias KVec4f =
    @Serializable(with = Vec4fSerializer::class)
    Vec4f
