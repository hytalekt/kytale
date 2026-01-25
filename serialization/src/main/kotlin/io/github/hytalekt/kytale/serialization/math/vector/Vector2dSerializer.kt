package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector2d
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

object Vector2dSerializer : KSerializer<Vector2d> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector2d") {
            element<Double>("x")
            element<Double>("y")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector2d,
    ) = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.x)
        encodeDoubleElement(descriptor, 1, value.y)
    }

    override fun deserialize(decoder: Decoder): Vector2d =
        decoder.decodeStructure(descriptor) {
            var x = 0.0
            var y = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector2d(x, y)
        }
}

/**
 * Serializable typealias for [Vector2d].
 *
 * Serialized shape: `{ x, y: double }`
 */
typealias KVector2d =
    @Serializable(with = Vector2dSerializer::class)
    Vector2d
