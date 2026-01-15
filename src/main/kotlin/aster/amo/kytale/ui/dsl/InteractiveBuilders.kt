package aster.amo.kytale.ui.dsl

@UiDsl
class InteractiveTextButtonBuilder(private val id: String) {
    private val element = UiTextButton().also { it.id = id }
    var onClick: OnActivate? = null

    var text: String?
        get() = element.text
        set(value) { element.text = value }

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var flexWeight: Int?
        get() = element.flexWeight
        set(value) { element.flexWeight = value }

    var disabled: Boolean?
        get() = element.disabled
        set(value) { element.disabled = value }

    var style: UiButtonStyle?
        get() = element.style
        set(value) { element.style = value }

    /** Dark button style preset */
    fun darkButton(text: String) {
        element.darkButton(text)
    }

    /** Primary/gold button style preset */
    fun primaryButton(text: String) {
        element.primaryButton(text)
    }

    /** Danger/red button style preset */
    fun dangerButton(text: String) {
        element.dangerButton(text)
    }

    /** Success/green button style preset */
    fun successButton(text: String) {
        element.text = text
        element.style = UiButtonStyle().apply {
            defaultBackground = "#2d5a3d"
            hoveredBackground = "#3d7a4d"
            labelStyle = UiLabelStyle().apply {
                fontSize = 14
                textColor = "#ffffff"
                renderBold = true
                horizontalAlignment = HorizontalAlignment.Center
                verticalAlignment = VerticalAlignment.Center
            }
        }
    }

    /** Warning/orange button style preset */
    fun warningButton(text: String) {
        element.text = text
        element.style = UiButtonStyle().apply {
            defaultBackground = "#8b6914"
            hoveredBackground = "#a67c1a"
            labelStyle = UiLabelStyle().apply {
                fontSize = 14
                textColor = "#ffffff"
                renderBold = true
                horizontalAlignment = HorizontalAlignment.Center
                verticalAlignment = VerticalAlignment.Center
            }
        }
    }

    /** Info/blue button style preset */
    fun infoButton(text: String) {
        element.text = text
        element.style = UiButtonStyle().apply {
            defaultBackground = "#2d5a8a"
            hoveredBackground = "#3d7aba"
            labelStyle = UiLabelStyle().apply {
                fontSize = 14
                textColor = "#ffffff"
                renderBold = true
                horizontalAlignment = HorizontalAlignment.Center
                verticalAlignment = VerticalAlignment.Center
            }
        }
    }

    fun build(): InteractiveTextButton {
        return InteractiveTextButton(id, element).apply {
            onClick = this@InteractiveTextButtonBuilder.onClick
        }
    }
}

@UiDsl
class InteractiveButtonBuilder(private val id: String) {
    private val element = UiButton().also { it.id = id }
    var onClick: OnActivate? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var style: UiIconButtonStyle?
        get() = element.style
        set(value) { element.style = value }

    fun build(): InteractiveButton {
        return InteractiveButton(id, element).apply {
            onClick = this@InteractiveButtonBuilder.onClick
        }
    }
}

@UiDsl
class InteractiveSliderBuilder(private val id: String) {
    private val element = UiSlider().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var min: Int?
        get() = element.min
        set(value) { element.min = value }

    var max: Int?
        get() = element.max
        set(value) { element.max = value }

    var step: Int?
        get() = element.step
        set(value) { element.step = value }

    var value: Int?
        get() = element.value
        set(value) { element.value = value }

    fun build(): InteractiveSlider {
        return InteractiveSlider(id, element).apply {
            onChange = this@InteractiveSliderBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveFloatSliderBuilder(private val id: String) {
    private val element = UiFloatSlider().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var min: Double?
        get() = element.min
        set(value) { element.min = value }

    var max: Double?
        get() = element.max
        set(value) { element.max = value }

    var step: Double?
        get() = element.step
        set(value) { element.step = value }

    var value: Double?
        get() = element.value
        set(value) { element.value = value }

    fun build(): InteractiveFloatSlider {
        return InteractiveFloatSlider(id, element).apply {
            onChange = this@InteractiveFloatSliderBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveCheckBoxBuilder(private val id: String) {
    private val element = UiCheckBox().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var value: Boolean?
        get() = element.value
        set(value) { element.value = value }

    fun build(): InteractiveCheckBox {
        return InteractiveCheckBox(id, element).apply {
            onChange = this@InteractiveCheckBoxBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveTextFieldBuilder(private val id: String) {
    private val element = UiTextField().also { it.id = id }
    var onChange: OnValueChange? = null
    var onSubmit: OnActivate? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var placeholderText: String?
        get() = element.placeholderText
        set(value) { element.placeholderText = value }

    var maxLength: Int?
        get() = element.maxLength
        set(value) { element.maxLength = value }

    fun build(): InteractiveTextField {
        return InteractiveTextField(id, element).apply {
            onChange = this@InteractiveTextFieldBuilder.onChange
            onSubmit = this@InteractiveTextFieldBuilder.onSubmit
        }
    }
}

@UiDsl
class InteractiveNumberFieldBuilder(private val id: String) {
    private val element = UiNumberField().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var value: Number?
        get() = element.value
        set(value) { element.value = value }

    var minValue: Number?
        get() = element.minValue
        set(value) { element.minValue = value }

    var maxValue: Number?
        get() = element.maxValue
        set(value) { element.maxValue = value }

    var step: Number?
        get() = element.step
        set(value) { element.step = value }

    fun build(): InteractiveNumberField {
        return InteractiveNumberField(id, element).apply {
            onChange = this@InteractiveNumberFieldBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveDropdownBoxBuilder(private val id: String) {
    private val element = UiDropdownBox().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var noItemsText: String?
        get() = element.noItemsText
        set(value) { element.noItemsText = value }

    fun build(): InteractiveDropdownBox {
        return InteractiveDropdownBox(id, element).apply {
            onChange = this@InteractiveDropdownBoxBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveColorPickerBuilder(private val id: String) {
    private val element = UiColorPicker().also { it.id = id }
    var onChange: OnValueChange? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var format: ColorFormat
        get() = element.format
        set(value) { element.format = value }

    fun build(): InteractiveColorPicker {
        return InteractiveColorPicker(id, element).apply {
            onChange = this@InteractiveColorPickerBuilder.onChange
        }
    }
}

@UiDsl
class InteractiveItemSlotButtonBuilder(private val id: String) {
    private val element = UiItemSlotButton().also { it.id = id }
    var onClick: OnActivate? = null

    var anchor: UiAnchor?
        get() = element.anchor
        set(value) { element.anchor = value }

    var layoutMode: LayoutMode?
        get() = element.layoutMode
        set(value) { element.layoutMode = value }

    fun build(): InteractiveItemSlotButton {
        return InteractiveItemSlotButton(id, element).apply {
            onClick = this@InteractiveItemSlotButtonBuilder.onClick
        }
    }
}
