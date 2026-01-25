package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Vector2l
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

object Vector2lSerializer : KSerializer<Vector2l> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector2l") {
            element<Long>("x")
            element<Long>("y")
        }

    override fun serialize(
        encoder: Encoder,
        value: Vector2l,
    ) = encoder.encodeStructure(descriptor) {
        encodeLongElement(descriptor, 0, value.x)
        encodeLongElement(descriptor, 1, value.y)
    }

    override fun deserialize(decoder: Decoder): Vector2l =
        decoder.decodeStructure(descriptor) {
            var x = 0L
            var y = 0L
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeLongElement(descriptor, 0)
                    1 -> y = decodeLongElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Vector2l(x, y)
        }
}

/**
 * Serializable typealias for [Vector2l].
 *
 * Serialized shape: `{ x, y: long }`
 */
typealias KVector2l =
    @Serializable(with = Vector2lSerializer::class)
    Vector2l
