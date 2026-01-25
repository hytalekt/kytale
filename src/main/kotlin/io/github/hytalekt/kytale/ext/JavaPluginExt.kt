package io.github.hytalekt.kytale.ext

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.registry.CodecMapRegistry

/**
 * Returns the [Interaction] codec registry for this plugin
 *
 * @see com.hypixel.hytale.server.core.plugin.JavaPlugin.getCodecRegistry
 */
val JavaPlugin.interactionRegistry: CodecMapRegistry.Assets<Interaction, *> get() = this.getCodecRegistry(Interaction.CODEC)
