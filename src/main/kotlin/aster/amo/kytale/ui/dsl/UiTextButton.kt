package aster.amo.kytale.ui.dsl

/**
 * TextButton element - clickable button with text using $C.@TextButton template.
 */
class UiTextButton : UiElement() {
    var text: String? = null
    var style: UiButtonStyle? = null
    var disabled: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}\$C.@TextButton$idPart {")
        append(serializeProperties(indent + 1))
        text?.let { appendLine("${indent(indent + 1)}Text: \"$it\";") }
        disabled?.let { appendLine("${indent(indent + 1)}Disabled: $it;") }
        style?.let { append(it.serialize(indent + 1)) }
        appendLine("${indent(indent)}}")
    }
}
