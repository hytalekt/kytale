package aster.amo.kytale.ui.dsl

/**
 * NumberField element - numeric input with validation.
 */
class UiNumberField : UiElement() {
    var value: Number? = null
    var minValue: Number? = null
    var maxValue: Number? = null
    var step: Number? = null
    var maxDecimalPlaces: Int? = null
    var useTemplate: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        if (useTemplate) {
            appendLine("${indent(indent)}\$C.@NumberField$idPart {")
            anchor?.let { appendLine("${indent(indent + 1)}@Anchor = ${it.serialize()};") }
        } else {
            appendLine("${indent(indent)}NumberField$idPart {")
            append(serializeProperties(indent + 1))
        }
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }

        // Format block for validation
        val hasFormat = minValue != null || maxValue != null || step != null || maxDecimalPlaces != null
        if (hasFormat) {
            appendLine("${indent(indent + 1)}Format: (")
            maxDecimalPlaces?.let { appendLine("${indent(indent + 2)}MaxDecimalPlaces: $it,") }
            step?.let { appendLine("${indent(indent + 2)}Step: $it,") }
            minValue?.let { appendLine("${indent(indent + 2)}MinValue: $it,") }
            maxValue?.let { appendLine("${indent(indent + 2)}MaxValue: $it,") }
            appendLine("${indent(indent + 1)});")
        }
        appendLine("${indent(indent)}}")
    }
}
