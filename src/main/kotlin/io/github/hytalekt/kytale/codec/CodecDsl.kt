package io.github.hytalekt.kytale.codec

import com.hypixel.hytale.codec.builder.BuilderCodec
import java.util.function.Supplier

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class CodecDsl

/**
 * DSL for [BuilderCodec]
 *
 * Example usage:
 *
 *     buildCodec(::ExampleData) {
 *         versioned()
 *
 *         documentation =
 *             """
 *             Example documentation
 *             """.trimIndent()
 *
 *         addField("Index", Codec.INTEGER) {
 *             documentation = "The index"
 *
 *             setter { this.index = it }
 *             getter { this.index }
 *         }
 *
 *         addField("Message", Codec.STRING) {
 *             documentation = "The message"
 *
 *             setter { this.message = it }
 *             getter { this.message }
 *         }
 *     }
 *
 * @param T The type to be encoded/decoded
 * @param supplier A supplier function that creates new instances of type T (e.g., `::ClassName`)
 * @param block A lambda with [CodecBuilder] receiver to configure the codec
 *
 * @return The built [BuilderCodec] for the type T
 *
 * @see CodecBuilder
 * @see FieldBuilder
 */
inline fun <reified T> buildCodec(
    supplier: Supplier<T>,
    parentCodec: BuilderCodec<in T>? = null,
    block: @CodecDsl CodecBuilder<T>.() -> Unit,
): BuilderCodec<T?> = CodecBuilder<T>(newCodecBuilder(supplier, parentCodec)).apply(block).inner.build()

inline fun <reified T> newCodecBuilder(
    supplier: Supplier<T>,
    parentCodec: BuilderCodec<in T>? = null,
): BuilderCodec.Builder<T> =
    parentCodec?.let { parentCodec ->
        BuilderCodec.builder(T::class.java, supplier, parentCodec)
    } ?: BuilderCodec.builder(T::class.java, supplier)
