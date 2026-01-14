package io.github.hytalekt.kytale.example.features

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import io.github.hytalekt.kytale.item.itemStack

/**
 * Example custom item creation using the item DSL.
 */
class MagicItemsManager(
    private val plugin: JavaPlugin,
) {
    /**
     * Creates a legendary sword with custom properties
     */
    fun createLegendarySword() =
        itemStack("hytale:iron_sword") {
            amount(1)
            durability(1000)
            displayName("§6§lLegendary Blade")

            lore {
                +"§7A blade forged in dragon fire"
                +"§7Grants its wielder immense power"
                +""
                +"§c+15 Attack Damage"
                +"§9+10% Critical Strike Chance"
                +"§a+5 Health on Kill"
            }

            metadata {
                "damage_bonus" to 15
                "crit_chance" to 0.10
                "health_on_kill" to 5
                "is_legendary" to true
                "enchantments" to listOf("sharpness:5", "fire_aspect:2", "unbreaking:3")
            }
        }

    /**
     * Creates a magic staff
     */
    fun createMagicStaff() =
        itemStack("hytale:wooden_staff") {
            displayName("§5§lStaff of Arcane Power")

            lore {
                +"§7Channel magical energies"
                +"§7Right-click to cast spells"
                +""
                +"§d+50 Mana"
                +"§b-20% Spell Cooldown"
            }

            metadata {
                "item_type" to "magic_staff"
                "mana_bonus" to 50
                "cooldown_reduction" to 0.20
                "spell_power" to 1.5
                "spells" to listOf("fireball", "ice_blast", "lightning")
            }
        }

    /**
     * Creates a healing potion stack
     */
    fun createHealingPotions() =
        itemStack("hytale:potion") {
            amount(16)
            displayName("§c§lGreater Healing Potion")

            lore {
                +"§7Restores §c50 HP §7instantly"
                +"§730 second cooldown"
            }

            metadata {
                "potion_type" to "healing"
                "heal_amount" to 50
                "cooldown" to 30000
            }
        }

    init {
        plugin.getLogger().at(java.util.logging.Level.INFO).log("MagicItemsManager initialized")
    }
}
