package io.github.hytalekt.kytale.serialization.math.vector

import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object TransformSerializer : KSerializer<Transform> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Transform") {
            element("position", Vector3dSerializer.descriptor)
            element("rotation", Vector3fSerializer.descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Transform,
    ) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, Vector3dSerializer, value.position)
        encodeSerializableElement(descriptor, 1, Vector3fSerializer, value.rotation)
    }

    override fun deserialize(decoder: Decoder): Transform =
        decoder.decodeStructure(descriptor) {
            var position: Vector3d? = null
            var rotation: Vector3f? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> position = decodeSerializableElement(descriptor, 0, Vector3dSerializer)
                    1 -> rotation = decodeSerializableElement(descriptor, 1, Vector3fSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Transform(position!!, rotation!!)
        }
}

/**
 * Serializable typealias for [Transform].
 *
 * Serialized shape: `{ position: Vector3d, rotation: Vector3f }`
 */
typealias KTransform =
    @Serializable(with = TransformSerializer::class)
    Transform
