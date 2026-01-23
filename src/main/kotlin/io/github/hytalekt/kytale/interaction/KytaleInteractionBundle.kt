package io.github.hytalekt.kytale.interaction

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction

data class KytaleInteractionBundle<T : Interaction>(
    val id: String,
    val interactionClass: Class<out T>,
    val codec: BuilderCodec<out T>
)
