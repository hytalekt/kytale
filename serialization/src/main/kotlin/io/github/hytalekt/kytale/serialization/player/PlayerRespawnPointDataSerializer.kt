package io.github.hytalekt.kytale.serialization.player

import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerRespawnPointData
import io.github.hytalekt.kytale.serialization.math.vector.Vector3dSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector3iSerializer
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

object PlayerRespawnPointDataSerializer : KSerializer<PlayerRespawnPointData> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PlayerRespawnPointData") {
            element("blockPosition", Vector3iSerializer.descriptor)
            element("respawnPosition", Vector3dSerializer.descriptor)
            element<String>("name")
        }

    override fun serialize(
        encoder: Encoder,
        value: PlayerRespawnPointData,
    ) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, Vector3iSerializer, value.blockPosition)
        encodeSerializableElement(descriptor, 1, Vector3dSerializer, value.respawnPosition)
        encodeStringElement(descriptor, 2, value.name)
    }

    override fun deserialize(decoder: Decoder): PlayerRespawnPointData =
        decoder.decodeStructure(descriptor) {
            var blockPosition: Vector3i? = null
            var respawnPosition: Vector3d? = null
            var name: String? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> blockPosition = decodeSerializableElement(descriptor, 0, Vector3iSerializer)
                    1 -> respawnPosition = decodeSerializableElement(descriptor, 1, Vector3dSerializer)
                    2 -> name = decodeStringElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            PlayerRespawnPointData(blockPosition!!, respawnPosition!!, name!!)
        }
}

typealias KPlayerRespawnPointData =
    @Serializable(with = PlayerRespawnPointDataSerializer::class)
    PlayerRespawnPointData
