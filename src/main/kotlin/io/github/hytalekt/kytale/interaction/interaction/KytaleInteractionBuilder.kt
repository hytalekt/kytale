package io.github.hytalekt.kytale.interaction.interaction

import io.github.hytalekt.kytale.interaction.KytaleInteractionDsl
import io.github.hytalekt.kytale.codec.CodecBuilder

@KytaleInteractionDsl
open class KytaleInteractionBuilder<Interaction>(
    val codecBuilderScope: CodecBuilder<Interaction>,
) {
    inline fun codec(crossinline block: CodecBuilder<Interaction>.() -> Unit) {
        codecBuilderScope.block()
    }
}
