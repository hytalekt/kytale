package aster.amo.kytale.ui.dsl

/**
 * Anchor positioning with extended properties.
 */
data class UiAnchor(
    var left: Int? = null,
    var right: Int? = null,
    var top: Int? = null,
    var bottom: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var minWidth: Int? = null,
    var maxWidth: Int? = null,
    var minHeight: Int? = null,
    var maxHeight: Int? = null,
    var full: Boolean? = null,
    var horizontal: Boolean? = null,
    var vertical: Boolean? = null
) {
    fun serialize(): String {
        val parts = mutableListOf<String>()
        width?.let { parts.add("Width: $it") }
        height?.let { parts.add("Height: $it") }
        left?.let { parts.add("Left: $it") }
        right?.let { parts.add("Right: $it") }
        top?.let { parts.add("Top: $it") }
        bottom?.let { parts.add("Bottom: $it") }
        minWidth?.let { parts.add("MinWidth: $it") }
        maxWidth?.let { parts.add("MaxWidth: $it") }
        minHeight?.let { parts.add("MinHeight: $it") }
        maxHeight?.let { parts.add("MaxHeight: $it") }
        full?.let { if (it) parts.add("Full: true") }
        horizontal?.let { if (it) parts.add("Horizontal: true") }
        vertical?.let { if (it) parts.add("Vertical: true") }
        return "(${parts.joinToString(", ")})"
    }
}
