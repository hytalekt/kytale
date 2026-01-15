package aster.amo.kytale.ui.dsl

/**
 * TextField element - single-line text input.
 */
class UiTextField : UiElement() {
    var placeholderText: String? = null
    var maxLength: Int? = null
    var useTemplate: Boolean = true

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        if (useTemplate) {
            appendLine("${indent(indent)}\$C.@TextField$idPart {")
            anchor?.let { appendLine("${indent(indent + 1)}@Anchor = ${it.serialize()};") }
        } else {
            appendLine("${indent(indent)}TextField$idPart {")
            append(serializeProperties(indent + 1))
        }
        placeholderText?.let { appendLine("${indent(indent + 1)}PlaceholderText: \"$it\";") }
        maxLength?.let { appendLine("${indent(indent + 1)}MaxLength: $it;") }
        if (useTemplate) {
            flexWeight?.let { appendLine("${indent(indent + 1)}FlexWeight: $it;") }
        }
        appendLine("${indent(indent)}}")
    }
}

/**
 * MultilineTextField element - multi-line text input.
 */
class UiMultilineTextField : UiElement() {
    var placeholderText: String? = null
    var autoGrow: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}MultilineTextField$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent + 1)}Style: \$C.@DefaultInputFieldStyle;")
        appendLine("${indent(indent + 1)}PlaceholderStyle: \$C.@DefaultInputFieldPlaceholderStyle;")
        appendLine("${indent(indent + 1)}Background: \$C.@InputBoxBackground;")
        autoGrow?.let { appendLine("${indent(indent + 1)}AutoGrow: $it;") }
        placeholderText?.let { appendLine("${indent(indent + 1)}PlaceholderText: \"$it\";") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * CompactTextField element - collapsible search/text field.
 */
class UiCompactTextField : UiElement() {
    var collapsedWidth: Int? = null
    var expandedWidth: Int? = null
    var placeholderText: String? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}CompactTextField$idPart {")
        append(serializeProperties(indent + 1))
        collapsedWidth?.let { appendLine("${indent(indent + 1)}CollapsedWidth: $it;") }
        expandedWidth?.let { appendLine("${indent(indent + 1)}ExpandedWidth: $it;") }
        placeholderText?.let { appendLine("${indent(indent + 1)}PlaceholderText: \"$it\";") }
        appendLine("${indent(indent)}}")
    }
}
