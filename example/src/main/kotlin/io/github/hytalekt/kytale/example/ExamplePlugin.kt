package io.github.hytalekt.kytale.example

import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import io.github.hytalekt.kytale.ext.atInfoOrNull
import io.github.hytalekt.kytale.ext.component1
import io.github.hytalekt.kytale.ext.component2
import io.github.hytalekt.kytale.ext.component3
import io.github.hytalekt.kytale.ext.plus
import io.github.hytalekt.kytale.ext.register
import io.github.hytalekt.kytale.ext.registerAsyncUnhandled
import io.github.hytalekt.kytale.ext.times
import io.github.hytalekt.kytale.message.text
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
        commandRegistry.registerCommand(WaypointCommand)
        
        eventRegistry.register<PlayerConnectEvent>(4) { event ->
            event.playerRef.sendMessage(text("You are connected to the server!"))
        }

        eventRegistry.registerAsyncUnhandled<_, PlayerChatEvent> {
            it.thenApply { event ->
                event.sender.sendMessage(text("You sent a message!"))
                event
            }
        }

        val pos = Vector3d(1.0, 2.0, 3.0)
        val offset = Vector3d(10.0, 0.0, 10.0)
        val newPos = pos + offset
        val scaled = pos * 2.0
        val (x, y, z) = newPos
    }
    
    override fun start() {
        logger.atInfoOrNull()?.log("Plugin started")
    }
}
