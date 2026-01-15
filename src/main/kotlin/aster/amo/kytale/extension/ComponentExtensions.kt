package aster.amo.kytale.extension

import com.hypixel.hytale.server.core.Message

/**
 * DSL extensions for Hytale's Message system.
 *
 * Provides Kotlin-idiomatic ways to create formatted messages
 * using Hytale's native Message and FormattedMessage classes.
 *
 * Example:
 * ```kotlin
 * val message = message {
 *     +"Welcome "
 *     colored("0xFFD700") { +"to the server!" }
 *     child {
 *         +"\nEnjoy your stay."
 *     }
 * }
 * ```
 */

/**
 * Common hex color constants for convenience.
 */
object Colors {
    const val BLACK = "0x000000"
    const val DARK_BLUE = "0x0000AA"
    const val DARK_GREEN = "0x00AA00"
    const val DARK_AQUA = "0x00AAAA"
    const val DARK_RED = "0xAA0000"
    const val DARK_PURPLE = "0xAA00AA"
    const val GOLD = "0xFFAA00"
    const val GRAY = "0xAAAAAA"
    const val DARK_GRAY = "0x555555"
    const val BLUE = "0x5555FF"
    const val GREEN = "0x55FF55"
    const val AQUA = "0x55FFFF"
    const val RED = "0xFF5555"
    const val LIGHT_PURPLE = "0xFF55FF"
    const val YELLOW = "0xFFFF55"
    const val WHITE = "0xFFFFFF"
}

/**
 * Builder for creating Hytale Message instances with a DSL.
 */
class MessageBuilder {
    private var rootMessage: Message? = null
    private var currentText: StringBuilder = StringBuilder()
    private var currentColor: String? = null

    /**
     * Appends plain text to the message.
     *
     * @param text the text to append
     */
    operator fun String.unaryPlus() {
        currentText.append(this)
    }

    /**
     * Sets the color for the current message segment.
     *
     * @param hex the hex color string (e.g., "0xFFD700")
     */
    fun color(hex: String) {
        currentColor = hex
    }

    /**
     * Creates a colored text segment.
     *
     * @param hex the hex color string
     * @param block builder for the colored content
     */
    fun colored(hex: String, block: MessageBuilder.() -> Unit) {
        flushCurrent()
        val nested = MessageBuilder().apply(block)
        val nestedMsg = nested.build()
        nestedMsg.color(hex)
        addToRoot(nestedMsg)
    }

    /**
     * Adds a child message.
     *
     * @param block builder for the child message
     */
    fun child(block: MessageBuilder.() -> Unit) {
        flushCurrent()
        val nested = MessageBuilder().apply(block)
        addToRoot(nested.build())
    }

    /**
     * Appends a newline.
     */
    fun newline() {
        currentText.append("\n")
    }

    /**
     * Appends a space.
     */
    fun space() {
        currentText.append(" ")
    }

    private fun flushCurrent() {
        if (currentText.isNotEmpty()) {
            val msg = Message.raw(currentText.toString())
            currentColor?.let { msg.color(it) }
            addToRoot(msg)
            currentText = StringBuilder()
            currentColor = null
        }
    }

    private fun addToRoot(msg: Message) {
        if (rootMessage == null) {
            rootMessage = msg
        } else {
            rootMessage!!.insert(msg)
        }
    }

    /**
     * Builds the final Message.
     *
     * @return the constructed Message
     */
    fun build(): Message {
        flushCurrent()
        return rootMessage ?: Message.raw("")
    }
}

/**
 * Creates a Message using the builder DSL.
 *
 * Example:
 * ```kotlin
 * val greeting = message {
 *     colored(Colors.GREEN) { +"Hello, " }
 *     +playerName
 *     +"!"
 * }
 * player.sendMessage(greeting)
 * ```
 *
 * @param block the builder configuration
 * @return the constructed Message
 */
fun message(block: MessageBuilder.() -> Unit): Message {
    return MessageBuilder().apply(block).build()
}

/**
 * Creates a simple colored message.
 *
 * @param hex the hex color string
 * @param content the text content
 * @return the colored Message
 */
fun coloredMessage(hex: String, content: String): Message {
    return Message.raw(content).apply { color(hex) }
}

/**
 * Extension to set color on a Message and return it for chaining.
 *
 * @param hex the hex color string
 * @return this Message for chaining
 */
fun Message.withColor(hex: String): Message {
    this.color(hex)
    return this
}

/**
 * Extension to add a child message and return parent for chaining.
 *
 * @param child the child Message to add
 * @return this Message for chaining
 */
fun Message.withChild(child: Message): Message {
    this.children.add(child)
    return this
}

/**
 * Extension to add a raw text child.
 *
 * @param text the text for the child message
 * @return this Message for chaining
 */
fun Message.withChild(text: String): Message {
    this.children.add(Message.raw(text))
    return this
}
