@file:Suppress("NOTHING_TO_INLINE")

package io.github.hytalekt.kytale.interaction.simpleinstant

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.interaction.KyInteractionExecutorHolder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.newCodecBuilder

inline fun internalSimpleInstantInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleInstantInteractionBuilder.() -> Unit
): KytaleInteractionBundle<SimpleInstantInteraction> {
    val firstRunHolder = KyInteractionExecutorHolder<FirstRunExecutor>()
    val simpleInstantInteraction = createDelegatedSimpleInstantInteraction(
        interactionId = interactionId,
        firstRunHolder = firstRunHolder,
    )

    val codecBuilder = newCodecBuilder(
        parentCodec = SimpleInstantInteraction.CODEC,
        supplier = { simpleInstantInteraction })

    KytaleSimpleInstantInteractionBuilder(
        codecBuilderScope = CodecBuilder(codecBuilder),
        firstRunHolder = firstRunHolder,
    ).apply(block)

    return KytaleInteractionBundle(
        id = interactionId,
        interactionClass = simpleInstantInteraction.javaClass,
        codec = codecBuilder.build(),
    )
}
