package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Cylinder
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

object CylinderSerializer : KSerializer<Cylinder> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Cylinder") {
            element<Double>("height")
            element<Double>("radiusX")
            element<Double>("radiusZ")
        }

    override fun serialize(
        encoder: Encoder,
        value: Cylinder,
    ) = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.height)
        encodeDoubleElement(descriptor, 1, value.radiusX)
        encodeDoubleElement(descriptor, 2, value.radiusZ)
    }

    override fun deserialize(decoder: Decoder): Cylinder =
        decoder.decodeStructure(descriptor) {
            var height = 0.0
            var radiusX = 0.0
            var radiusZ = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> height = decodeDoubleElement(descriptor, 0)
                    1 -> radiusX = decodeDoubleElement(descriptor, 1)
                    2 -> radiusZ = decodeDoubleElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Cylinder(height, radiusX, radiusZ)
        }
}

typealias KCylinder =
    @Serializable(with = CylinderSerializer::class)
    Cylinder
