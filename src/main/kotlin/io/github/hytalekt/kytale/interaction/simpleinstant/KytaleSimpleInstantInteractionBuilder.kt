package io.github.hytalekt.kytale.interaction.simpleinstant

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import io.github.hytalekt.kytale.interaction.KyInteractionExecutorHolder
import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.interaction.interaction.KytaleInteractionBuilder
import io.github.hytalekt.kytale.codec.CodecBuilder

private typealias Interaction = SimpleInstantInteraction

@KytaleInteractionDsl
class KytaleSimpleInstantInteractionBuilder(
    codecBuilderScope: CodecBuilder<Interaction>,
    private var firstRunHolder: KyInteractionExecutorHolder<FirstRunExecutor>,
) : KytaleInteractionBuilder<Interaction>(codecBuilderScope) {
    fun firstRun(block: FirstRunExecutor) {
        firstRunHolder.executor = block
    }
}
