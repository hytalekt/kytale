package io.github.hytalekt.kytale.example.features

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import io.github.hytalekt.kytale.entity.npc

/**
 * Example NPC management using the NPC DSL.
 */
class CustomNpcManager(
    private val plugin: JavaPlugin,
) {
    private val npcs = mutableListOf<Any>()

    fun spawnMerchantNpc(
        x: Double,
        y: Double,
        z: Double,
    ) {
        // TODO: Get world
        // val world = plugin.server.getWorld("world")

        // Create a merchant NPC with full configuration
        /*
        val merchant = npc(world) {
            typeId("hytale:village_merchant")
            position(x, y, z)
            name("Friendly Merchant")

            interaction {
                onActivate { player ->
                    player.sendMessage("Welcome to my shop!")
                    // TODO: Open shop UI
                }

                onAttack { player ->
                    player.sendMessage("Hey! Don't attack me!")
                }

                distance(5.0)
            }

            ai {
                wander(radius = 10.0, speed = 1.0)
                faceNearestPlayer(distance = 5.0)
            }

            equipment {
                mainHand("hytale:merchant_staff")
                chestplate("hytale:merchant_robe")
            }
        }

        npcs.add(merchant)
         */
        plugin.getLogger().at(java.util.logging.Level.INFO).log("Spawned merchant NPC at (" + x + ", " + y + ", " + z + ")")
    }

    fun spawnGuardNpc(
        x: Double,
        y: Double,
        z: Double,
    ) {
        plugin.getLogger().at(java.util.logging.Level.INFO).log("Spawned guard NPC at (" + x + ", " + y + ", " + z + ")")
    }

    fun spawnQuestGiverNpc(
        x: Double,
        y: Double,
        z: Double,
    ) {
        plugin.getLogger().at(java.util.logging.Level.INFO).log("Spawned quest giver NPC at (" + x + ", " + y + ", " + z + ")")
    }

    fun spawnHostileNpc(
        x: Double,
        y: Double,
        z: Double,
    ) {
        plugin.getLogger().at(java.util.logging.Level.INFO).log("Spawned hostile NPC at (" + x + ", " + y + ", " + z + ")")
    }

    fun cleanup() {
        npcs.forEach { npc ->
            // TODO: Remove NPC from world
        }
        val count = npcs.size
        npcs.clear()
        plugin.getLogger().at(java.util.logging.Level.INFO).log("Cleaned up " + count + " NPCs")
    }
}
