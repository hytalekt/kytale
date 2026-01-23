package io.github.hytalekt.kytale.interaction.extension

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction
import com.hypixel.hytale.server.core.plugin.registry.CodecMapRegistry
import io.github.hytalekt.kytale.interaction.KytaleInteractionBundle

fun <T : Interaction> CodecMapRegistry.Assets<Interaction, *>.register(bundle: KytaleInteractionBundle<T>) = run {
    this.register(bundle.id, bundle.interactionClass, bundle.codec)
}
