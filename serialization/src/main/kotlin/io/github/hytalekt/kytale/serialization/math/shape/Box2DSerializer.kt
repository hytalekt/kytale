package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Box2D
import com.hypixel.hytale.math.vector.Vector2d
import io.github.hytalekt.kytale.serialization.math.vector.Vector2dSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object Box2DSerializer : KSerializer<Box2D> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Box2D") {
            element("min", Vector2dSerializer.descriptor)
            element("max", Vector2dSerializer.descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Box2D,
    ) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, Vector2dSerializer, value.min)
        encodeSerializableElement(descriptor, 1, Vector2dSerializer, value.max)
    }

    override fun deserialize(decoder: Decoder): Box2D =
        decoder.decodeStructure(descriptor) {
            var min: Vector2d? = null
            var max: Vector2d? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> min = decodeSerializableElement(descriptor, 0, Vector2dSerializer)
                    1 -> max = decodeSerializableElement(descriptor, 1, Vector2dSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Box2D(min!!, max!!)
        }
}

typealias KBox2D =
    @Serializable(with = Box2DSerializer::class)
    Box2D
