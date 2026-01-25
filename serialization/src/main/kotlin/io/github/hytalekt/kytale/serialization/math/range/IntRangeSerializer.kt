package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.IntRange
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object IntRangeSerializer : KSerializer<IntRange> {
    private val delegateSerializer = IntArraySerializer()
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: IntRange,
    ) {
        val array = intArrayOf(value.inclusiveMin, value.inclusiveMax)
        encoder.encodeSerializableValue(delegateSerializer, array)
    }

    override fun deserialize(decoder: Decoder): IntRange {
        val array = decoder.decodeSerializableValue(delegateSerializer)
        require(array.size == 2) { "IntRange requires exactly 2 elements [min, max], got ${array.size}" }
        return IntRange(array[0], array[1])
    }
}

/**
 * Serializable typealias for [IntRange].
 *
 * Serialized shape: `[min, max]: int[]`
 */
typealias KIntRange =
    @Serializable(with = IntRangeSerializer::class)
    IntRange
