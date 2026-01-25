package io.github.hytalekt.kytale.serialization.player

import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData
import io.github.hytalekt.kytale.serialization.math.vector.TransformSerializer
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

object PlayerDeathPositionDataSerializer : KSerializer<PlayerDeathPositionData> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PlayerDeathPositionData") {
            element<String>("markerId")
            element("transform", TransformSerializer.descriptor)
            element<Int>("day")
        }

    override fun serialize(
        encoder: Encoder,
        value: PlayerDeathPositionData,
    ) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.markerId)
        encodeSerializableElement(descriptor, 1, TransformSerializer, value.transform)
        encodeIntElement(descriptor, 2, value.day)
    }

    override fun deserialize(decoder: Decoder): PlayerDeathPositionData =
        decoder.decodeStructure(descriptor) {
            var markerId: String? = null
            var transform: Transform? = null
            var day = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> markerId = decodeStringElement(descriptor, 0)
                    1 -> transform = decodeSerializableElement(descriptor, 1, TransformSerializer)
                    2 -> day = decodeIntElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            PlayerDeathPositionData(markerId!!, transform!!, day)
        }
}

/**
 * Serializable typealias for [PlayerDeathPositionData].
 *
 * Serialized shape: `{ markerId: string, transform: Transform, day: int }`
 */
typealias KPlayerDeathPositionData =
    @Serializable(with = PlayerDeathPositionDataSerializer::class)
    PlayerDeathPositionData
