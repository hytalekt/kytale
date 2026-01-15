package aster.amo.kytale.ui.dsl

/**
 * Label style.
 */
data class UiLabelStyle(
    var fontSize: Int? = null,
    var textColor: String? = null,
    var renderBold: Boolean? = null,
    var renderUppercase: Boolean? = null,
    var horizontalAlignment: HorizontalAlignment? = null,
    var verticalAlignment: VerticalAlignment? = null,
    var wrap: Boolean? = null,
    var lineSpacing: Int? = null
) {
    fun serialize(): String {
        val parts = mutableListOf<String>()
        fontSize?.let { parts.add("FontSize: $it") }
        textColor?.let { parts.add("TextColor: $it") }
        renderBold?.let { parts.add("RenderBold: $it") }
        renderUppercase?.let { parts.add("RenderUppercase: $it") }
        horizontalAlignment?.let { parts.add("HorizontalAlignment: ${it.value}") }
        verticalAlignment?.let { parts.add("VerticalAlignment: ${it.value}") }
        wrap?.let { parts.add("Wrap: $it") }
        lineSpacing?.let { parts.add("LineSpacing: $it") }
        return "(${parts.joinToString(", ")})"
    }
}

/**
 * Button style with states.
 */
class UiButtonStyle {
    var defaultBackground: String? = null
    var hoveredBackground: String? = null
    var pressedBackground: String? = null
    var disabledBackground: String? = null
    var labelStyle: UiLabelStyle? = null
    var hoveredLabelStyle: UiLabelStyle? = null
    var pressedLabelStyle: UiLabelStyle? = null
    var disabledLabelStyle: UiLabelStyle? = null
    var useSounds: Boolean = true

    fun serialize(indent: Int): String {
        val i = "  ".repeat(indent)
        return buildString {
            appendLine("${i}Style: (")

            // Default state
            append("${i}  Default: (")
            val defaultParts = mutableListOf<String>()
            defaultBackground?.let { defaultParts.add("Background: $it") }
            labelStyle?.let { defaultParts.add("LabelStyle: ${it.serialize()}") }
            append(defaultParts.joinToString(", "))
            appendLine("),")

            // Hovered state
            append("${i}  Hovered: (")
            val hoveredParts = mutableListOf<String>()
            (hoveredBackground ?: defaultBackground)?.let { hoveredParts.add("Background: $it") }
            (hoveredLabelStyle ?: labelStyle)?.let { hoveredParts.add("LabelStyle: ${it.serialize()}") }
            append(hoveredParts.joinToString(", "))
            appendLine("),")

            // Pressed state (if specified)
            pressedBackground?.let { pressed ->
                append("${i}  Pressed: (")
                val pressedParts = mutableListOf<String>()
                pressedParts.add("Background: $pressed")
                (pressedLabelStyle ?: labelStyle)?.let { pressedParts.add("LabelStyle: ${it.serialize()}") }
                append(pressedParts.joinToString(", "))
                appendLine("),")
            }

            // Disabled state (if specified)
            disabledBackground?.let { disabled ->
                append("${i}  Disabled: (")
                val disabledParts = mutableListOf<String>()
                disabledParts.add("Background: $disabled")
                (disabledLabelStyle ?: labelStyle)?.let { disabledParts.add("LabelStyle: ${it.serialize()}") }
                append(disabledParts.joinToString(", "))
                appendLine("),")
            }

            if (useSounds) {
                appendLine("${i}  Sounds: \$C.@ButtonSounds,")
            }
            appendLine("${i});")
        }
    }
}

/**
 * Icon button style for Button and ItemSlotButton elements.
 */
class UiIconButtonStyle {
    var defaultBackground: String? = null
    var hoveredBackground: String? = null
    var pressedBackground: String? = null
    var disabledBackground: String? = null
    var iconColor: String? = null
    var hoveredIconColor: String? = null
    var useSounds: Boolean = true

    fun serialize(indent: Int): String {
        val i = "  ".repeat(indent)
        return buildString {
            appendLine("${i}Style: (")

            // Default state
            defaultBackground?.let {
                appendLine("${i}  Default: (Background: $it),")
            }

            // Hovered state
            hoveredBackground?.let {
                appendLine("${i}  Hovered: (Background: $it),")
            }

            // Pressed state
            pressedBackground?.let {
                appendLine("${i}  Pressed: (Background: $it),")
            }

            // Disabled state
            disabledBackground?.let {
                appendLine("${i}  Disabled: (Background: $it),")
            }

            if (useSounds) {
                appendLine("${i}  Sounds: \$C.@ButtonSounds,")
            }
            appendLine("${i});")
        }
    }
}

/**
 * Slider style configuration.
 */
class UiSliderStyle {
    var trackBackground: String? = null
    var fillBackground: String? = null
    var thumbBackground: String? = null
    var thumbSize: Int? = null

    fun serialize(): String {
        val parts = mutableListOf<String>()
        trackBackground?.let { parts.add("TrackBackground: $it") }
        fillBackground?.let { parts.add("FillBackground: $it") }
        thumbBackground?.let { parts.add("ThumbBackground: $it") }
        thumbSize?.let { parts.add("ThumbSize: $it") }
        return "(${parts.joinToString(", ")})"
    }
}
