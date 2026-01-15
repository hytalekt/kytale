package aster.amo.kytale.ui.dsl

/**
 * Tab element - tab button for tab panels.
 */
class UiTab : UiElement() {
    var text: String? = null
    var selected: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Tab$idPart {")
        append(serializeProperties(indent + 1))
        text?.let { appendLine("${indent(indent + 1)}Text: \"$it\";") }
        selected?.let { appendLine("${indent(indent + 1)}Selected: $it;") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * TabPanel element - container that shows content based on selected tab.
 */
class UiTabPanel : UiElement() {
    val tabs = mutableListOf<UiTab>()
    val panels = mutableListOf<UiGroup>()

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}TabPanel$idPart {")
        append(serializeProperties(indent + 1))
        // Tab bar
        appendLine("${indent(indent + 1)}Group #TabBar {")
        appendLine("${indent(indent + 2)}LayoutMode: Left;")
        tabs.forEach { append(it.serialize(indent + 2)) }
        appendLine("${indent(indent + 1)}}")
        // Content panels
        panels.forEach { append(it.serialize(indent + 1)) }
        appendLine("${indent(indent)}}")
    }
}
