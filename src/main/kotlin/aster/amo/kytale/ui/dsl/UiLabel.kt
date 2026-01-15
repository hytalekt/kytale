package aster.amo.kytale.ui.dsl

/**
 * Label element - text display.
 */
class UiLabel : UiElement() {
    var text: String? = null
    var style: UiLabelStyle? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Label$idPart {")
        append(serializeProperties(indent + 1))
        style?.let { appendLine("${indent(indent + 1)}Style: ${it.serialize()};") }
        text?.let { appendLine("${indent(indent + 1)}Text: \"$it\";") }
        appendLine("${indent(indent)}}")
    }
}
