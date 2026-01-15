package aster.amo.kytale.ui.dsl

/**
 * RadioButton element - single-select option button.
 */
class UiRadioButton : UiElement() {
    var text: String? = null
    var groupName: String? = null
    var value: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}RadioButton$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent + 1)}Style: \$C.@DefaultRadioButtonStyle;")
        groupName?.let { appendLine("${indent(indent + 1)}GroupName: \"$it\";") }
        text?.let { appendLine("${indent(indent + 1)}Text: \"$it\";") }
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }
        appendLine("${indent(indent)}}")
    }
}
