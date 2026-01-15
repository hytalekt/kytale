package aster.amo.kytale.ui.dsl

/**
 * ToggleButton element - button that toggles between on/off states.
 */
class UiToggleButton : UiElement() {
    var text: String? = null
    var value: Boolean? = null
    var onStyle: UiButtonStyle? = null
    var offStyle: UiButtonStyle? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ToggleButton$idPart {")
        append(serializeProperties(indent + 1))
        text?.let { appendLine("${indent(indent + 1)}Text: \"$it\";") }
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }
        // ToggleButton uses combined style with On/Off states
        if (onStyle != null || offStyle != null) {
            appendLine("${indent(indent + 1)}Style: (")
            onStyle?.let { style ->
                appendLine("${indent(indent + 2)}On: (")
                style.defaultBackground?.let { appendLine("${indent(indent + 3)}Background: $it,") }
                style.labelStyle?.let { appendLine("${indent(indent + 3)}LabelStyle: ${it.serialize()},") }
                appendLine("${indent(indent + 2)}),")
            }
            offStyle?.let { style ->
                appendLine("${indent(indent + 2)}Off: (")
                style.defaultBackground?.let { appendLine("${indent(indent + 3)}Background: $it,") }
                style.labelStyle?.let { appendLine("${indent(indent + 3)}LabelStyle: ${it.serialize()},") }
                appendLine("${indent(indent + 2)}),")
            }
            appendLine("${indent(indent + 1)});")
        }
        appendLine("${indent(indent)}}")
    }
}
