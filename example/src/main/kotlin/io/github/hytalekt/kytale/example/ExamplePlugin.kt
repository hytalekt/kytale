package io.github.hytalekt.kytale.example

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import io.github.hytalekt.kytale.example.commands.registerCommands
import io.github.hytalekt.kytale.example.events.registerEvents
import io.github.hytalekt.kytale.example.features.CustomNpcManager
import io.github.hytalekt.kytale.example.features.MagicItemsManager

/**
 * Example plugin demonstrating Kytale DSL usage.
 *
 * This plugin showcases:
 * - Command registration with fluent DSL (commands/Commands.kt)
 * - Event handling with type-safe listeners (events/Events.kt)
 * - NPC creation and management (features/CustomNpcManager.kt)
 * - Custom item creation (features/MagicItemsManager.kt)
 *
 * Note: Some features are simplified or have TODO markers where
 * deeper Hytale API integration is needed.
 */
class ExamplePlugin(
    init: JavaPluginInit,
) : JavaPlugin(init) {
    private lateinit var npcManager: CustomNpcManager
    private lateinit var itemsManager: MagicItemsManager

    override fun start() {
        getLogger().at(java.util.logging.Level.INFO).log("Enabling Kytale Example Plugin...")

        // Initialize managers
        npcManager = CustomNpcManager(this)
        itemsManager = MagicItemsManager(this)

        // Register commands
        registerCommands()

        // Register event listeners
        registerEvents()

        // Create example items
        val sword = itemsManager.createLegendarySword()
        val staff = itemsManager.createMagicStaff()
        getLogger().at(java.util.logging.Level.INFO).log("Created example items")

        getLogger().at(java.util.logging.Level.INFO).log("Kytale Example Plugin enabled!")
    }

    override fun shutdown() {
        getLogger().at(java.util.logging.Level.INFO).log("Disabling Kytale Example Plugin...")

        // Cleanup
        npcManager.cleanup()

        getLogger().at(java.util.logging.Level.INFO).log("Kytale Example Plugin disabled!")
    }
}
