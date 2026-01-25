package io.github.hytalekt.kytale.example.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.message.text

val WorldCommand =
    command("world", "World management commands") {
        requirePermission("example.world")

        subcommand("tp", "Teleport to a world") {
            val worldArg = requiredArg("world", "World to teleport to", ArgTypes.WORLD)

            executorSync { context ->
                val world = worldArg()
                context.sendMessage(text("Teleporting you to world: ${world.name}"))
            }
        }

        subcommand("info", "Get information about a world") {
            val worldArg = optionalArg("world", "World to get info about", ArgTypes.WORLD)

            executorSync { context ->
                val world = worldArg()
                val worldName = world?.name ?: "current world"
                context.sendMessage(text("Information about $worldName"))
            }
        }
    }
