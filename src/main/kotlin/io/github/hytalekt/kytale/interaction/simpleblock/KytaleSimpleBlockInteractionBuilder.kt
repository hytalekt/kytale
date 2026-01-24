package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.interaction.interaction.KytaleInteractionBuilder
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle

private typealias Interaction = SimpleBlockInteraction

@KytaleInteractionDsl
class KytaleSimpleBlockInteractionBuilder(
    private val interactionId: String,
    codecBuilder: CodecBuilder<Interaction>,
    private val delegate: KytaleSimpleBlockInteractionDelegate
): KytaleInteractionBuilder<Interaction>(codecBuilder) {
    fun interactWithBlock(block: InteractWithBlockExecutor) {
        delegate.interactWithBlockExecutor = block
    }

    fun simulateInteractWithBlock(block: SimulateInteractWithBlockExecutor) {
        delegate.simulateInteractWithBlockExecutor = block
    }

    fun build(): KytaleInteractionBundle<SimpleBlockInteraction> {
        require(delegate.interactWithBlockExecutor != null) { "${interactionId}: interactWithBlock definition is required" }
        // simulateInteractWithBlock is optional, no need to validate

        return KytaleInteractionBundle(
            id = interactionId,
            interactionClass = delegate.javaClass,
            codec = codecBuilder.inner.build()
        )
    }
}
