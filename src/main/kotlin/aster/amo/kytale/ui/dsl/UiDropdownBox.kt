package aster.amo.kytale.ui.dsl

/**
 * DropdownBox element - dropdown selection menu.
 */
class UiDropdownBox : UiElement() {
    var noItemsText: String? = null
    var useTemplate: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        if (useTemplate) {
            appendLine("${indent(indent)}\$C.@DropdownBox$idPart {")
            anchor?.let { appendLine("${indent(indent + 1)}@Anchor = ${it.serialize()};") }
        } else {
            appendLine("${indent(indent)}DropdownBox$idPart {")
            append(serializeProperties(indent + 1))
        }
        noItemsText?.let { appendLine("${indent(indent + 1)}NoItemsText: \"$it\";") }
        appendLine("${indent(indent)}}")
    }
}
