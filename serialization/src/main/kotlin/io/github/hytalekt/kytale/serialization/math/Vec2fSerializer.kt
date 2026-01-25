package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Vec2f
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

object Vec2fSerializer : KSerializer<Vec2f> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vec2f") {
            element<Float>("x")
            element<Float>("y")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vec2f,
    ) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.x)
        encodeFloatElement(descriptor, 1, value.y)
    }

    override fun deserialize(decoder: Decoder): Vec2f =
        decoder.decodeStructure(descriptor) {
            var x = 0f
            var y = 0f
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeFloatElement(descriptor, 0)
                    1 -> y = decodeFloatElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vec2f(x, y)
        }
}

/**
 * Serializable typealias for [Vec2f].
 *
 * Serialized shape: `{ x, y: float }`
 */
typealias KVec2f =
    @Serializable(with = Vec2fSerializer::class)
    Vec2f
