package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Box
import com.hypixel.hytale.math.vector.Vector3d
import io.github.hytalekt.kytale.serialization.math.vector.Vector3dSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object BoxSerializer : KSerializer<Box> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Box") {
            element("min", Vector3dSerializer.descriptor)
            element("max", Vector3dSerializer.descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Box,
    ) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, Vector3dSerializer, value.min)
        encodeSerializableElement(descriptor, 1, Vector3dSerializer, value.max)
    }

    override fun deserialize(decoder: Decoder): Box =
        decoder.decodeStructure(descriptor) {
            var min: Vector3d? = null
            var max: Vector3d? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> min = decodeSerializableElement(descriptor, 0, Vector3dSerializer)
                    1 -> max = decodeSerializableElement(descriptor, 1, Vector3dSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Box(min!!, max!!)
        }
}

typealias KBox =
    @Serializable(with = BoxSerializer::class)
    Box
