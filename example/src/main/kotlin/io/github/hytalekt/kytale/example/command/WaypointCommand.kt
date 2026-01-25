package io.github.hytalekt.kytale.example.command

import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.example.waypoint.loadWaypoints
import io.github.hytalekt.kytale.message.text

val WaypointCommand =
    command("waypoint", "Waypoint registry commands") {
        alias("wp")
        requirePermission("example.waypoint")

        val registry by lazy { loadWaypoints() }

        subcommand("list", "List all registered waypoints") {
            executorSync { context ->
                context.sendMessage(text("Waypoints (v${registry.version.major}.${registry.version.minor}.${registry.version.patch}):"))
                registry.waypoints.forEach { waypoint ->
                    val pos = waypoint.position
                    context.sendMessage(text("  ${waypoint.name}: (${pos.x}, ${pos.y}, ${pos.z})"))
                }
            }
        }

        subcommand("info", "Show detailed waypoint info") {
            val nameArg =
                requiredArg("name", "Waypoint name", com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes.STRING)

            executorSync { context ->
                val name = nameArg()
                val waypoint = registry.waypoints.find { it.name.equals(name, ignoreCase = true) }
                if (waypoint == null) {
                    context.sendMessage(text("Waypoint '$name' not found"))
                    return@executorSync
                }
                val pos = waypoint.position
                val rot = waypoint.rotation
                context.sendMessage(text("Waypoint: ${waypoint.name}"))
                context.sendMessage(text("  Position: (${pos.x}, ${pos.y}, ${pos.z})"))
                context.sendMessage(text("  Rotation: (${rot.x}, ${rot.y}, ${rot.z})"))
            }
        }
    }
