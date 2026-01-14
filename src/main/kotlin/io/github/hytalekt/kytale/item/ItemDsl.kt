package io.github.hytalekt.kytale.item

import com.hypixel.hytale.server.core.inventory.ItemStack

/**
 * DSL for creating and configuring ItemStacks.
 *
 * Example usage:
 * ```
 * val sword = itemStack("hytale:iron_sword") {
 *     amount(1)
 *     durability(100)
 *     displayName("Legendary Sword")
 *     lore {
 *         +"A powerful blade"
 *         +"Forged in dragon fire"
 *     }
 *     metadata {
 *         set("damage_bonus", 5)
 *         set("enchantments", listOf("sharpness", "fire_aspect"))
 *     }
 * }
 * ```
 */
@DslMarker
annotation class ItemDsl

/**
 * Builder for ItemStack creation
 */
@ItemDsl
class ItemStackBuilder(
    private val itemId: String,
) {
    private var amount: Int = 1
    private var durability: Int? = null
    private var displayName: String? = null
    private val loreLines = mutableListOf<String>()
    private val metadata = mutableMapOf<String, Any>()

    /**
     * Sets the stack amount
     */
    fun amount(count: Int) {
        amount = count
    }

    /**
     * Sets the durability (for tools/armor)
     */
    fun durability(value: Int) {
        durability = value
    }

    /**
     * Sets a custom display name
     */
    fun displayName(name: String) {
        displayName = name
    }

    /**
     * Configures item lore
     */
    fun lore(configure: LoreBuilder.() -> Unit) {
        val builder = LoreBuilder()
        builder.configure()
        loreLines.addAll(builder.lines)
    }

    /**
     * Configures item metadata
     */
    fun metadata(configure: MetadataBuilder.() -> Unit) {
        val builder = MetadataBuilder()
        builder.configure()
        metadata.putAll(builder.data)
    }

    /**
     * Builds the ItemStack
     */
    fun build(): ItemStack {
        TODO("Implement ItemStack creation for itemId: $itemId")
    }
}

/**
 * Builder for item lore
 */
@ItemDsl
class LoreBuilder {
    internal val lines = mutableListOf<String>()

    /**
     * Adds a lore line using unary plus operator
     */
    operator fun String.unaryPlus() {
        lines.add(this)
    }

    /**
     * Adds a lore line
     */
    fun line(text: String) {
        lines.add(text)
    }

    /**
     * Adds multiple lore lines
     */
    fun lines(vararg texts: String) {
        lines.addAll(texts)
    }
}

/**
 * Builder for item metadata
 */
@ItemDsl
class MetadataBuilder {
    internal val data = mutableMapOf<String, Any>()

    /**
     * Sets a metadata value
     */
    fun set(
        key: String,
        value: Any,
    ) {
        data[key] = value
    }

    /**
     * Sets a string metadata value (infix notation)
     */
    infix fun String.to(value: String) {
        data[this] = value
    }

    /**
     * Sets an integer metadata value (infix notation)
     */
    infix fun String.to(value: Int) {
        data[this] = value
    }

    /**
     * Sets a boolean metadata value (infix notation)
     */
    infix fun String.to(value: Boolean) {
        data[this] = value
    }
}

/**
 * DSL function to create an ItemStack
 */
fun itemStack(
    itemId: String,
    configure: ItemStackBuilder.() -> Unit = {},
): ItemStack = ItemStackBuilder(itemId).apply(configure).build()

/**
 * DSL function to create an ItemStack with just the ID
 */
fun itemStack(itemId: String): ItemStack = ItemStackBuilder(itemId).build()
