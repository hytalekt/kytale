package aster.amo.kytale.ui.dsl

/**
 * Root UI page definition.
 */
@UiDsl
class UiPage(val name: String) {
    var width: Int = 500
    var height: Int = 400
    var title: String = ""
    private var content: UiGroup? = null
    var includeBackButton: Boolean = true

    fun content(block: UiGroup.() -> Unit) {
        content = UiGroup().apply {
            layoutMode = LayoutMode.Top
            block()
        }
    }

    fun serialize(): String = buildString {
        appendLine("\$C = \"../../Common.ui\";")
        appendLine()
        appendLine("\$C.@PageOverlay {")
        appendLine("  \$C.@Container {")
        appendLine("    Anchor: (Width: $width, Height: $height);")
        appendLine()
        appendLine("    #Title {")
        appendLine("      Group {")
        appendLine("        \$C.@Title {")
        appendLine("          @Text = \"$title\";")
        appendLine("        }")
        appendLine("      }")
        appendLine("    }")
        appendLine()
        appendLine("    #Content {")
        content?.let {
            it.layoutMode?.let { mode -> appendLine("      LayoutMode: ${mode.value};") }
            it.children.forEach { child ->
                append(child.serialize(3))
            }
        }
        appendLine("    }")
        appendLine("  }")
        appendLine("}")
        if (includeBackButton) {
            appendLine()
            appendLine("\$C.@BackButton {}")
        }
    }
}

/**
 * Create a UI page definition.
 */
fun uiPage(name: String, block: UiPage.() -> Unit): UiPage {
    return UiPage(name).apply(block)
}

@UiDsl
fun UiGroup.group(id: String? = null, block: UiGroup.() -> Unit) {
    children.add(UiGroup().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.label(id: String? = null, block: UiLabel.() -> Unit) {
    children.add(UiLabel().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.textButton(id: String, block: UiTextButton.() -> Unit) {
    children.add(UiTextButton().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.button(id: String? = null, block: UiButton.() -> Unit) {
    children.add(UiButton().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.checkBox(id: String? = null, block: UiCheckBox.() -> Unit) {
    children.add(UiCheckBox().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.slider(id: String? = null, block: UiSlider.() -> Unit) {
    children.add(UiSlider().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.floatSlider(id: String? = null, block: UiFloatSlider.() -> Unit) {
    children.add(UiFloatSlider().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.textField(id: String? = null, block: UiTextField.() -> Unit) {
    children.add(UiTextField().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.numberField(id: String? = null, block: UiNumberField.() -> Unit) {
    children.add(UiNumberField().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.multilineTextField(id: String? = null, block: UiMultilineTextField.() -> Unit) {
    children.add(UiMultilineTextField().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.dropdownBox(id: String? = null, block: UiDropdownBox.() -> Unit) {
    children.add(UiDropdownBox().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.colorPicker(id: String? = null, block: UiColorPicker.() -> Unit) {
    children.add(UiColorPicker().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.colorPickerDropdownBox(id: String? = null, block: UiColorPickerDropdownBox.() -> Unit) {
    children.add(UiColorPickerDropdownBox().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.sprite(id: String? = null, block: UiSprite.() -> Unit) {
    children.add(UiSprite().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.assetImage(id: String? = null, block: UiAssetImage.() -> Unit) {
    children.add(UiAssetImage().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.progressBar(id: String? = null, block: UiProgressBar.() -> Unit) {
    children.add(UiProgressBar().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.timerLabel(id: String? = null, block: UiTimerLabel.() -> Unit) {
    children.add(UiTimerLabel().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.itemIcon(id: String? = null, block: UiItemIcon.() -> Unit) {
    children.add(UiItemIcon().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.itemSlot(id: String? = null, block: UiItemSlot.() -> Unit) {
    children.add(UiItemSlot().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.itemSlotButton(id: String? = null, block: UiItemSlotButton.() -> Unit) {
    children.add(UiItemSlotButton().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.itemGrid(id: String? = null, block: UiItemGrid.() -> Unit) {
    children.add(UiItemGrid().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.compactTextField(id: String? = null, block: UiCompactTextField.() -> Unit) {
    children.add(UiCompactTextField().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.separator(id: String? = null, block: UiSeparator.() -> Unit) {
    children.add(UiSeparator().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.toggleButton(id: String? = null, block: UiToggleButton.() -> Unit) {
    children.add(UiToggleButton().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.radioButton(id: String? = null, block: UiRadioButton.() -> Unit) {
    children.add(UiRadioButton().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.tab(id: String? = null, block: UiTab.() -> Unit) {
    children.add(UiTab().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiGroup.tabPanel(id: String? = null, block: UiTabPanel.() -> Unit) {
    children.add(UiTabPanel().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiTabPanel.tab(id: String? = null, block: UiTab.() -> Unit) {
    tabs.add(UiTab().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiTabPanel.panel(id: String? = null, block: UiGroup.() -> Unit) {
    panels.add(UiGroup().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiElement.anchor(block: UiAnchor.() -> Unit) {
    anchor = UiAnchor().apply(block)
}

@UiDsl
fun UiElement.padding(block: UiPadding.() -> Unit) {
    padding = UiPadding().apply(block)
}

@UiDsl
fun UiLabel.style(block: UiLabelStyle.() -> Unit) {
    style = UiLabelStyle().apply(block)
}

@UiDsl
fun UiTextButton.buttonStyle(block: UiButtonStyle.() -> Unit) {
    style = UiButtonStyle().apply(block)
}

@UiDsl
fun UiButtonStyle.labelStyle(block: UiLabelStyle.() -> Unit) {
    labelStyle = UiLabelStyle().apply(block)
}

@UiDsl
fun UiButtonStyle.hoveredLabelStyle(block: UiLabelStyle.() -> Unit) {
    hoveredLabelStyle = UiLabelStyle().apply(block)
}

@UiDsl
fun UiButtonStyle.pressedLabelStyle(block: UiLabelStyle.() -> Unit) {
    pressedLabelStyle = UiLabelStyle().apply(block)
}

@UiDsl
fun UiButtonStyle.disabledLabelStyle(block: UiLabelStyle.() -> Unit) {
    disabledLabelStyle = UiLabelStyle().apply(block)
}

@UiDsl
fun UiToggleButton.onStyle(block: UiButtonStyle.() -> Unit) {
    onStyle = UiButtonStyle().apply(block)
}

@UiDsl
fun UiToggleButton.offStyle(block: UiButtonStyle.() -> Unit) {
    offStyle = UiButtonStyle().apply(block)
}

@UiDsl
fun UiButton.iconButtonStyle(block: UiIconButtonStyle.() -> Unit) {
    style = UiIconButtonStyle().apply(block)
}

@UiDsl
fun UiItemSlotButton.iconButtonStyle(block: UiIconButtonStyle.() -> Unit) {
    style = UiIconButtonStyle().apply(block)
}

@UiDsl
fun UiTimerLabel.style(block: UiLabelStyle.() -> Unit) {
    style = UiLabelStyle().apply(block)
}

@UiDsl
fun UiItemSlotButton.itemSlot(id: String? = null, block: UiItemSlot.() -> Unit) {
    children.add(UiItemSlot().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiItemSlotButton.itemIcon(id: String? = null, block: UiItemIcon.() -> Unit) {
    children.add(UiItemIcon().apply {
        this.id = id
        block()
    })
}

@UiDsl
fun UiItemSlotButton.label(id: String? = null, block: UiLabel.() -> Unit) {
    children.add(UiLabel().apply {
        this.id = id
        block()
    })
}

/** Set anchor to fill parent */
fun UiAnchor.fill() {
    left = 0
    right = 0
    top = 0
    bottom = 0
}

/** Set size */
fun UiAnchor.size(width: Int, height: Int) {
    this.width = width
    this.height = height
}

/** Common button style preset - dark button */
fun UiTextButton.darkButton(text: String) {
    this.text = text
    buttonStyle {
        defaultBackground = "#1a2636"
        hoveredBackground = "#243448"
        labelStyle {
            fontSize = 14
            textColor = "#bfcdd5"
            renderBold = true
            horizontalAlignment = HorizontalAlignment.Center
            verticalAlignment = VerticalAlignment.Center
        }
        hoveredLabelStyle {
            fontSize = 14
            textColor = "#ffffff"
            renderBold = true
            horizontalAlignment = HorizontalAlignment.Center
            verticalAlignment = VerticalAlignment.Center
        }
    }
}

/** Common button style preset - gold/primary button */
fun UiTextButton.primaryButton(text: String) {
    this.text = text
    buttonStyle {
        defaultBackground = "#c4a23a"
        hoveredBackground = "#d4b24a"
        labelStyle {
            fontSize = 14
            textColor = "#121a26"
            renderBold = true
            horizontalAlignment = HorizontalAlignment.Center
            verticalAlignment = VerticalAlignment.Center
        }
    }
}

/** Common button style preset - danger/red button */
fun UiTextButton.dangerButton(text: String) {
    this.text = text
    buttonStyle {
        defaultBackground = "#c43a3a"
        hoveredBackground = "#d44a4a"
        labelStyle {
            fontSize = 14
            textColor = "#ffffff"
            renderBold = true
            horizontalAlignment = HorizontalAlignment.Center
            verticalAlignment = VerticalAlignment.Center
        }
    }
}
