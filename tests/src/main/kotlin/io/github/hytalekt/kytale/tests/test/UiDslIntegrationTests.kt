package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.ui.dsl.*

/**
 * Integration tests for UI DSL module.
 *
 * Tests UI element construction, serialization, and DSL builders.
 */

fun IntegrationTestRunner.uiAnchorTests() = suite("UI DSL - Anchor") {

    test("UiAnchor serializes width and height") {
        val anchor = UiAnchor(width = 100, height = 50)
        val result = anchor.serialize()
        assertContains(result, "Width: 100")
        assertContains(result, "Height: 50")
    }

    test("UiAnchor serializes position offsets") {
        val anchor = UiAnchor(left = 10, right = 20, top = 5, bottom = 15)
        val result = anchor.serialize()
        assertContains(result, "Left: 10")
        assertContains(result, "Right: 20")
        assertContains(result, "Top: 5")
        assertContains(result, "Bottom: 15")
    }

    test("UiAnchor serializes min/max constraints") {
        val anchor = UiAnchor(minWidth = 50, maxWidth = 200, minHeight = 30, maxHeight = 100)
        val result = anchor.serialize()
        assertContains(result, "MinWidth: 50")
        assertContains(result, "MaxWidth: 200")
        assertContains(result, "MinHeight: 30")
        assertContains(result, "MaxHeight: 100")
    }

    test("UiAnchor full flag serializes") {
        val anchor = UiAnchor(full = true)
        val result = anchor.serialize()
        assertContains(result, "Full: true")
    }

    test("UiAnchor horizontal flag serializes") {
        val anchor = UiAnchor(horizontal = true)
        val result = anchor.serialize()
        assertContains(result, "Horizontal: true")
    }

    test("UiAnchor fill helper sets all edges") {
        val anchor = UiAnchor()
        anchor.fill()
        assertEquals(0, anchor.left)
        assertEquals(0, anchor.right)
        assertEquals(0, anchor.top)
        assertEquals(0, anchor.bottom)
    }

    test("UiAnchor size helper sets dimensions") {
        val anchor = UiAnchor()
        anchor.size(200, 150)
        assertEquals(200, anchor.width)
        assertEquals(150, anchor.height)
    }
}

fun IntegrationTestRunner.uiPaddingTests() = suite("UI DSL - Padding") {

    test("UiPadding serializes horizontal") {
        val padding = UiPadding(horizontal = 10)
        val result = padding.serialize()
        assertContains(result, "Horizontal: 10")
    }

    test("UiPadding serializes vertical") {
        val padding = UiPadding(vertical = 15)
        val result = padding.serialize()
        assertContains(result, "Vertical: 15")
    }

    test("UiPadding serializes individual sides") {
        val padding = UiPadding(left = 5, right = 10, top = 15, bottom = 20)
        val result = padding.serialize()
        assertContains(result, "Left: 5")
        assertContains(result, "Right: 10")
        assertContains(result, "Top: 15")
        assertContains(result, "Bottom: 20")
    }
}

fun IntegrationTestRunner.uiStylesTests() = suite("UI DSL - Styles") {

    test("UiLabelStyle serializes fontSize") {
        val style = UiLabelStyle(fontSize = 14)
        val result = style.serialize()
        assertContains(result, "FontSize: 14")
    }

    test("UiLabelStyle serializes textColor") {
        val style = UiLabelStyle(textColor = "#ffffff")
        val result = style.serialize()
        assertContains(result, "TextColor: #ffffff")
    }

    test("UiLabelStyle serializes renderBold") {
        val style = UiLabelStyle(renderBold = true)
        val result = style.serialize()
        assertContains(result, "RenderBold: true")
    }

    test("UiLabelStyle serializes alignment") {
        val style = UiLabelStyle(
            horizontalAlignment = HorizontalAlignment.Center,
            verticalAlignment = VerticalAlignment.Center
        )
        val result = style.serialize()
        assertContains(result, "HorizontalAlignment: Center")
        assertContains(result, "VerticalAlignment: Center")
    }

    test("UiLabelStyle serializes wrap and lineSpacing") {
        val style = UiLabelStyle(wrap = true, lineSpacing = 5)
        val result = style.serialize()
        assertContains(result, "Wrap: true")
        assertContains(result, "LineSpacing: 5")
    }

    test("UiButtonStyle serializes default state") {
        val style = UiButtonStyle().apply {
            defaultBackground = "#1a2636"
            labelStyle = UiLabelStyle(fontSize = 14)
        }
        val result = style.serialize(0)
        assertContains(result, "Default:")
        assertContains(result, "Background: #1a2636")
    }

    test("UiButtonStyle serializes hovered state") {
        val style = UiButtonStyle().apply {
            hoveredBackground = "#243448"
        }
        val result = style.serialize(0)
        assertContains(result, "Hovered:")
    }

    test("UiSliderStyle serializes track and thumb") {
        val style = UiSliderStyle().apply {
            trackBackground = "#333333"
            fillBackground = "#c4a23a"
            thumbBackground = "#ffffff"
            thumbSize = 16
        }
        val result = style.serialize()
        assertContains(result, "TrackBackground: #333333")
        assertContains(result, "FillBackground: #c4a23a")
        assertContains(result, "ThumbBackground: #ffffff")
        assertContains(result, "ThumbSize: 16")
    }
}

fun IntegrationTestRunner.uiElementsTests() = suite("UI DSL - Elements") {

    test("UiLabel serializes with id") {
        val label = UiLabel().apply {
            id = "myLabel"
            text = "Hello World"
        }
        val result = label.serialize(0)
        assertContains(result, "Label #myLabel")
        assertContains(result, "Text: \"Hello World\"")
    }

    test("UiLabel serializes with style") {
        val label = UiLabel().apply {
            text = "Styled"
            style = UiLabelStyle(fontSize = 18, textColor = "#ff0000")
        }
        val result = label.serialize(0)
        assertContains(result, "Style:")
        assertContains(result, "FontSize: 18")
    }

    test("UiGroup serializes with layoutMode") {
        val group = UiGroup().apply {
            layoutMode = LayoutMode.Top
        }
        val result = group.serialize(0)
        assertContains(result, "Group")
        assertContains(result, "LayoutMode: Top")
    }

    test("UiGroup serializes with id") {
        val group = UiGroup().apply {
            id = "myGroup"
        }
        val result = group.serialize(0)
        assertContains(result, "Group #myGroup")
    }

    test("UiGroup serializes children") {
        val group = UiGroup().apply {
            children.add(UiLabel().apply { text = "Child1" })
            children.add(UiLabel().apply { text = "Child2" })
        }
        val result = group.serialize(0)
        assertContains(result, "Child1")
        assertContains(result, "Child2")
    }

    test("UiSlider serializes min/max/step") {
        val slider = UiSlider().apply {
            id = "volume"
            min = 0
            max = 100
            step = 5
            value = 50
        }
        val result = slider.serialize(0)
        assertContains(result, "Slider #volume")
        assertContains(result, "Min: 0")
        assertContains(result, "Max: 100")
        assertContains(result, "Step: 5")
        assertContains(result, "Value: 50")
    }

    test("UiFloatSlider serializes float values") {
        val slider = UiFloatSlider().apply {
            min = 0.0
            max = 1.0
            step = 0.1
            value = 0.5
        }
        val result = slider.serialize(0)
        assertContains(result, "FloatSlider")
        assertContains(result, "Min: 0.0")
        assertContains(result, "Max: 1.0")
    }
}

fun IntegrationTestRunner.uiPageTests() = suite("UI DSL - Page") {

    test("uiPage creates page with name") {
        val page = uiPage("TestPage") {
            title = "Test Title"
        }
        assertEquals("TestPage", page.name)
        assertEquals("Test Title", page.title)
    }

    test("uiPage serializes with dimensions") {
        val page = uiPage("Test") {
            width = 600
            height = 500
            title = "My Page"
        }
        val result = page.serialize()
        assertContains(result, "Width: 600")
        assertContains(result, "Height: 500")
    }

    test("uiPage serializes title") {
        val page = uiPage("Test") {
            title = "Settings"
        }
        val result = page.serialize()
        assertContains(result, "@Text = \"Settings\"")
    }

    test("uiPage includes back button by default") {
        val page = uiPage("Test") {}
        val result = page.serialize()
        assertContains(result, "@BackButton")
    }

    test("uiPage can exclude back button") {
        val page = uiPage("Test") {
            includeBackButton = false
        }
        val result = page.serialize()
        assertFalse(result.contains("@BackButton"))
    }

    test("uiPage content adds children") {
        val page = uiPage("Test") {
            content {
                children.add(UiLabel().apply { text = "Content" })
            }
        }
        val result = page.serialize()
        assertContains(result, "#Content")
    }
}

fun IntegrationTestRunner.uiBuilderDslTests() = suite("UI DSL - Builders") {

    test("group builder creates nested group") {
        val parent = UiGroup()
        parent.group("inner") {
            layoutMode = LayoutMode.Left
        }
        assertEquals(1, parent.children.size)
        val child = parent.children[0] as UiGroup
        assertEquals("inner", child.id)
        assertEquals(LayoutMode.Left, child.layoutMode)
    }

    test("label builder creates label") {
        val group = UiGroup()
        group.label("title") {
            text = "Welcome"
        }
        assertEquals(1, group.children.size)
        val label = group.children[0] as UiLabel
        assertEquals("title", label.id)
        assertEquals("Welcome", label.text)
    }

    test("slider builder creates slider") {
        val group = UiGroup()
        group.slider("volume") {
            min = 0
            max = 100
        }
        val slider = group.children[0] as UiSlider
        assertEquals("volume", slider.id)
        assertEquals(0, slider.min)
        assertEquals(100, slider.max)
    }

    test("textButton builder creates button") {
        val group = UiGroup()
        group.textButton("submit") {
            text = "Submit"
        }
        val button = group.children[0] as UiTextButton
        assertEquals("submit", button.id)
        assertEquals("Submit", button.text)
    }

    test("checkBox builder creates checkbox") {
        val group = UiGroup()
        group.checkBox("enabled") {
            value = true
        }
        val checkbox = group.children[0] as UiCheckBox
        assertEquals("enabled", checkbox.id)
    }

    test("anchor DSL extension works") {
        val label = UiLabel()
        label.anchor {
            width = 100
            height = 50
        }
        assertNotNull(label.anchor)
        assertEquals(100, label.anchor!!.width)
        assertEquals(50, label.anchor!!.height)
    }

    test("padding DSL extension works") {
        val label = UiLabel()
        label.padding {
            horizontal = 10
            vertical = 5
        }
        assertNotNull(label.padding)
        assertEquals(10, label.padding!!.horizontal)
        assertEquals(5, label.padding!!.vertical)
    }

    test("darkButton preset applies style") {
        val button = UiTextButton()
        button.darkButton("Click Me")
        assertEquals("Click Me", button.text)
        assertNotNull(button.style)
        assertEquals("#1a2636", button.style!!.defaultBackground)
    }

    test("primaryButton preset applies gold style") {
        val button = UiTextButton()
        button.primaryButton("Submit")
        assertEquals("Submit", button.text)
        assertEquals("#c4a23a", button.style!!.defaultBackground)
    }

    test("dangerButton preset applies red style") {
        val button = UiTextButton()
        button.dangerButton("Delete")
        assertEquals("Delete", button.text)
        assertEquals("#c43a3a", button.style!!.defaultBackground)
    }
}

fun IntegrationTestRunner.uiEnumsTests() = suite("UI DSL - Enums") {

    test("LayoutMode values are correct") {
        assertEquals("Top", LayoutMode.Top.value)
        assertEquals("Left", LayoutMode.Left.value)
        assertEquals("Center", LayoutMode.Center.value)
        assertEquals("TopScrolling", LayoutMode.TopScrolling.value)
    }

    test("HorizontalAlignment values are correct") {
        assertEquals("Left", HorizontalAlignment.Left.value)
        assertEquals("Center", HorizontalAlignment.Center.value)
        assertEquals("Right", HorizontalAlignment.Right.value)
    }

    test("VerticalAlignment values are correct") {
        assertEquals("Top", VerticalAlignment.Top.value)
        assertEquals("Center", VerticalAlignment.Center.value)
        assertEquals("Bottom", VerticalAlignment.Bottom.value)
    }

    test("ColorFormat values are correct") {
        assertEquals("Rgb", ColorFormat.Rgb.value)
        assertEquals("Rgba", ColorFormat.Rgba.value)
        assertEquals("Hsv", ColorFormat.Hsv.value)
        assertEquals("Hsva", ColorFormat.Hsva.value)
    }
}

fun IntegrationTestRunner.uiInteractiveTests() = suite("UI DSL - Interactive") {

    test("InteractiveTextButtonBuilder creates button") {
        val builder = InteractiveTextButtonBuilder("btn")
        builder.text = "Click"
        val wrapper = builder.build()
        assertEquals("btn", wrapper.elementId)
    }

    test("InteractiveTextButtonBuilder darkButton preset") {
        val builder = InteractiveTextButtonBuilder("btn")
        builder.darkButton("Dark")
        val wrapper = builder.build()
        val element = wrapper.element as UiTextButton
        assertEquals("Dark", element.text)
    }

    test("InteractiveTextButtonBuilder successButton preset") {
        val builder = InteractiveTextButtonBuilder("btn")
        builder.successButton("Success")
        val wrapper = builder.build()
        val element = wrapper.element as UiTextButton
        assertEquals("Success", element.text)
    }

    test("InteractiveSliderBuilder creates slider") {
        val builder = InteractiveSliderBuilder("slider")
        builder.min = 0
        builder.max = 100
        builder.value = 50
        val wrapper = builder.build()
        val element = wrapper.element as UiSlider
        assertEquals(0, element.min)
        assertEquals(100, element.max)
        assertEquals(50, element.value)
    }

    test("InteractiveCheckBoxBuilder creates checkbox") {
        val builder = InteractiveCheckBoxBuilder("check")
        builder.value = true
        val wrapper = builder.build()
        assertEquals("check", wrapper.elementId)
    }

    test("InteractiveTextFieldBuilder creates text field") {
        val builder = InteractiveTextFieldBuilder("input")
        builder.placeholderText = "Enter text..."
        builder.maxLength = 100
        val wrapper = builder.build()
        assertEquals("input", wrapper.elementId)
    }

    test("InteractiveNumberFieldBuilder creates number field") {
        val builder = InteractiveNumberFieldBuilder("num")
        builder.value = 42
        builder.minValue = 0
        builder.maxValue = 100
        val wrapper = builder.build()
        assertEquals("num", wrapper.elementId)
    }

    test("InteractiveDropdownBoxBuilder creates dropdown") {
        val builder = InteractiveDropdownBoxBuilder("dropdown")
        builder.noItemsText = "No items"
        val wrapper = builder.build()
        assertEquals("dropdown", wrapper.elementId)
    }

    test("InteractiveColorPickerBuilder creates color picker") {
        val builder = InteractiveColorPickerBuilder("color")
        builder.format = ColorFormat.Rgba
        val wrapper = builder.build()
        val element = wrapper.element as UiColorPicker
        assertEquals(ColorFormat.Rgba, element.format)
    }
}

fun IntegrationTestRunner.uiMoreElementsTests() = suite("UI DSL - More Elements") {

    // UiTextButton tests
    test("UiTextButton serializes with text") {
        val button = UiTextButton().apply {
            id = "submit"
            text = "Submit"
        }
        val result = button.serialize(0)
        assertContains(result, "@TextButton #submit")
        assertContains(result, "Text: \"Submit\"")
    }

    test("UiTextButton serializes disabled state") {
        val button = UiTextButton().apply {
            text = "Disabled"
            disabled = true
        }
        val result = button.serialize(0)
        assertContains(result, "Disabled: true")
    }

    // UiButton tests
    test("UiButton serializes with id") {
        val button = UiButton().apply {
            id = "iconBtn"
        }
        val result = button.serialize(0)
        assertContains(result, "Button #iconBtn")
    }

    // UiTextField tests
    test("UiTextField serializes placeholder") {
        val field = UiTextField().apply {
            id = "search"
            placeholderText = "Search..."
        }
        val result = field.serialize(0)
        assertContains(result, "@TextField #search")
        assertContains(result, "PlaceholderText: \"Search...\"")
    }

    test("UiTextField serializes maxLength") {
        val field = UiTextField().apply {
            maxLength = 50
        }
        val result = field.serialize(0)
        assertContains(result, "MaxLength: 50")
    }

    // UiMultilineTextField tests
    test("UiMultilineTextField serializes with autoGrow") {
        val field = UiMultilineTextField().apply {
            id = "bio"
            placeholderText = "Enter bio..."
            autoGrow = true
        }
        val result = field.serialize(0)
        assertContains(result, "MultilineTextField #bio")
        assertContains(result, "AutoGrow: true")
    }

    // UiCompactTextField tests
    test("UiCompactTextField serializes widths") {
        val field = UiCompactTextField().apply {
            collapsedWidth = 40
            expandedWidth = 200
            placeholderText = "Search"
        }
        val result = field.serialize(0)
        assertContains(result, "CompactTextField")
        assertContains(result, "CollapsedWidth: 40")
        assertContains(result, "ExpandedWidth: 200")
    }

    // UiCheckBox tests
    test("UiCheckBox serializes value") {
        val checkbox = UiCheckBox().apply {
            id = "enabled"
            value = true
        }
        val result = checkbox.serialize(0)
        assertContains(result, "@CheckBox #enabled")
        assertContains(result, "Value: true")
    }

    // UiDropdownBox tests
    test("UiDropdownBox serializes noItemsText") {
        val dropdown = UiDropdownBox().apply {
            id = "options"
            noItemsText = "No options available"
        }
        val result = dropdown.serialize(0)
        assertContains(result, "@DropdownBox #options")
        assertContains(result, "NoItemsText: \"No options available\"")
    }

    // UiNumberField tests
    test("UiNumberField serializes value") {
        val field = UiNumberField().apply {
            id = "quantity"
            value = 10
        }
        val result = field.serialize(0)
        assertContains(result, "@NumberField #quantity")
        assertContains(result, "Value: 10")
    }

    test("UiNumberField serializes format block") {
        val field = UiNumberField().apply {
            minValue = 0
            maxValue = 100
            step = 5
            maxDecimalPlaces = 2
        }
        val result = field.serialize(0)
        assertContains(result, "Format:")
        assertContains(result, "MinValue: 0")
        assertContains(result, "MaxValue: 100")
        assertContains(result, "Step: 5")
        assertContains(result, "MaxDecimalPlaces: 2")
    }

    // UiColorPicker tests
    test("UiColorPicker serializes format") {
        val picker = UiColorPicker().apply {
            id = "bgColor"
            format = ColorFormat.Rgba
        }
        val result = picker.serialize(0)
        assertContains(result, "ColorPicker #bgColor")
        assertContains(result, "Format: Rgba")
    }

    // UiColorPickerDropdownBox tests
    test("UiColorPickerDropdownBox serializes options") {
        val picker = UiColorPickerDropdownBox().apply {
            format = ColorFormat.Hsva
            displayTextField = true
        }
        val result = picker.serialize(0)
        assertContains(result, "ColorPickerDropdownBox")
        assertContains(result, "Format: Hsva")
        assertContains(result, "DisplayTextField: true")
    }
}

fun IntegrationTestRunner.uiSpriteAndImageTests() = suite("UI DSL - Sprites & Images") {

    test("UiSprite serializes texturePath") {
        val sprite = UiSprite().apply {
            id = "icon"
            texturePath = "textures/icons/sword.png"
        }
        val result = sprite.serialize(0)
        assertContains(result, "Sprite #icon")
        assertContains(result, "TexturePath: \"textures/icons/sword.png\"")
    }

    test("UiSprite serializes frame properties") {
        val sprite = UiSprite().apply {
            frameWidth = 32
            frameHeight = 32
            framesPerRow = 8
            frameCount = 16
            framesPerSecond = 10
        }
        val result = sprite.serialize(0)
        assertContains(result, "Frame:")
        assertContains(result, "Width: 32")
        assertContains(result, "Height: 32")
        assertContains(result, "PerRow: 8")
        assertContains(result, "Count: 16")
        assertContains(result, "FramesPerSecond: 10")
    }

    test("UiAssetImage serializes with id") {
        val image = UiAssetImage().apply {
            id = "preview"
        }
        val result = image.serialize(0)
        assertContains(result, "AssetImage #preview")
    }

    test("UiProgressBar serializes textures") {
        val bar = UiProgressBar().apply {
            id = "health"
            barTexturePath = "textures/ui/health_bar.png"
            effectTexturePath = "textures/ui/health_effect.png"
        }
        val result = bar.serialize(0)
        assertContains(result, "ProgressBar #health")
        assertContains(result, "BarTexturePath: \"textures/ui/health_bar.png\"")
        assertContains(result, "EffectTexturePath: \"textures/ui/health_effect.png\"")
    }

    test("UiProgressBar serializes effect dimensions") {
        val bar = UiProgressBar().apply {
            effectWidth = 64
            effectHeight = 16
            effectOffset = 4
        }
        val result = bar.serialize(0)
        assertContains(result, "EffectWidth: 64")
        assertContains(result, "EffectHeight: 16")
        assertContains(result, "EffectOffset: 4")
    }

    test("UiTimerLabel serializes seconds") {
        val timer = UiTimerLabel().apply {
            id = "countdown"
            seconds = 300
        }
        val result = timer.serialize(0)
        assertContains(result, "TimerLabel #countdown")
        assertContains(result, "Seconds: 300")
    }

    test("UiTimerLabel serializes with style") {
        val timer = UiTimerLabel().apply {
            style = UiLabelStyle(fontSize = 24, textColor = "#ff0000")
        }
        val result = timer.serialize(0)
        assertContains(result, "Style:")
        assertContains(result, "FontSize: 24")
    }
}

fun IntegrationTestRunner.uiToggleRadioSeparatorTests() = suite("UI DSL - Toggle, Radio, Separator") {

    test("UiToggleButton serializes text and value") {
        val toggle = UiToggleButton().apply {
            id = "darkMode"
            text = "Dark Mode"
            value = true
        }
        val result = toggle.serialize(0)
        assertContains(result, "ToggleButton #darkMode")
        assertContains(result, "Text: \"Dark Mode\"")
        assertContains(result, "Value: true")
    }

    test("UiToggleButton serializes on/off styles") {
        val toggle = UiToggleButton().apply {
            onStyle = UiButtonStyle().apply { defaultBackground = "#4CAF50" }
            offStyle = UiButtonStyle().apply { defaultBackground = "#f44336" }
        }
        val result = toggle.serialize(0)
        assertContains(result, "Style:")
        assertContains(result, "On:")
        assertContains(result, "Off:")
    }

    test("UiRadioButton serializes properties") {
        val radio = UiRadioButton().apply {
            id = "opt1"
            text = "Option 1"
            groupName = "options"
            value = true
        }
        val result = radio.serialize(0)
        assertContains(result, "RadioButton #opt1")
        assertContains(result, "Text: \"Option 1\"")
        assertContains(result, "GroupName: \"options\"")
        assertContains(result, "Value: true")
    }

    test("UiSeparator serializes horizontal") {
        val sep = UiSeparator().apply {
            color = "#333333"
            thickness = 2
            orientation = SeparatorOrientation.Horizontal
        }
        val result = sep.serialize(0)
        assertContains(result, "Height: 2")
        assertContains(result, "Horizontal: true")
        assertContains(result, "Background: #333333")
    }

    test("UiSeparator serializes vertical") {
        val sep = UiSeparator().apply {
            thickness = 1
            orientation = SeparatorOrientation.Vertical
        }
        val result = sep.serialize(0)
        assertContains(result, "Width: 1")
        assertContains(result, "Vertical: true")
    }

    test("SeparatorOrientation enum values exist") {
        assertEquals(SeparatorOrientation.Horizontal, SeparatorOrientation.valueOf("Horizontal"))
        assertEquals(SeparatorOrientation.Vertical, SeparatorOrientation.valueOf("Vertical"))
    }
}

fun IntegrationTestRunner.uiTabTests() = suite("UI DSL - Tabs") {

    test("UiTab serializes text") {
        val tab = UiTab().apply {
            id = "tab1"
            text = "General"
        }
        val result = tab.serialize(0)
        assertContains(result, "Tab #tab1")
        assertContains(result, "Text: \"General\"")
    }

    test("UiTab serializes selected state") {
        val tab = UiTab().apply {
            text = "Selected Tab"
            selected = true
        }
        val result = tab.serialize(0)
        assertContains(result, "Selected: true")
    }

    test("UiTabPanel serializes tabs in TabBar") {
        val panel = UiTabPanel().apply {
            id = "settings"
            tabs.add(UiTab().apply { text = "Tab1" })
            tabs.add(UiTab().apply { text = "Tab2" })
        }
        val result = panel.serialize(0)
        assertContains(result, "TabPanel #settings")
        assertContains(result, "#TabBar")
        assertContains(result, "LayoutMode: Left")
    }

    test("UiTabPanel serializes panels") {
        val panel = UiTabPanel().apply {
            panels.add(UiGroup().apply { id = "panel1" })
            panels.add(UiGroup().apply { id = "panel2" })
        }
        val result = panel.serialize(0)
        assertContains(result, "Group #panel1")
        assertContains(result, "Group #panel2")
    }
}

fun IntegrationTestRunner.uiItemElementsTests() = suite("UI DSL - Item Elements") {

    test("UiItemIcon serializes with id") {
        val icon = UiItemIcon().apply {
            id = "swordIcon"
        }
        val result = icon.serialize(0)
        assertContains(result, "ItemIcon #swordIcon")
    }

    test("UiItemSlot serializes options") {
        val slot = UiItemSlot().apply {
            id = "slot1"
            showQualityBackground = true
            showQuantity = true
        }
        val result = slot.serialize(0)
        assertContains(result, "ItemSlot #slot1")
        assertContains(result, "ShowQualityBackground: true")
        assertContains(result, "ShowQuantity: true")
    }

    test("UiItemSlotButton serializes with layoutMode") {
        val button = UiItemSlotButton().apply {
            id = "slotBtn"
            layoutMode = LayoutMode.Center
        }
        val result = button.serialize(0)
        assertContains(result, "ItemSlotButton #slotBtn")
        assertContains(result, "LayoutMode: Center")
    }

    test("UiItemSlotButton serializes children") {
        val button = UiItemSlotButton().apply {
            children.add(UiItemIcon().apply { id = "icon" })
            children.add(UiLabel().apply { text = "x5" })
        }
        val result = button.serialize(0)
        assertContains(result, "ItemIcon #icon")
        assertContains(result, "x5")
    }

    test("UiItemGrid serializes slotsPerRow") {
        val grid = UiItemGrid().apply {
            id = "inventory"
            slotsPerRow = 9
        }
        val result = grid.serialize(0)
        assertContains(result, "ItemGrid #inventory")
        assertContains(result, "SlotsPerRow: 9")
    }

    test("UiItemGrid serializes style properties") {
        val grid = UiItemGrid().apply {
            slotSize = 48
            slotIconSize = 40
            slotSpacing = 4
            slotBackground = "textures/ui/slot_bg.png"
        }
        val result = grid.serialize(0)
        assertContains(result, "Style:")
        assertContains(result, "SlotSize: 48")
        assertContains(result, "SlotIconSize: 40")
        assertContains(result, "SlotSpacing: 4")
        assertContains(result, "SlotBackground: \"textures/ui/slot_bg.png\"")
    }
}

fun IntegrationTestRunner.uiInteractiveGroupBuilderTests() = suite("UI DSL - InteractiveGroupBuilder") {

    test("InteractiveGroupBuilder sets id property") {
        val builder = InteractiveGroupBuilder()
        builder.id = "myGroup"
        assertEquals("myGroup", builder.id)
    }

    test("InteractiveGroupBuilder sets layoutMode property") {
        val builder = InteractiveGroupBuilder()
        builder.layoutMode = LayoutMode.Top
        assertEquals(LayoutMode.Top, builder.layoutMode)
    }

    test("InteractiveGroupBuilder sets anchor property") {
        val builder = InteractiveGroupBuilder()
        builder.anchor = UiAnchor(width = 100, height = 50)
        assertNotNull(builder.anchor)
        assertEquals(100, builder.anchor!!.width)
    }

    test("InteractiveGroupBuilder sets padding property") {
        val builder = InteractiveGroupBuilder()
        builder.padding = UiPadding(horizontal = 10)
        assertNotNull(builder.padding)
        assertEquals(10, builder.padding!!.horizontal)
    }

    test("InteractiveGroupBuilder sets flexWeight property") {
        val builder = InteractiveGroupBuilder()
        builder.flexWeight = 2
        assertEquals(2, builder.flexWeight)
    }

    test("InteractiveGroupBuilder sets background property") {
        val builder = InteractiveGroupBuilder()
        builder.background = "#333333"
        assertEquals("#333333", builder.background)
    }

    test("InteractiveGroupBuilder textButton returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.textButton("btn") {
            text = "Click"
        }
        assertEquals("btn", wrapper.elementId)
        assertEquals("Click", (wrapper.element as UiTextButton).text)
    }

    test("InteractiveGroupBuilder slider returns wrapper with config") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.slider("vol") {
            min = 0
            max = 100
        }
        assertEquals("vol", wrapper.elementId)
        val element = wrapper.element as UiSlider
        assertEquals(0, element.min)
        assertEquals(100, element.max)
    }

    test("InteractiveGroupBuilder checkBox returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.checkBox("check") {
            value = true
        }
        assertEquals("check", wrapper.elementId)
    }

    test("InteractiveGroupBuilder textField returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.textField("input") {
            placeholderText = "Enter..."
        }
        assertEquals("input", wrapper.elementId)
    }

    test("InteractiveGroupBuilder label returns element") {
        val builder = InteractiveGroupBuilder()
        val label = builder.label("title") {
            text = "Hello"
        }
        assertEquals("title", label.id)
        assertEquals("Hello", label.text)
    }

    test("InteractiveGroupBuilder nested group returns builder") {
        val builder = InteractiveGroupBuilder()
        val innerBuilder = builder.group("inner") {
            id = "innerGroup"
        }
        assertEquals("innerGroup", innerBuilder.id)
    }

    test("InteractiveGroupBuilder sprite returns element") {
        val builder = InteractiveGroupBuilder()
        val sprite = builder.sprite("icon") {
            texturePath = "test.png"
        }
        assertEquals("icon", sprite.id)
        assertEquals("test.png", sprite.texturePath)
    }

    test("InteractiveGroupBuilder progressBar returns element") {
        val builder = InteractiveGroupBuilder()
        val bar = builder.progressBar("health") {
            barTexturePath = "bar.png"
        }
        assertEquals("health", bar.id)
        assertEquals("bar.png", bar.barTexturePath)
    }

    test("InteractiveGroupBuilder itemGrid returns element") {
        val builder = InteractiveGroupBuilder()
        val grid = builder.itemGrid("inv") {
            slotsPerRow = 9
        }
        assertEquals("inv", grid.id)
        assertEquals(9, grid.slotsPerRow)
    }

    test("InteractiveGroupBuilder numberField returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.numberField("count") {
            value = 42
            minValue = 0
            maxValue = 100
        }
        assertEquals("count", wrapper.elementId)
    }

    test("InteractiveGroupBuilder dropdownBox returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.dropdownBox("options") {
            noItemsText = "Empty"
        }
        assertEquals("options", wrapper.elementId)
    }

    test("InteractiveGroupBuilder colorPicker returns wrapper") {
        val builder = InteractiveGroupBuilder()
        val wrapper = builder.colorPicker("color") {
            format = ColorFormat.Rgba
        }
        assertEquals("color", wrapper.elementId)
    }
}

fun IntegrationTestRunner.uiIconButtonStyleTests() = suite("UI DSL - IconButtonStyle") {

    test("UiIconButtonStyle serializes default background") {
        val style = UiIconButtonStyle().apply {
            defaultBackground = "#1a1a1a"
        }
        val result = style.serialize(0)
        assertContains(result, "Style:")
        assertContains(result, "Default:")
        assertContains(result, "Background: #1a1a1a")
    }

    test("UiIconButtonStyle serializes hovered background") {
        val style = UiIconButtonStyle().apply {
            hoveredBackground = "#333333"
        }
        val result = style.serialize(0)
        assertContains(result, "Hovered:")
        assertContains(result, "Background: #333333")
    }

    test("UiIconButtonStyle serializes pressed state") {
        val style = UiIconButtonStyle().apply {
            pressedBackground = "#000000"
        }
        val result = style.serialize(0)
        assertContains(result, "Pressed:")
    }

    test("UiIconButtonStyle serializes disabled state") {
        val style = UiIconButtonStyle().apply {
            disabledBackground = "#666666"
        }
        val result = style.serialize(0)
        assertContains(result, "Disabled:")
    }

    test("UiIconButtonStyle includes sounds by default") {
        val style = UiIconButtonStyle()
        val result = style.serialize(0)
        assertContains(result, "Sounds:")
    }

    test("UiIconButtonStyle can disable sounds") {
        val style = UiIconButtonStyle().apply {
            useSounds = false
        }
        val result = style.serialize(0)
        assertFalse(result.contains("Sounds:"))
    }
}

fun IntegrationTestRunner.uiWrappersTests() = suite("UI DSL - Wrappers") {

    test("InteractiveTextButton stores elementId") {
        val element = UiTextButton().apply { id = "btn" }
        val wrapper = InteractiveTextButton("btn", element)
        assertEquals("btn", wrapper.elementId)
    }

    test("InteractiveButton stores element") {
        val element = UiButton().apply { id = "iconBtn" }
        val wrapper = InteractiveButton("iconBtn", element)
        assertEquals(element, wrapper.element)
    }

    test("InteractiveSlider stores elementId") {
        val element = UiSlider().apply { id = "vol" }
        val wrapper = InteractiveSlider("vol", element)
        assertEquals("vol", wrapper.elementId)
    }

    test("InteractiveFloatSlider stores element") {
        val element = UiFloatSlider().apply { id = "opacity" }
        val wrapper = InteractiveFloatSlider("opacity", element)
        assertEquals(element, wrapper.element)
    }

    test("InteractiveCheckBox stores elementId") {
        val element = UiCheckBox().apply { id = "enabled" }
        val wrapper = InteractiveCheckBox("enabled", element)
        assertEquals("enabled", wrapper.elementId)
    }

    test("InteractiveTextField stores element") {
        val element = UiTextField().apply { id = "name" }
        val wrapper = InteractiveTextField("name", element)
        assertEquals(element, wrapper.element)
    }

    test("InteractiveNumberField stores elementId") {
        val element = UiNumberField().apply { id = "count" }
        val wrapper = InteractiveNumberField("count", element)
        assertEquals("count", wrapper.elementId)
    }

    test("InteractiveDropdownBox stores element") {
        val element = UiDropdownBox().apply { id = "options" }
        val wrapper = InteractiveDropdownBox("options", element)
        assertEquals(element, wrapper.element)
    }

    test("InteractiveColorPicker stores elementId") {
        val element = UiColorPicker().apply { id = "color" }
        val wrapper = InteractiveColorPicker("color", element)
        assertEquals("color", wrapper.elementId)
    }

    test("InteractiveColorPickerDropdownBox stores element") {
        val element = UiColorPickerDropdownBox().apply { id = "colorDropdown" }
        val wrapper = InteractiveColorPickerDropdownBox("colorDropdown", element)
        assertEquals(element, wrapper.element)
    }

    test("InteractiveItemSlotButton stores elementId") {
        val element = UiItemSlotButton().apply { id = "slot" }
        val wrapper = InteractiveItemSlotButton("slot", element)
        assertEquals("slot", wrapper.elementId)
    }
}
