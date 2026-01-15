package io.github.hytalekt.kytale.message

import com.hypixel.hytale.protocol.MaybeBool
import com.hypixel.hytale.server.core.Message
import java.awt.Color

/**
 * A convenient [Message.raw] builder function
 *
 * Builds a styled message with optional formatting. Styles default to `null`,
 * which corresponds to being set as [MaybeBool.Null]
 *
 * @param text The raw message contents
 * @param bold Whether to style the text as bold. If set to null, no bold style is applied
 * @param italic Whether to style the text as italic. If set to null, no italic style is applied
 * @param underlined Whether to style the text as underlined. If set to null, no underline is applied
 * @param monospace Whether to style the text as monospace. If set to null, no monospace style is applied
 * @param color The color of the text. If set to null, no color is applied
 * @param children The children of the Message
 *
 * @return A styled [Message]
 *
 * @see com.hypixel.hytale.server.core.Message.raw
 */
@Suppress("NOTHING_TO_INLINE")
inline fun text(
    text: String,
    bold: Boolean? = null,
    italic: Boolean? = null,
    underlined: Boolean? = null,
    monospace: Boolean? = null,
    color: Color? = null,
    children: List<Message>? = null,
): Message =
    Message.raw(text).apply {
        bold?.let(this::bold)
        italic?.let(this::italic)
        underlined?.let(this::underlined)
        monospace?.let(this::monospace)
        color?.let(this::color)
        children?.let(this::insertAll)
    }

/**
 * A convenient [Message.translation] builder function
 *
 * Builds a styled message with optional formatting. Styles default to `null`,
 * which corresponds to being set as [MaybeBool.Null]
 *
 * @param text The raw message contents
 * @param bold Whether to style the text as bold. If set to null, no bold style is applied
 * @param italic Whether to style the text as italic. If set to null, no italic style is applied
 * @param underlined Whether to style the text as underlined. If set to null, no underline is applied
 * @param monospace Whether to style the text as monospace. If set to null, no monospace style is applied
 * @param color The color of the text. If set to null, no color is applied
 * @param children The children of the Message
 *
 * @return A styled [Message]
 *
 * @see com.hypixel.hytale.server.core.Message.raw
 */
fun i18n(
    messageId: String,
    bold: Boolean? = null,
    italic: Boolean? = null,
    underlined: Boolean? = null,
    monospace: Boolean? = null,
    color: Color? = null,
    children: List<Message>? = null,
): Message =
    Message.translation(messageId).apply {
        bold?.let(this::bold)
        italic?.let(this::italic)
        underlined?.let(this::underlined)
        monospace?.let(this::monospace)
        color?.let(this::color)
        children?.let(this::insertAll)
    }

/**
 * Joins messages together under a parent
 *
 * @see io.github.hytalekt.kytale.message.append
 * @see com.hypixel.hytale.server.core.Message.join
 */
fun messageOf(vararg messages: Message): Message = Message.join(*messages)

/**
 * Applies the `bold` style to a message
 *
 * @see com.hypixel.hytale.server.core.Message.bold
 */
fun Message.bold(): Message = bold(true)

/**
 * Applies the `italic` style to a message
 *
 * @see com.hypixel.hytale.server.core.Message.italic
 */
fun Message.italic(): Message = italic(true)

/**
 * Applies the `monospace` style to a message
 *
 * @see com.hypixel.hytale.server.core.Message.monospace
 */
fun Message.monospace(): Message = monospace(true)

/**
 * Applies the `underlined` style to a message
 * Hytale currently doesn't provide an `underlined` method, so this should be used until then
 *
 * @param value Whether to underline the message, defaults to true
 */
fun Message.underlined(value: Boolean = true): Message =
    apply {
        formattedMessage.underlined = if (value) MaybeBool.True else MaybeBool.False
    }

/**
 * Resets a Message's styling with the underlying FormattedMessage object
 */
fun Message.reset(): Message =
    apply {
        formattedMessage.bold = MaybeBool.Null
        formattedMessage.italic = MaybeBool.Null
        formattedMessage.monospace = MaybeBool.Null
        formattedMessage.underlined = MaybeBool.Null
    }

/**
 * Alias for [com.hypixel.hytale.server.core.Message.insert]
 *
 * @see io.github.hytalekt.kytale.message.messageOf
 */
@JvmSynthetic
operator fun Message.plus(other: Message): Message = insert(other)

/**
 * Alias for [com.hypixel.hytale.server.core.Message.insertAll]
 *
 * @see io.github.hytalekt.kytale.message.messageOf
 */
fun Message.append(vararg messages: Message): Message = insertAll(*messages)
