package io.github.hytalekt.kytale.interaction.simpleinstant

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.interaction.interaction.KytaleInteractionBuilder
import io.github.hytalekt.kytale.codec.CodecBuilder
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle

private typealias Interaction = SimpleInstantInteraction

@KytaleInteractionDsl
class KytaleSimpleInstantInteractionBuilder(
    private val interactionId: String,
    codecBuilder: CodecBuilder<Interaction>,
    private val delegate: KytaleSimpleInstantInteractionDelegate
) : KytaleInteractionBuilder<Interaction>(codecBuilder) {
    fun firstRun(block: FirstRunExecutor) {
        delegate.firstRunExecutor = block
    }

    fun simulateFirstRun(block: SimulateFirstRunExecutor) {
        delegate.simulateFirstRunExecutor = block
    }

    fun build(): KytaleInteractionBundle<SimpleInstantInteraction> {
        require(delegate.firstRunExecutor != null) { "firstRun executor is required" }
        // simulateFirstRun is optional, no need to validate

        return KytaleInteractionBundle(
            id = interactionId,
            interactionClass = delegate.javaClass,
            codec = codecBuilder.inner.build()
        )
    }
}
