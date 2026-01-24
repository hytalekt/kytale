package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.interaction.interaction.KytaleInteractionBuilder
import io.github.hytalekt.kytale.codec.CodecBuilder

private typealias Interaction = SimpleBlockInteraction

@KytaleInteractionDsl
class KytaleSimpleBlockInteractionBuilder(
    codecBuilderScope: CodecBuilder<Interaction>,
    private val delegate: KytaleSimpleBlockInteractionDelegate
): KytaleInteractionBuilder<Interaction>(codecBuilderScope) {
    fun interactWithBlock(block: InteractWithBlockExecutor) {
        delegate.interactWithBlockExecutor = block
    }

    fun simulateInteractWithBlock(block: SimulateInteractWithBlockExecutor) {
        delegate.simulateInteractWithBlockExecutor = block
    }

    override fun validate() {
        require(delegate.interactWithBlockExecutor != null) { "interactWithBlock executor is required" }
        // simulateInteractWithBlock is optional, no need to validate
    }
}
