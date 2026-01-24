package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for NetworkId that encodes as a plain integer.
 *
 * Example: `123` instead of `{"id":123}`
 */
object NetworkIdSerializer : KSerializer<NetworkId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NetworkId", PrimitiveKind.INT)

    override fun serialize(
        encoder: Encoder,
        value: NetworkId,
    ) {
        encoder.encodeInt(value.id)
    }

    override fun deserialize(decoder: Decoder): NetworkId {
        val id = decoder.decodeInt()
        return NetworkId(id)
    }
}

typealias KNetworkId =
    @Serializable(with = NetworkIdSerializer::class)
    NetworkId
