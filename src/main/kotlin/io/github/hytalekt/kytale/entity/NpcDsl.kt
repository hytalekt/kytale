package io.github.hytalekt.kytale.entity

import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter

/**
 * Builder for creating NPCs with fluent configuration.
 *
 * Example usage:
 * ```
 * val npc = npc(world) {
 *     typeId("hytale:village_merchant")
 *     position(x, y, z)
 *     name("Friendly Merchant")
 *     interaction {
 *         onActivate { player ->
 *             // Handle interaction
 *         }
 *     }
 *     ai {
 *         wander(radius = 10.0)
 *         faceNearestPlayer(distance = 5.0)
 *     }
 * }
 * ```
 */
@EntityDsl
class NpcBuilder(
    private val world: World,
) {
    private var npcTypeId: String? = null
    private var npcTypeIndex: Int? = null

    /**
     * Sets the NPC type by ID
     */
    fun typeId(id: String) {
        npcTypeId = id
    }

    /**
     * Sets the NPC type by index
     */
    fun typeIndex(index: Int) {
        npcTypeIndex = index
    }

    /**
     * Sets the position of the NPC
     */
    fun position(
        x: Double,
        y: Double,
        z: Double,
    ) {
        // TODO: Implement
    }

    /**
     * Sets a custom name for the NPC
     */
    fun name(displayName: String) {
        // TODO: Implement nameplate configuration
    }

    /**
     * Configures NPC interactions
     */
    fun interaction(configure: NpcInteractionBuilder.() -> Unit) {
        // TODO: Implement interaction configuration
    }

    /**
     * Configures NPC AI behavior
     */
    fun ai(configure: NpcAiBuilder.() -> Unit) {
        // TODO: Implement AI configuration
    }

    /**
     * Configures NPC equipment
     */
    fun equipment(configure: NpcEquipmentBuilder.() -> Unit) {
        // TODO: Implement equipment configuration
    }

    /**
     * Builds the NPC entity
     */
    fun build(): INonPlayerCharacter {
        TODO("Implement NPC creation with typeId: $npcTypeId")
    }
}

/**
 * Builder for NPC interactions
 */
@EntityDsl
class NpcInteractionBuilder {
    /**
     * Configures what happens when a player activates (right-clicks) the NPC
     */
    fun onActivate(handler: (player: Any) -> Unit) {
        // TODO: Implement activation handler
    }

    /**
     * Configures what happens when a player attacks the NPC
     */
    fun onAttack(handler: (player: Any) -> Unit) {
        // TODO: Implement attack handler
    }

    /**
     * Sets the interaction distance
     */
    fun distance(blocks: Double) {
        // TODO: Implement distance configuration
    }
}

/**
 * Builder for NPC AI configuration
 */
@EntityDsl
class NpcAiBuilder {
    /**
     * Makes the NPC wander within a radius
     */
    fun wander(
        radius: Double,
        speed: Double = 1.0,
    ) {
        // TODO: Implement wander behavior
    }

    /**
     * Makes the NPC face the nearest player within distance
     */
    fun faceNearestPlayer(distance: Double) {
        // TODO: Implement facing behavior
    }

    /**
     * Makes the NPC patrol between points
     */
    fun patrol(vararg points: Triple<Double, Double, Double>) {
        // TODO: Implement patrol behavior
    }

    /**
     * Makes the NPC follow a target
     */
    fun follow(
        targetId: String,
        distance: Double = 3.0,
    ) {
        // TODO: Implement follow behavior
    }

    /**
     * Makes the NPC flee from threats
     */
    fun fleeFromDanger(radius: Double = 10.0) {
        // TODO: Implement flee behavior
    }
}

/**
 * Builder for NPC equipment
 */
@EntityDsl
class NpcEquipmentBuilder {
    /**
     * Sets the main hand item
     */
    fun mainHand(itemId: String) {
        // TODO: Implement equipment
    }

    /**
     * Sets the off hand item
     */
    fun offHand(itemId: String) {
        // TODO: Implement equipment
    }

    /**
     * Sets helmet
     */
    fun helmet(itemId: String) {
        // TODO: Implement armor
    }

    /**
     * Sets chestplate
     */
    fun chestplate(itemId: String) {
        // TODO: Implement armor
    }

    /**
     * Sets leggings
     */
    fun leggings(itemId: String) {
        // TODO: Implement armor
    }

    /**
     * Sets boots
     */
    fun boots(itemId: String) {
        // TODO: Implement armor
    }
}

/**
 * DSL function to create an NPC
 */
fun npc(
    world: World,
    configure: NpcBuilder.() -> Unit,
): INonPlayerCharacter = NpcBuilder(world).apply(configure).build()
