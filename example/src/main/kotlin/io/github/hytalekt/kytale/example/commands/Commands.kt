package io.github.hytalekt.kytale.example.commands

import io.github.hytalekt.kytale.command.*

/**
 * Example command registration demonstrating the command DSL.
 */
fun registerCommands() {
    // Simple command with arguments
    val giveCommand =
        command("give") {
            description("Give yourself an item")
            permission("example.give")

            argument("item") {
                type(ArgumentTypes.ITEM)
                required()
                suggestions("hytale:sword", "hytale:pickaxe", "hytale:stone")
            }

            argument("amount") {
                type(ArgumentTypes.INTEGER)
                optional(1)
            }

            execute {
                val itemId = get<String>("item")
                val amount = getOptional<Int>("amount") ?: 1

                // TODO: Get player and give item
                println("Giving $amount x $itemId")
            }
        }

    // Command with subcommands
    val adminCommand =
        command("admin") {
            description("Admin commands")
            permission("example.admin")

            subcommand("reload") {
                description("Reload the plugin")
                execute {
                    println("Reloading plugin...")
                }
            }

            subcommand("debug") {
                description("Toggle debug mode")
                execute {
                    println("Toggling debug mode...")
                }
            }

            subcommand("spawn") {
                description("Spawn an NPC")

                argument("type") {
                    type(ArgumentTypes.STRING)
                    required()
                    suggestions("merchant", "guard", "quest_giver")
                }

                execute {
                    val npcType = get<String>("type")
                    println("Spawning NPC: $npcType")
                }
            }
        }

    // Teleport command with multiple arguments
    val teleportCommand =
        command("teleport", "tp") {
            description("Teleport to coordinates or player")
            permission("example.teleport")

            argument("x") {
                type(ArgumentTypes.DOUBLE)
                required()
            }

            argument("y") {
                type(ArgumentTypes.DOUBLE)
                required()
            }

            argument("z") {
                type(ArgumentTypes.DOUBLE)
                required()
            }

            execute {
                val x = get<Double>("x")
                val y = get<Double>("y")
                val z = get<Double>("z")

                println("Teleporting to ($x, $y, $z)")
            }

            subcommand("player") {
                description("Teleport to another player")

                argument("target") {
                    type(ArgumentTypes.PLAYER)
                    required()
                }

                execute {
                    println("Teleporting to player")
                }
            }
        }

    // TODO: Register commands with Hytale's command system
    println("Commands registered: give, admin, teleport")
}
