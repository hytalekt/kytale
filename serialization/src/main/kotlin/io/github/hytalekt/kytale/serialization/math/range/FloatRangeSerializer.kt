package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.FloatRange
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

object FloatRangeSerializer : KSerializer<FloatRange> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("FloatRange") {
            element<Float>("inclusiveMin")
            element<Float>("inclusiveMax")
        }

    override fun serialize(
        encoder: Encoder,
        value: FloatRange,
    ) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.inclusiveMin)
        encodeFloatElement(descriptor, 1, value.inclusiveMax)
    }

    override fun deserialize(decoder: Decoder): FloatRange =
        decoder.decodeStructure(descriptor) {
            var inclusiveMin = 0f
            var inclusiveMax = 0f
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> inclusiveMin = decodeFloatElement(descriptor, 0)
                    1 -> inclusiveMax = decodeFloatElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            FloatRange(inclusiveMin, inclusiveMax)
        }
}

typealias KFloatRange =
    @Serializable(with = FloatRangeSerializer::class)
    FloatRange
