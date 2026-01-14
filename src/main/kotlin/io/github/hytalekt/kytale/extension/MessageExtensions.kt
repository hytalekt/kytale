package io.github.hytalekt.kytale.extension

import com.hypixel.hytale.server.core.Message

/**
 * Extension functions for Message creation and manipulation.
 *
 * Provides convenient ways to create formatted messages.
 */

/**
 * Creates a message from a string
 */
fun String.toMessage(): Message {
    // TODO: Convert string to Message
    TODO("Implement string to Message conversion")
}

/**
 * Message builder DSL for creating formatted messages
 *
 * Example:
 * ```
 * val message = message {
 *     text("Hello ")
 *     text("World") {
 *         color(Color.RED)
 *         bold()
 *     }
 *     text("!") {
 *         color(Color.GREEN)
 *         italic()
 *     }
 * }
 * ```
 */
@DslMarker
annotation class MessageDsl

@MessageDsl
class MessageBuilder {
    private val parts = mutableListOf<MessagePart>()

    /**
     * Adds a text component
     */
    fun text(
        content: String,
        configure: TextComponentBuilder.() -> Unit = {},
    ) {
        val builder = TextComponentBuilder(content)
        builder.configure()
        parts.add(builder.build())
    }

    /**
     * Adds a line break
     */
    fun newLine() {
        parts.add(MessagePart.NewLine)
    }

    /**
     * Builds the message
     */
    fun build(): Message {
        TODO("Build Message from parts")
    }
}

@MessageDsl
class TextComponentBuilder(
    private val content: String,
) {
    private var color: Color? = null
    private var bold: Boolean = false
    private var italic: Boolean = false
    private var underline: Boolean = false
    private var strikethrough: Boolean = false

    /**
     * Sets the text color
     */
    fun color(value: Color) {
        color = value
    }

    /**
     * Makes the text bold
     */
    fun bold() {
        bold = true
    }

    /**
     * Makes the text italic
     */
    fun italic() {
        italic = true
    }

    /**
     * Underlines the text
     */
    fun underline() {
        underline = true
    }

    /**
     * Strikes through the text
     */
    fun strikethrough() {
        strikethrough = true
    }

    fun build(): MessagePart.Text = MessagePart.Text(content, color, bold, italic, underline, strikethrough)
}

sealed class MessagePart {
    data class Text(
        val content: String,
        val color: Color?,
        val bold: Boolean,
        val italic: Boolean,
        val underline: Boolean,
        val strikethrough: Boolean,
    ) : MessagePart()

    object NewLine : MessagePart()
}

/**
 * Common colors for messages
 */
enum class Color {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
}

/**
 * DSL function to create a message
 */
fun message(configure: MessageBuilder.() -> Unit): Message = MessageBuilder().apply(configure).build()
