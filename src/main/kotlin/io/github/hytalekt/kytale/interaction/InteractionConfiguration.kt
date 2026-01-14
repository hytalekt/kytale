package io.github.hytalekt.kytale.interaction

/**
 * DSL for configuring interactions on blocks, entities, and items.
 *
 * Example usage:
 * ```
 * block("hytale:custom_chest") {
 *     interactions {
 *         onActivate {
 *             distance(5.0)
 *             priority(InteractionPriority.HIGH)
 *             handler { context ->
 *                 // Open chest UI
 *                 context.player.openUI(chestUI)
 *             }
 *         }
 *
 *         onRightClick {
 *             requiresItem("hytale:key")
 *             handler { context ->
 *                 // Unlock chest
 *             }
 *         }
 *     }
 * }
 * ```
 */

/**
 * Builder for configuring interactions on an object
 */
@InteractionDsl
class InteractionConfigBuilder {
    private val interactions = mutableListOf<InteractionConfig>()

    /**
     * Configures activation interaction (primary interact)
     */
    fun onActivate(configure: InteractionBuilder.() -> Unit) {
        val builder = InteractionBuilder(InteractionType.ACTIVATE)
        builder.configure()
        interactions.add(builder.build())
    }

    /**
     * Configures right-click interaction
     */
    fun onRightClick(configure: InteractionBuilder.() -> Unit) {
        val builder = InteractionBuilder(InteractionType.RIGHT_CLICK)
        builder.configure()
        interactions.add(builder.build())
    }

    /**
     * Configures left-click/attack interaction
     */
    fun onAttack(configure: InteractionBuilder.() -> Unit) {
        val builder = InteractionBuilder(InteractionType.ATTACK)
        builder.configure()
        interactions.add(builder.build())
    }

    /**
     * Configures use item interaction
     */
    fun onUseItem(configure: InteractionBuilder.() -> Unit) {
        val builder = InteractionBuilder(InteractionType.USE_ITEM)
        builder.configure()
        interactions.add(builder.build())
    }
}

/**
 * Builder for individual interaction configuration
 */
@InteractionDsl
class InteractionBuilder(
    private val type: InteractionType,
) {
    private var distance: Double = 3.0
    private var priority: InteractionPriority = InteractionPriority.NORMAL
    private var requiredItem: String? = null
    private var requiredGameMode: String? = null
    private var handler: ((Any) -> Unit)? = null

    /**
     * Sets the interaction distance
     */
    fun distance(blocks: Double) {
        distance = blocks
    }

    /**
     * Sets the interaction priority
     */
    fun priority(value: InteractionPriority) {
        priority = value
    }

    /**
     * Requires a specific item to be held
     */
    fun requiresItem(itemId: String) {
        requiredItem = itemId
    }

    /**
     * Requires a specific game mode
     */
    fun requiresGameMode(mode: String) {
        requiredGameMode = mode
    }

    /**
     * Sets the interaction handler
     */
    fun handler(block: (Any) -> Unit) {
        handler = block
    }

    /**
     * Builds the interaction config
     */
    fun build(): InteractionConfig {
        require(handler != null) { "Interaction handler must be set" }
        return InteractionConfig(
            type = type,
            distance = distance,
            priority = priority,
            requiredItem = requiredItem,
            requiredGameMode = requiredGameMode,
            handler = handler!!,
        )
    }
}

/**
 * Represents interaction configuration
 */
data class InteractionConfig(
    val type: InteractionType,
    val distance: Double,
    val priority: InteractionPriority,
    val requiredItem: String?,
    val requiredGameMode: String?,
    val handler: (Any) -> Unit,
)

/**
 * Types of interactions
 */
enum class InteractionType {
    ACTIVATE,
    RIGHT_CLICK,
    ATTACK,
    USE_ITEM,
    BREAK,
    PLACE,
}

/**
 * Interaction priority levels
 */
enum class InteractionPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
}

/**
 * DSL function for configuring interactions
 */
fun interactions(configure: InteractionConfigBuilder.() -> Unit): List<InteractionConfig> {
    val builder = InteractionConfigBuilder()
    builder.configure()
    // TODO: Return built interactions
    return emptyList()
}
