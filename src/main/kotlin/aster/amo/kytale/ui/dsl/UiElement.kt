package aster.amo.kytale.ui.dsl

/**
 * Base class for all UI elements.
 */
sealed class UiElement {
    var id: String? = null
    var anchor: UiAnchor? = null
    var flexWeight: Int? = null
    var padding: UiPadding? = null
    var background: String? = null
    var visible: Boolean? = null
    var tooltipText: String? = null
    var hitTestVisible: Boolean? = null

    abstract fun serialize(indent: Int = 0): String

    protected fun indent(level: Int) = "  ".repeat(level)

    protected fun serializeProperties(indent: Int): String = buildString {
        id?.let { } // ID is handled in element declaration
        anchor?.let { appendLine("${indent(indent)}Anchor: ${it.serialize()};") }
        flexWeight?.let { appendLine("${indent(indent)}FlexWeight: $it;") }
        padding?.let { appendLine("${indent(indent)}Padding: ${it.serialize()};") }
        background?.let { appendLine("${indent(indent)}Background: $it;") }
        visible?.let { appendLine("${indent(indent)}Visible: $it;") }
        tooltipText?.let { appendLine("${indent(indent)}TooltipText: \"$it\";") }
        hitTestVisible?.let { appendLine("${indent(indent)}HitTestVisible: $it;") }
    }
}
