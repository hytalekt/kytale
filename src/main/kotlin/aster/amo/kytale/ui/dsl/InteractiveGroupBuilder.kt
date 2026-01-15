package aster.amo.kytale.ui.dsl

/**
 * Builder context for interactive groups that tracks both UI elements and event handlers.
 */
@UiDsl
class InteractiveGroupBuilder {
    internal val group = UiGroup()
    internal val wrappers = mutableListOf<InteractiveWrapper>()

    var id: String?
        get() = group.id
        set(value) { group.id = value }

    var anchor: UiAnchor?
        get() = group.anchor
        set(value) { group.anchor = value }

    var padding: UiPadding?
        get() = group.padding
        set(value) { group.padding = value }

    var layoutMode: LayoutMode?
        get() = group.layoutMode
        set(value) { group.layoutMode = value }

    var flexWeight: Int?
        get() = group.flexWeight
        set(value) { group.flexWeight = value }

    var background: String?
        get() = group.background
        set(value) { group.background = value }

    /**
     * Add an interactive text button.
     */
    fun textButton(id: String, block: InteractiveTextButtonBuilder.() -> Unit): InteractiveTextButton {
        val builder = InteractiveTextButtonBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive button (icon-only).
     */
    fun button(id: String, block: InteractiveButtonBuilder.() -> Unit): InteractiveButton {
        val builder = InteractiveButtonBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive slider.
     */
    fun slider(id: String, block: InteractiveSliderBuilder.() -> Unit): InteractiveSlider {
        val builder = InteractiveSliderBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive float slider.
     */
    fun floatSlider(id: String, block: InteractiveFloatSliderBuilder.() -> Unit): InteractiveFloatSlider {
        val builder = InteractiveFloatSliderBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive checkbox.
     */
    fun checkBox(id: String, block: InteractiveCheckBoxBuilder.() -> Unit): InteractiveCheckBox {
        val builder = InteractiveCheckBoxBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive text field.
     */
    fun textField(id: String, block: InteractiveTextFieldBuilder.() -> Unit): InteractiveTextField {
        val builder = InteractiveTextFieldBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive number field.
     */
    fun numberField(id: String, block: InteractiveNumberFieldBuilder.() -> Unit): InteractiveNumberField {
        val builder = InteractiveNumberFieldBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive dropdown box.
     */
    fun dropdownBox(id: String, block: InteractiveDropdownBoxBuilder.() -> Unit): InteractiveDropdownBox {
        val builder = InteractiveDropdownBoxBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive color picker.
     */
    fun colorPicker(id: String, block: InteractiveColorPickerBuilder.() -> Unit): InteractiveColorPicker {
        val builder = InteractiveColorPickerBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add an interactive item slot button.
     */
    fun itemSlotButton(id: String, block: InteractiveItemSlotButtonBuilder.() -> Unit): InteractiveItemSlotButton {
        val builder = InteractiveItemSlotButtonBuilder(id)
        builder.block()
        val wrapper = builder.build()
        group.children.add(wrapper.element)
        wrappers.add(wrapper)
        return wrapper
    }

    /**
     * Add a nested group.
     */
    fun group(id: String? = null, block: InteractiveGroupBuilder.() -> Unit): InteractiveGroupBuilder {
        val builder = InteractiveGroupBuilder()
        builder.id = id
        builder.block()
        group.children.add(builder.group)
        wrappers.addAll(builder.wrappers)
        return builder
    }

    /**
     * Add a label.
     */
    fun label(id: String? = null, block: UiLabel.() -> Unit): UiLabel {
        val label = UiLabel()
        label.id = id
        label.block()
        group.children.add(label)
        return label
    }

    /**
     * Add a sprite.
     */
    fun sprite(id: String? = null, block: UiSprite.() -> Unit): UiSprite {
        val sprite = UiSprite()
        sprite.id = id
        sprite.block()
        group.children.add(sprite)
        return sprite
    }

    /**
     * Add an asset image.
     */
    fun assetImage(id: String? = null, block: UiAssetImage.() -> Unit): UiAssetImage {
        val image = UiAssetImage()
        image.id = id
        image.block()
        group.children.add(image)
        return image
    }

    /**
     * Add a progress bar.
     */
    fun progressBar(id: String? = null, block: UiProgressBar.() -> Unit): UiProgressBar {
        val bar = UiProgressBar()
        bar.id = id
        bar.block()
        group.children.add(bar)
        return bar
    }

    /**
     * Add a timer label.
     */
    fun timerLabel(id: String? = null, block: UiTimerLabel.() -> Unit): UiTimerLabel {
        val timer = UiTimerLabel()
        timer.id = id
        timer.block()
        group.children.add(timer)
        return timer
    }

    /**
     * Add an item icon.
     */
    fun itemIcon(id: String? = null, block: UiItemIcon.() -> Unit): UiItemIcon {
        val icon = UiItemIcon()
        icon.id = id
        icon.block()
        group.children.add(icon)
        return icon
    }

    /**
     * Add an item slot.
     */
    fun itemSlot(id: String? = null, block: UiItemSlot.() -> Unit): UiItemSlot {
        val slot = UiItemSlot()
        slot.id = id
        slot.block()
        group.children.add(slot)
        return slot
    }

    /**
     * Add an item grid.
     */
    fun itemGrid(id: String? = null, block: UiItemGrid.() -> Unit): UiItemGrid {
        val grid = UiItemGrid()
        grid.id = id
        grid.block()
        group.children.add(grid)
        return grid
    }

    /**
     * Add a multiline text field.
     */
    fun multilineTextField(id: String? = null, block: UiMultilineTextField.() -> Unit): UiMultilineTextField {
        val field = UiMultilineTextField()
        field.id = id
        field.block()
        group.children.add(field)
        return field
    }

    /**
     * Add a compact text field.
     */
    fun compactTextField(id: String? = null, block: UiCompactTextField.() -> Unit): UiCompactTextField {
        val field = UiCompactTextField()
        field.id = id
        field.block()
        group.children.add(field)
        return field
    }
}
