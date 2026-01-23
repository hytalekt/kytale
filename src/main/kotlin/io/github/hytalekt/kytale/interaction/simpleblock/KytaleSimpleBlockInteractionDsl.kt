package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KyInteractionExecutorHolder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.codec.newCodecBuilder

inline fun internalSimpleBlockInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleBlockInteractionBuilder.() -> Unit
): KytaleInteractionBundle<SimpleBlockInteraction> {
    val interactExecutorHolder = KyInteractionExecutorHolder<InteractWithBlockExecutor>()
    val simulateExecutorHolder = KyInteractionExecutorHolder<SimulateInteractWithBlockExecutor>()

    val simpleBlockInteraction = createDelegatedSimpleBlockInteraction(
        interactionId = interactionId,
        interactWithBlockHolder = interactExecutorHolder,
        simulateInteractWithBlockHolder = simulateExecutorHolder
    )

    val codecBuilder = newCodecBuilder(
        parentCodec = SimpleBlockInteraction.CODEC,
        supplier = { simpleBlockInteraction })

    KytaleSimpleBlockInteractionBuilder(
        interactWithBlockHolder = interactExecutorHolder,
        simulateInteractWithBlockHolder = simulateExecutorHolder,
        codecBuilderScope = CodecBuilder(codecBuilder)
    ).apply { block() }

    return KytaleInteractionBundle(
        id = interactionId,
        interactionClass = simpleBlockInteraction.javaClass,
        codec = codecBuilder.build(),
    )
}
