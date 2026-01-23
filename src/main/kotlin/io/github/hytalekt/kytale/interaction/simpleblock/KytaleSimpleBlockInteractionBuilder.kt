package io.github.hytalekt.kytale.interaction.simpleblock

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.KyInteractionExecutorHolder
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.interaction.interaction.KytaleInteractionBuilder
import io.github.hytalekt.kytale.codec.CodecBuilder

private typealias Interaction = SimpleBlockInteraction

@KytaleInteractionDsl
class KytaleSimpleBlockInteractionBuilder(
    codecBuilderScope: CodecBuilder<Interaction>,
    private var interactWithBlockHolder: KyInteractionExecutorHolder<InteractWithBlockExecutor>,
    private var simulateInteractWithBlockHolder: KyInteractionExecutorHolder<SimulateInteractWithBlockExecutor>,
): KytaleInteractionBuilder<Interaction>(codecBuilderScope) {
    fun interactWithBlock(block: InteractWithBlockExecutor) {
        interactWithBlockHolder.executor = block
    }

    fun simulateInteractWithBlock(block: SimulateInteractWithBlockExecutor) {
        simulateInteractWithBlockHolder.executor = block
    }
}
