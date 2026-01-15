package aster.amo.kytale.ui.dsl

/**
 * ItemIcon element - displays an item's icon.
 */
class UiItemIcon : UiElement() {
    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ItemIcon$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent)}}")
    }
}

/**
 * ItemSlot element - item slot with quality background support.
 */
class UiItemSlot : UiElement() {
    var showQualityBackground: Boolean? = null
    var showQuantity: Boolean? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ItemSlot$idPart {")
        append(serializeProperties(indent + 1))
        showQualityBackground?.let { appendLine("${indent(indent + 1)}ShowQualityBackground: $it;") }
        showQuantity?.let { appendLine("${indent(indent + 1)}ShowQuantity: $it;") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * ItemSlotButton element - clickable item slot button.
 */
class UiItemSlotButton : UiElement() {
    var layoutMode: LayoutMode? = null
    var style: UiIconButtonStyle? = null
    val children = mutableListOf<UiElement>()

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ItemSlotButton$idPart {")
        append(serializeProperties(indent + 1))
        layoutMode?.let { appendLine("${indent(indent + 1)}LayoutMode: ${it.value};") }
        style?.let { append(it.serialize(indent + 1)) }
        children.forEach { append(it.serialize(indent + 1)) }
        appendLine("${indent(indent)}}")
    }
}

/**
 * ItemGrid element - grid of item slots.
 */
class UiItemGrid : UiElement() {
    var slotsPerRow: Int? = null
    var slotSize: Int? = null
    var slotIconSize: Int? = null
    var slotSpacing: Int? = null
    var slotBackground: String? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ItemGrid$idPart {")
        append(serializeProperties(indent + 1))
        slotsPerRow?.let { appendLine("${indent(indent + 1)}SlotsPerRow: $it;") }

        val hasStyle = slotSize != null || slotIconSize != null || slotSpacing != null || slotBackground != null
        if (hasStyle) {
            appendLine("${indent(indent + 1)}Style: (")
            slotSize?.let { appendLine("${indent(indent + 2)}SlotSize: $it,") }
            slotIconSize?.let { appendLine("${indent(indent + 2)}SlotIconSize: $it,") }
            slotSpacing?.let { appendLine("${indent(indent + 2)}SlotSpacing: $it,") }
            slotBackground?.let { appendLine("${indent(indent + 2)}SlotBackground: \"$it\",") }
            appendLine("${indent(indent + 1)});")
        }
        appendLine("${indent(indent)}}")
    }
}
