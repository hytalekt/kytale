package io.github.hytalekt.kytale.codec

import com.hypixel.hytale.codec.ExtraInfo
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.builder.BuilderField
import com.hypixel.hytale.codec.schema.metadata.Metadata
import com.hypixel.hytale.codec.validation.LateValidator
import com.hypixel.hytale.codec.validation.Validator
import java.util.function.Supplier
import kotlin.collections.addAll

private typealias Setter<T, F> = T.(F, ExtraInfo) -> Unit

private typealias Getter<T, F> = T.(ExtraInfo) -> F

private typealias Inherit<T> = T.(T, ExtraInfo) -> Unit

/**
 * A DSL builder for configuring individual codec fields
 *
 * Provides methods to set getters, setters, validators, metadata, and documentation for a field.
 * Typically used within the [CodecBuilder.addField] lambda block.
 *
 * @param T The type being encoded/decoded
 * @param F The type of the field
 * @see CodecBuilder.addField
 */
@CodecDsl
data class FieldBuilder<T, F>(
    /** The setter function for this field. Assigns the decoded value to the object. */
    var setter: Setter<T, F>? = null,
    /** The getter function for this field. Retrieves the value from the object for encoding. */
    var getter: Getter<T, F>? = null,
    /** An optional inherit function for this field. */
    var inherit: Inherit<T>? = null,
    /** Documentation for this field. */
    var documentation: String = "",
    /** List of validators to apply to this field. */
    val validators: MutableList<Validator<F>> = mutableListOf(),
    /** List of late validators to apply to this field. */
    val lateValidators: MutableList<Supplier<LateValidator<in F>>> = mutableListOf(),
    /** List of metadata associated with this field. */
    val metadata: MutableList<Metadata> = mutableListOf(),
) {
    /**
     * Sets the setter function without ExtraInfo
     *
     * @param setter A lambda that receives the field value and assigns it to the object
     */
    inline fun setter(crossinline setter: T.(F) -> Unit) {
        this.setter { value, _ -> setter(value) }
    }

    /**
     * Sets the setter function with ExtraInfo
     *
     * @param setter A [Setter] lambda that can access ExtraInfo
     */
    fun setter(setter: Setter<T, F>) {
        this.setter = setter
    }

    /**
     * Sets the getter function
     *
     * @param getter A [Getter] lambda that retrieves the field value from the object
     */
    fun getter(getter: Getter<T, F>) {
        this.getter = getter
    }

    /**
     * Sets the inherit function
     *
     * @param inherit An [Inherit] lambda for inheritance behavior
     */
    fun inherit(inherit: Inherit<T>) {
        this.inherit = inherit
    }

    /**
     * Add validators for this field
     *
     * @param validator A [Validator] instance
     *
     * @see com.hypixel.hytale.codec.validation.Validator
     */
    fun addValidator(validator: Validator<F>) {
        this.validators.add(validator)
    }

    /**
     * Add late validators for this field
     *
     * Late validators are evaluated after all fields have been decoded.
     *
     * @param validator A supplier that provides a [LateValidator] instance
     *
     * @see com.hypixel.hytale.codec.validation.LateValidator
     */
    fun addLateValidator(validator: @CodecDsl Supplier<LateValidator<in F>>) {
        lateValidators.add(validator)
    }

    /**
     * Add metadata for this field
     *
     * @param metadata A [Metadata] instance to add to this field
     *
     * @see com.hypixel.hytale.codec.schema.metadata.Metadata
     */
    fun addMetadata(metadata: Metadata) {
        this.metadata.add(metadata)
    }

    /**
     * Builds the field with the given parent builder and keyed codec
     *
     * @param B The type of the parent builder
     * @param parent The parent codec builder
     * @param codec The keyed codec for this field
     *
     * @return The built [BuilderField.FieldBuilder]
     */
    fun <B : BuilderCodec.BuilderBase<T, B>> build(
        parent: B,
        codec: KeyedCodec<F>,
    ): BuilderField.FieldBuilder<T, F, B> =
        BuilderField
            .FieldBuilder(
                parent,
                codec,
                setter,
                getter,
                inherit,
            ).also {
                validators.forEach(it::addValidator)
                lateValidators.forEach(it::addValidatorLate)
                metadata.forEach(it::metadata)
                it.documentation(documentation)
            }
}
