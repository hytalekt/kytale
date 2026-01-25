package io.github.hytalekt.kytale.serialization.math.shape

import com.hypixel.hytale.math.shape.Ellipsoid
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

object EllipsoidSerializer : KSerializer<Ellipsoid> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Ellipsoid") {
            element<Double>("radiusX")
            element<Double>("radiusY")
            element<Double>("radiusZ")
        }

    override fun serialize(
        encoder: Encoder,
        value: Ellipsoid,
    ) = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.radiusX)
        encodeDoubleElement(descriptor, 1, value.radiusY)
        encodeDoubleElement(descriptor, 2, value.radiusZ)
    }

    override fun deserialize(decoder: Decoder): Ellipsoid =
        decoder.decodeStructure(descriptor) {
            var radiusX = 0.0
            var radiusY = 0.0
            var radiusZ = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> radiusX = decodeDoubleElement(descriptor, 0)
                    1 -> radiusY = decodeDoubleElement(descriptor, 1)
                    2 -> radiusZ = decodeDoubleElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Ellipsoid(radiusX, radiusY, radiusZ)
        }
}

/**
 * Serializable typealias for [Ellipsoid].
 *
 * Serialized shape: `{ radiusX, radiusY, radiusZ: double }`
 */
typealias KEllipsoid =
    @Serializable(with = EllipsoidSerializer::class)
    Ellipsoid
