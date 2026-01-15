package io.github.hytalekt.kytale.example.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.message.text

val TeleportCommand =
    command("teleport", "Teleport commands") {
        alias("tp", "tele")
        requirePermission("example.teleport")

        subcommand("here", "Teleport a player to you") {
            val playerArg = requiredArg("player", "Player to teleport", ArgTypes.PLAYER_REF)

            executorSync { context ->
                val player = playerArg()
                context.sendMessage(text("Teleporting ${player.username} to you"))
            }
        }

        subcommand("to", "Teleport to a location or player") {
            variant("Teleport to player") {
                val targetArg = requiredArg("target", "Player to teleport to", ArgTypes.PLAYER_REF)

                executorSync { context ->
                    val target = targetArg()
                    context.sendMessage(text("Teleporting you to ${target.username}"))
                }
            }

            variant("Teleport to coordinates") {
                val xArg = requiredArg("x", "X coordinate", ArgTypes.DOUBLE)
                val yArg = requiredArg("y", "Y coordinate", ArgTypes.DOUBLE)
                val zArg = requiredArg("z", "Z coordinate", ArgTypes.DOUBLE)

                executorSync { context ->
                    val x = xArg()
                    val y = yArg()
                    val z = zArg()
                    context.sendMessage(text("Teleporting you to ($x, $y, $z)"))
                }
            }
        }

        subcommand("spawn", "Teleport to spawn") {
            executorSync { context ->
                context.sendMessage(text("Teleporting you to spawn"))
            }
        }
    }
