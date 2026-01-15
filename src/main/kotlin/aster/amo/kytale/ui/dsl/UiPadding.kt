package aster.amo.kytale.ui.dsl

/**
 * Padding values.
 */
data class UiPadding(
    var left: Int? = null,
    var right: Int? = null,
    var top: Int? = null,
    var bottom: Int? = null,
    var horizontal: Int? = null,
    var vertical: Int? = null
) {
    fun serialize(): String {
        val parts = mutableListOf<String>()
        horizontal?.let { parts.add("Horizontal: $it") }
        vertical?.let { parts.add("Vertical: $it") }
        left?.let { parts.add("Left: $it") }
        right?.let { parts.add("Right: $it") }
        top?.let { parts.add("Top: $it") }
        bottom?.let { parts.add("Bottom: $it") }
        return "(${parts.joinToString(", ")})"
    }
}
