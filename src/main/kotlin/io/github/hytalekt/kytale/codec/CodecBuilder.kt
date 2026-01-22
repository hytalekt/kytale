@file:Suppress("NOTHING_TO_INLINE")

package io.github.hytalekt.kytale.codec

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.builder.BuilderField
import com.hypixel.hytale.codec.schema.metadata.Metadata

@CodecDsl
@JvmInline
value class CodecBuilder<T>(
    val inner: BuilderCodec.Builder<T>,
) {
    /**
     * Sets the documentation for this codec
     *
     * This is a write-only property. The getter throws [UnsupportedOperationException]
     * because the underlying codec builder API does not support reading documentation back.
     *
     * @throws UnsupportedOperationException when attempting to read the documentation value
     */
    var documentation: String
        get() = throw UnsupportedOperationException("Documentation is write-only and cannot be read")
        set(value) {
            inner.documentation(value)
        }

    /**
     * Add a field to the codec with the given keyed codec
     *
     * @param F The type of the field
     * @param codec The [KeyedCodec] that defines the field's key and codec
     * @param block A lambda to configure the field using [FieldBuilder]
     *
     * @return The built field builder
     */
    inline fun <F> addField(
        codec: KeyedCodec<F>,
        block: FieldBuilder<T, F>.() -> Unit,
    ): BuilderField.FieldBuilder<T, F, BuilderCodec.Builder<T>> = FieldBuilder<T, F>().apply(block).build(inner, codec)

    /**
     * Add a field to the codec with a string key and codec
     *
     * @param F The type of the field
     * @param field The field name/key
     * @param codec The [Codec] for encoding/decoding the field
     * @param block A lambda to configure the field using [FieldBuilder]
     *
     * @return The built field builder
     */
    inline fun <F> addField(
        field: String,
        codec: Codec<F>,
        block: FieldBuilder<T, F>.() -> Unit,
    ): BuilderField.FieldBuilder<T, F, BuilderCodec.Builder<T>> = addField(KeyedCodec(field, codec), block)

    /**
     * Defines an `afterDecode` callback that is invoked after the codec finishes decoding
     *
     * @param receiver A lambda that will be invoked with the decoded object
     *
     * @see com.hypixel.hytale.codec.builder.BuilderCodec.Builder.afterDecode
     */
    fun afterDecode(receiver: T.() -> Unit) {
        inner.afterDecode(receiver)
    }

    /**
     * Adds a piece of metadata to the codec
     *
     * @param meta The [Metadata] to add
     *
     * @see com.hypixel.hytale.codec.schema.metadata.Metadata
     */
    fun addMetadata(meta: Metadata) {
        inner.metadata(meta)
    }

    /**
     * Sets the current codec version and minimal required codec version
     *
     * @param version The current version of the codec
     * @param minVersion The minimum version that this codec can decode. Defaults to 0
     */
    fun codecVersion(
        version: Int,
        minVersion: Int = 0,
    ) {
        inner.codecVersion(minVersion, version)
    }

    /**
     * Enables the `versioned` flag on the codec to indicate it supports versioning
     *
     * @see codecVersion
     */
    fun versioned() {
        inner.versioned()
    }
}
