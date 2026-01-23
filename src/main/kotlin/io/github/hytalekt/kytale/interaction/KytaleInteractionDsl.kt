package io.github.hytalekt.kytale.interaction

import io.github.hytalekt.kytale.interaction.simpleblock.KytaleSimpleBlockInteractionBuilder
import io.github.hytalekt.kytale.interaction.simpleblock.internalSimpleBlockInteraction
import io.github.hytalekt.kytale.interaction.simpleinstant.KytaleSimpleInstantInteractionBuilder
import io.github.hytalekt.kytale.interaction.simpleinstant.internalSimpleInstantInteraction

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD
)
annotation class KytaleInteractionDsl

inline fun simpleInstantInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleInstantInteractionBuilder.() -> Unit
) = internalSimpleInstantInteraction(
    interactionId, block,
)

inline fun simpleBlockInteraction(
    interactionId: String,
    block: @KytaleInteractionDsl KytaleSimpleBlockInteractionBuilder.() -> Unit

) = internalSimpleBlockInteraction(
    interactionId, block,
)
