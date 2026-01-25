package io.github.hytalekt.kytale.serialization.math.range

import com.hypixel.hytale.math.range.FloatRange
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.FloatArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object FloatRangeSerializer : KSerializer<FloatRange> {
    private val delegateSerializer = FloatArraySerializer()
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: FloatRange,
    ) {
        val array = floatArrayOf(value.inclusiveMin, value.inclusiveMax)
        encoder.encodeSerializableValue(delegateSerializer, array)
    }

    override fun deserialize(decoder: Decoder): FloatRange {
        val array = decoder.decodeSerializableValue(delegateSerializer)
        require(array.size == 2) { "FloatRange requires exactly 2 elements [min, max], got ${array.size}" }
        return FloatRange(array[0], array[1])
    }
}

/**
 * Serializable typealias for [FloatRange].
 *
 * Serialized shape: `[min, max]: float[]`
 */
typealias KFloatRange =
    @Serializable(with = FloatRangeSerializer::class)
    FloatRange
