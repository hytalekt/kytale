@file:Suppress("NOTHING_TO_INLINE")

package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.codec.newCodecBuilder

inline fun internalSimpleBlockInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleBlockInteractionBuilder.() -> Unit
) = with(createDelegatedSimpleBlockInteraction()) {
    KytaleSimpleBlockInteractionBuilder(
        interactionId = interactionId,
        codecBuilder = CodecBuilder(newCodecBuilder<SimpleBlockInteraction>(
            parentCodec = SimpleBlockInteraction.CODEC,
            supplier = { this }
        )),
        delegate = this
    ).apply(block).build()
}

// Create a new anonymous class + anonymous object to delegate the firstRun call.
// Since this function is inline, a new class will be created for each call, circumventing the
// restriction of one class per interactionId.
inline fun createDelegatedSimpleBlockInteraction(): KytaleSimpleBlockInteractionDelegate =
    object : KytaleSimpleBlockInteractionDelegate() {}
