package aster.amo.kytale.ui.dsl

/**
 * Slider element - horizontal slider for integer values.
 */
class UiSlider : UiElement() {
    var min: Int? = null
    var max: Int? = null
    var step: Int? = null
    var value: Int? = null
    var useDefaultStyle: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Slider$idPart {")
        append(serializeProperties(indent + 1))
        if (useDefaultStyle) {
            appendLine("${indent(indent + 1)}Style: \$C.@DefaultSliderStyle;")
        }
        min?.let { appendLine("${indent(indent + 1)}Min: $it;") }
        max?.let { appendLine("${indent(indent + 1)}Max: $it;") }
        step?.let { appendLine("${indent(indent + 1)}Step: $it;") }
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * FloatSlider element - horizontal slider for floating-point values.
 */
class UiFloatSlider : UiElement() {
    var min: Double? = null
    var max: Double? = null
    var step: Double? = null
    var value: Double? = null
    var useDefaultStyle: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}FloatSlider$idPart {")
        append(serializeProperties(indent + 1))
        if (useDefaultStyle) {
            appendLine("${indent(indent + 1)}Style: \$C.@DefaultSliderStyle;")
        }
        min?.let { appendLine("${indent(indent + 1)}Min: $it;") }
        max?.let { appendLine("${indent(indent + 1)}Max: $it;") }
        step?.let { appendLine("${indent(indent + 1)}Step: $it;") }
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }
        appendLine("${indent(indent)}}")
    }
}
