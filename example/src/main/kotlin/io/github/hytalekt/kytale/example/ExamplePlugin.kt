package io.github.hytalekt.kytale.example

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import io.github.hytalekt.kytale.example.command.*

class ExamplePlugin(
    init: JavaPluginInit,
) : JavaPlugin(init) {
    override fun setup() {
        commandRegistry.registerCommand(KitCommand)
        commandRegistry.registerCommand(TeleportCommand)
        commandRegistry.registerCommand(GamemodeCommand)
        commandRegistry.registerCommand(GiveCommand)
        commandRegistry.registerCommand(WorldCommand)
    }
}
