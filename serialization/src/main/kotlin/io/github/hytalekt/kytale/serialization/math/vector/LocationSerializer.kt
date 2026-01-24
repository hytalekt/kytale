@file:OptIn(ExperimentalSerializationApi::class)

package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Location
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Location") {
            element<String?>("world")
            element("position", Vector3dSerializer.descriptor)
            element("rotation", Vector3fSerializer.descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Location,
    ) = encoder.encodeStructure(descriptor) {
        encodeNullableSerializableElement(descriptor, 0, String.serializer(), value.world)
        encodeSerializableElement(descriptor, 1, Vector3dSerializer, value.position)
        encodeSerializableElement(descriptor, 2, Vector3fSerializer, value.rotation)
    }

    override fun deserialize(decoder: Decoder): Location =
        decoder.decodeStructure(descriptor) {
            var world: String? = null
            var position: Vector3d? = null
            var rotation: Vector3f? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> world = decodeNullableSerializableElement(descriptor, 0, String.serializer())
                    1 -> position = decodeSerializableElement(descriptor, 1, Vector3dSerializer)
                    2 -> rotation = decodeSerializableElement(descriptor, 2, Vector3fSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Location(world, position!!, rotation!!)
        }
}

typealias KLocation =
    @Serializable(with = LocationSerializer::class)
    Location
