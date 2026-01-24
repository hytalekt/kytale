package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.modules.entity.component.WorldGenId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for WorldGenId that encodes as a plain integer.
 *
 * Example: `456` instead of `{"worldGenId":456}`
 */
object WorldGenIdSerializer : KSerializer<WorldGenId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("WorldGenId", PrimitiveKind.INT)

    override fun serialize(
        encoder: Encoder,
        value: WorldGenId,
    ) {
        encoder.encodeInt(value.worldGenId)
    }

    override fun deserialize(decoder: Decoder): WorldGenId {
        val worldGenId = decoder.decodeInt()
        return WorldGenId(worldGenId)
    }
}

typealias KWorldGenId =
    @Serializable(with = WorldGenIdSerializer::class)
    WorldGenId
