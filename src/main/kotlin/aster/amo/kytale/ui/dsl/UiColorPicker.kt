package aster.amo.kytale.ui.dsl

/**
 * ColorPicker element - full color picker widget.
 */
class UiColorPicker : UiElement() {
    var format: ColorFormat = ColorFormat.Rgb

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ColorPicker$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent + 1)}Format: ${format.value};")
        appendLine("${indent(indent + 1)}Style: \$C.@DefaultColorPickerStyle;")
        appendLine("${indent(indent)}}")
    }
}

/**
 * ColorPickerDropdownBox element - compact color picker in dropdown.
 */
class UiColorPickerDropdownBox : UiElement() {
    var format: ColorFormat = ColorFormat.Rgb
    var displayTextField: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ColorPickerDropdownBox$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent + 1)}Format: ${format.value};")
        appendLine("${indent(indent + 1)}Style: \$C.@DefaultColorPickerDropdownBoxStyle;")
        displayTextField?.let { appendLine("${indent(indent + 1)}DisplayTextField: $it;") }
        appendLine("${indent(indent)}}")
    }
}
