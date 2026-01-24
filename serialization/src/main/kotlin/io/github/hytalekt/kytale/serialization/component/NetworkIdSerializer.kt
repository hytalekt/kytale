package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId
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

object NetworkIdSerializer : KSerializer<NetworkId> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("NetworkId") {
            element<Int>("id")
        }

    override fun serialize(
        encoder: Encoder,
        value: NetworkId,
    ) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.id)
    }

    override fun deserialize(decoder: Decoder): NetworkId =
        decoder.decodeStructure(descriptor) {
            var id = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeIntElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            NetworkId(id)
        }
}

typealias KNetworkId =
    @Serializable(with = NetworkIdSerializer::class)
    NetworkId
