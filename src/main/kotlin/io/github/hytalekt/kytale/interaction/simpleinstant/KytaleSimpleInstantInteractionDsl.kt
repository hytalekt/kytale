@file:Suppress("NOTHING_TO_INLINE")

package io.github.hytalekt.kytale.interaction.simpleinstant

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.newCodecBuilder

inline fun internalSimpleInstantInteraction(
    interactionId: String, block: @KytaleInteractionDsl KytaleSimpleInstantInteractionBuilder.() -> Unit
): KytaleInteractionBundle<SimpleInstantInteraction> {
    val delegatedInteraction = createDelegatedSimpleInstantInteraction()

    val codecBuilder = newCodecBuilder<SimpleInstantInteraction>(
        parentCodec = SimpleInstantInteraction.CODEC,
        supplier = { delegatedInteraction }
    )

    KytaleSimpleInstantInteractionBuilder(
        codecBuilderScope = CodecBuilder(codecBuilder), delegate = delegatedInteraction
    ).apply(block)

    return KytaleInteractionBundle(
        id = interactionId,
        interactionClass = delegatedInteraction.javaClass,
        codec = codecBuilder.build(),
    )
}

// Create a new anonymous class + anonymous object to delegate all calls.
// Since this function is inline, a new class will be created for each call,
// circumventing the restriction of one class per interactionId.
inline fun createDelegatedSimpleInstantInteraction(): KytaleSimpleInstantInteractionDelegate =
    object : KytaleSimpleInstantInteractionDelegate() {}
