package io.github.hytalekt.kytale.example.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.message.text

val GamemodeCommand =
    command("gamemode", "Change gamemode") {
        alias("gm")
        requirePermission("example.gamemode")

        variant("Change your own gamemode") {
            val modeArg = requiredArg("mode", "Gamemode to change to", ArgTypes.GAME_MODE)

            executorSync { context ->
                val mode = modeArg()
                context.sendMessage(text("Changed your gamemode to ${mode.name}"))
            }
        }

        variant("Change another player's gamemode") {
            val modeArg = requiredArg("mode", "Gamemode to change to", ArgTypes.GAME_MODE)
            val playerArg = requiredArg("player", "Player to change gamemode", ArgTypes.PLAYER_REF)

            executorSync { context ->
                val mode = modeArg()
                val player = playerArg()
                context.sendMessage(text("Changed ${player.username}'s gamemode to ${mode.name}"))
            }
        }
    }
