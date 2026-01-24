package io.github.hytalekt.kytale.interaction.interaction

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle

@KytaleInteractionDsl
abstract class KytaleInteractionBuilder<T : Interaction>(
    val codecBuilder: CodecBuilder<T>,
) {
    inline fun codec(crossinline block: CodecBuilder<T>.() -> Unit) {
        codecBuilder.apply(block)
    }

    abstract fun validate();

    fun build(
        interactionId: String,
        interactionClass: Class<out T>,
    ): KytaleInteractionBundle<T> {
        validate()
        return KytaleInteractionBundle(
            id = interactionId,
            interactionClass = interactionClass,
            codec = codecBuilder.inner.build()
        )
    }
}
