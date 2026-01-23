package io.github.hytalekt.kytale.interaction.extension

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction
import com.hypixel.hytale.server.core.plugin.JavaPlugin

val JavaPlugin.interactionRegistry get() = this.getCodecRegistry(Interaction.CODEC)
