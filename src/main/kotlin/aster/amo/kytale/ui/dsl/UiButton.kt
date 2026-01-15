package aster.amo.kytale.ui.dsl

/**
 * Button element - icon-only clickable button.
 */
class UiButton : UiElement() {
    var style: UiIconButtonStyle? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Button$idPart {")
        append(serializeProperties(indent + 1))
        style?.let { append(it.serialize(indent + 1)) }
        appendLine("${indent(indent)}}")
    }
}
