package io.github.hytalekt.kytale.serialization.component

import com.hypixel.hytale.server.core.meta.MetaKey
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

/**
 * Serializer for MetaKey<T>.
 * Note: Only serializes the id field. Type parameter T is erased at runtime.
 */
class MetaKeySerializer<T> : KSerializer<MetaKey<T>> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("MetaKey") {
            element<Int>("id")
        }

    override fun serialize(
        encoder: Encoder,
        value: MetaKey<T>,
    ) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.id)
    }

    override fun deserialize(decoder: Decoder): MetaKey<T> =
        decoder.decodeStructure(descriptor) {
            var id = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeIntElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            @Suppress("UNCHECKED_CAST")
            MetaKey::class.java
                .getDeclaredConstructor(Int::class.javaPrimitiveType)
                .apply { isAccessible = true }
                .newInstance(id) as MetaKey<T>
        }
}

typealias KMetaKey<T> =
    @Serializable(with = MetaKeySerializer::class)
    MetaKey<T>
