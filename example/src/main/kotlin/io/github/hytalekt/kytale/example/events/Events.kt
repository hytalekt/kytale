package io.github.hytalekt.kytale.example.events

import io.github.hytalekt.kytale.event.*

/**
 * Example event registration demonstrating the event DSL.
 */
fun registerEvents() {
    events {
        // Simple event listener
        onPlayerJoin { event ->
            println("Player joined! Sending welcome message...")
        }

        onPlayerQuit { event ->
            println("Player left the server")
        }

        // Event with priority and cancellation
        listen<Any> {
            // TODO: Replace with actual BlockBreakEvent
            priority = EventPriority.HIGH
            ignoreCancelled = true

            filter { event ->
                // Only handle diamond ore
                true
            }

            handle { event ->
                println("Player broke diamond ore!")
            }
        }

        // Cancellable event
        listen<Any> {
            // TODO: Replace with actual BlockPlaceEvent
            priority = EventPriority.NORMAL

            handle { event ->
                // Check if player can build here
                val canBuild = true

                if (!canBuild) {
                    println("Player can't build here!")
                }
            }
        }

        // Entity damage event
        onEntityDamage { event ->
            println("Entity took damage")
        }

        // Chat event with filtering
        onPlayerChat { event ->
            println("Player sent a chat message")
        }

        // Inventory click event
        onInventoryClick { event ->
            println("Player clicked in inventory")
        }

        // Player move event with distance threshold
        onPlayerMove(minimumDistance = 1.0) { event ->
            println("Player moved significantly")
        }

        // Multiple listeners for the same event type
        onPlayerJoin { event ->
            println("First join handler")
        }

        onPlayerJoin { event ->
            println("Second join handler")
        }

        // Monitor priority (runs last, can't modify)
        listen<Any> {
            // TODO: Replace with actual PlayerJoinEvent
            priority = EventPriority.MONITOR
            ignoreCancelled = true

            handle { event ->
                println("Analytics: Player joined")
            }
        }
    }

    println("Event listeners registered")
}
