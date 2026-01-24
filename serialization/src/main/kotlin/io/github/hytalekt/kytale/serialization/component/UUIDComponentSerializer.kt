package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.entity.UUIDComponent
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
import java.util.UUID

object UUIDComponentSerializer : KSerializer<UUIDComponent> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("UUIDComponent") {
            element<String>("uuid")
        }

    override fun serialize(
        encoder: Encoder,
        value: UUIDComponent,
    ) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.uuid.toString())
    }

    override fun deserialize(decoder: Decoder): UUIDComponent =
        decoder.decodeStructure(descriptor) {
            var uuidString: String? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> uuidString = decodeStringElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            UUIDComponent(UUID.fromString(uuidString!!))
        }
}

typealias KUUIDComponent =
    @Serializable(with = UUIDComponentSerializer::class)
    UUIDComponent
