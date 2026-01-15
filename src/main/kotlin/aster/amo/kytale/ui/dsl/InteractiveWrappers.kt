package aster.amo.kytale.ui.dsl

/**
 * Interactive text button wrapper.
 */
class InteractiveTextButton(
    override val elementId: String,
    override val element: UiTextButton
) : InteractiveWrapper() {
    var onClick: OnActivate? = null
}

/**
 * Interactive button (icon-only) wrapper.
 */
class InteractiveButton(
    override val elementId: String,
    override val element: UiButton
) : InteractiveWrapper() {
    var onClick: OnActivate? = null
}

/**
 * Interactive slider wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveSlider(
    override val elementId: String,
    override val element: UiSlider
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive float slider wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveFloatSlider(
    override val elementId: String,
    override val element: UiFloatSlider
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive checkbox wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveCheckBox(
    override val elementId: String,
    override val element: UiCheckBox
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive text field wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 * Use onSubmit for enter key press.
 */
class InteractiveTextField(
    override val elementId: String,
    override val element: UiTextField
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
    var onSubmit: OnActivate? = null
}

/**
 * Interactive number field wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveNumberField(
    override val elementId: String,
    override val element: UiNumberField
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive dropdown box wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveDropdownBox(
    override val elementId: String,
    override val element: UiDropdownBox
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive color picker wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveColorPicker(
    override val elementId: String,
    override val element: UiColorPicker
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive color picker dropdown box wrapper.
 * The onChange handler receives the new value via ValueEventContext.
 */
class InteractiveColorPickerDropdownBox(
    override val elementId: String,
    override val element: UiColorPickerDropdownBox
) : InteractiveWrapper() {
    var onChange: OnValueChange? = null
}

/**
 * Interactive item slot button wrapper.
 */
class InteractiveItemSlotButton(
    override val elementId: String,
    override val element: UiItemSlotButton
) : InteractiveWrapper() {
    var onClick: OnActivate? = null
}
