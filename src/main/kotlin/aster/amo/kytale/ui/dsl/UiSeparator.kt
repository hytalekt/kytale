package aster.amo.kytale.ui.dsl

/**
 * Separator element - horizontal or vertical divider line.
 */
class UiSeparator : UiElement() {
    var color: String? = null
    var thickness: Int = 1
    var orientation: SeparatorOrientation = SeparatorOrientation.Horizontal

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Group$idPart {")
        when (orientation) {
            SeparatorOrientation.Horizontal -> {
                appendLine("${indent(indent + 1)}Anchor: (Height: $thickness, Horizontal: true);")
            }
            SeparatorOrientation.Vertical -> {
                appendLine("${indent(indent + 1)}Anchor: (Width: $thickness, Vertical: true);")
            }
        }
        color?.let { appendLine("${indent(indent + 1)}Background: $it;") }
        appendLine("${indent(indent)}}")
    }
}

enum class SeparatorOrientation {
    Horizontal, Vertical
}
