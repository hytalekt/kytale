package aster.amo.kytale.ui.dsl

/**
 * Group element - container for other elements.
 */
class UiGroup : UiElement() {
    var layoutMode: LayoutMode? = null
    var scrollbarStyle: String? = null
    var clipChildren: Boolean? = null
    val children = mutableListOf<UiElement>()

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Group$idPart {")
        append(serializeProperties(indent + 1))
        layoutMode?.let { appendLine("${indent(indent + 1)}LayoutMode: ${it.value};") }
        scrollbarStyle?.let { appendLine("${indent(indent + 1)}ScrollbarStyle: $it;") }
        clipChildren?.let { appendLine("${indent(indent + 1)}ClipChildren: $it;") }
        children.forEach { append(it.serialize(indent + 1)) }
        appendLine("${indent(indent)}}")
    }
}
