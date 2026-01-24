package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.IntRange
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

object IntRangeSerializer : KSerializer<IntRange> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("IntRange") {
            element<Int>("inclusiveMin")
            element<Int>("inclusiveMax")
        }

    override fun serialize(
        encoder: Encoder,
        value: IntRange,
    ) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.inclusiveMin)
        encodeIntElement(descriptor, 1, value.inclusiveMax)
    }

    override fun deserialize(decoder: Decoder): IntRange =
        decoder.decodeStructure(descriptor) {
            var inclusiveMin = 0
            var inclusiveMax = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> inclusiveMin = decodeIntElement(descriptor, 0)
                    1 -> inclusiveMax = decodeIntElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            IntRange(inclusiveMin, inclusiveMax)
        }
}

typealias KIntRange =
    @Serializable(with = IntRangeSerializer::class)
    IntRange
