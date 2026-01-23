package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.codec.newCodecBuilder

inline fun internalSimpleBlockInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleBlockInteractionBuilder.() -> Unit
): KytaleInteractionBundle<SimpleBlockInteraction> {
    val delegatedInteraction = createDelegatedSimpleBlockInteraction()

    val codecBuilder = newCodecBuilder(
        parentCodec = SimpleBlockInteraction.CODEC,
        supplier = { delegatedInteraction as SimpleBlockInteraction }
    )

    KytaleSimpleBlockInteractionBuilder(
        codecBuilderScope = CodecBuilder(codecBuilder),
        delegate = delegatedInteraction
    ).apply { block() }

    return KytaleInteractionBundle(
        id = interactionId,
        interactionClass = delegatedInteraction.javaClass,
        codec = codecBuilder.build(),
    )
}
