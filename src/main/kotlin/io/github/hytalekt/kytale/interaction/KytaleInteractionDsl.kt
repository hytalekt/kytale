package io.github.hytalekt.kytale.interaction

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.codec.newCodecBuilder
import io.github.hytalekt.kytale.interaction.simpleblock.KytaleSimpleBlockInteractionBuilder
import io.github.hytalekt.kytale.interaction.simpleblock.KytaleSimpleBlockInteractionDelegate
import io.github.hytalekt.kytale.interaction.simpleinstant.KytaleSimpleInstantInteractionBuilder
import io.github.hytalekt.kytale.interaction.simpleinstant.KytaleSimpleInstantInteractionDelegate

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD
)
annotation class KytaleInteractionDsl

inline fun simpleInstantInteraction(
    interactionId: String, block: @KytaleInteractionDsl KytaleSimpleInstantInteractionBuilder.() -> Unit
) = with(object : KytaleSimpleInstantInteractionDelegate() {}) {
    KytaleSimpleInstantInteractionBuilder(
        interactionId,
        codecBuilder = CodecBuilder(
            newCodecBuilder<SimpleInstantInteraction>(
                parentCodec = SimpleInstantInteraction.CODEC,
                supplier = { this }
            )),
        delegate = this
    ).apply(block).build()
}

inline fun simpleBlockInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleBlockInteractionBuilder.() -> Unit
) = with(object : KytaleSimpleBlockInteractionDelegate() {}) {
    KytaleSimpleBlockInteractionBuilder(
        interactionId = interactionId,
        codecBuilder = CodecBuilder(newCodecBuilder<SimpleBlockInteraction>(
            parentCodec = SimpleBlockInteraction.CODEC,
            supplier = { this }
        )),
        delegate = this
    ).apply(block).build()
}
