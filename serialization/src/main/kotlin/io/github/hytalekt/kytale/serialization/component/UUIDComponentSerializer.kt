package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.entity.UUIDComponent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

/**
 * Serializer for UUIDComponent that encodes as a plain UUID string.
 *
 * Example: `"550e8400-e29b-41d4-a716-446655440000"` instead of `{"uuid":"..."}`
 */
object UUIDComponentSerializer : KSerializer<UUIDComponent> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUIDComponent", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: UUIDComponent,
    ) {
        encoder.encodeString(value.uuid.toString())
    }

    override fun deserialize(decoder: Decoder): UUIDComponent {
        val uuidString = decoder.decodeString()
        return UUIDComponent(UUID.fromString(uuidString))
    }
}

/**
 * Serializable typealias for [UUIDComponent].
 *
 * Serialized shape: `string` (UUID format)
 */
typealias KUUIDComponent =
    @Serializable(with = UUIDComponentSerializer::class)
    UUIDComponent
