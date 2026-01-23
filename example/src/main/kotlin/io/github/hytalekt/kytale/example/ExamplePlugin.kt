package io.github.hytalekt.kytale.example

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import io.github.hytalekt.kytale.example.command.*
import io.github.hytalekt.kytale.example.interaction.simpleblock.DumpContainerInteraction
import io.github.hytalekt.kytale.example.interaction.simpleinstant.SpawnEntityInteraction
import io.github.hytalekt.kytale.interaction.extension.interactionRegistry
import io.github.hytalekt.kytale.interaction.extension.register

class ExamplePlugin(
    init: JavaPluginInit,
) : JavaPlugin(init) {
    override fun setup() {
        with(commandRegistry) {
            registerCommand(KitCommand)
            registerCommand(TeleportCommand)
            registerCommand(GamemodeCommand)
            registerCommand(GiveCommand)
            registerCommand(WorldCommand)
        }

        with(interactionRegistry) {
            register(SpawnEntityInteraction)
            register(DumpContainerInteraction)
        }
    }
}
