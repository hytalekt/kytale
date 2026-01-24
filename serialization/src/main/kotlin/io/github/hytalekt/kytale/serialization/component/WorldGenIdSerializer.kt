package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.component.WorldGenId
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

object WorldGenIdSerializer : KSerializer<WorldGenId> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("WorldGenId") {
            element<Int>("worldGenId")
        }

    override fun serialize(
        encoder: Encoder,
        value: WorldGenId,
    ) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.worldGenId)
    }

    override fun deserialize(decoder: Decoder): WorldGenId =
        decoder.decodeStructure(descriptor) {
            var worldGenId = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> worldGenId = decodeIntElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            WorldGenId(worldGenId)
        }
}

typealias KWorldGenId =
    @Serializable(with = WorldGenIdSerializer::class)
    WorldGenId
