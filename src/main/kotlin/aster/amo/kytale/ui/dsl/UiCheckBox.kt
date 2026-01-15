package aster.amo.kytale.ui.dsl

/**
 * CheckBox element - toggle checkbox.
 */
class UiCheckBox : UiElement() {
    var value: Boolean? = null
    var useTemplate: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        if (useTemplate) {
            appendLine("${indent(indent)}\$C.@CheckBox$idPart {")
        } else {
            appendLine("${indent(indent)}CheckBox$idPart {")
        }
        append(serializeProperties(indent + 1))
        value?.let { appendLine("${indent(indent + 1)}Value: $it;") }
        appendLine("${indent(indent)}}")
    }
}
