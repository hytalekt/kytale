package io.github.hytalekt.kytale.ui.dsl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain

class UiElementsTest : FunSpec({

    context("UiLabel") {
        test("creates label with text") {
            val label = UiLabel().apply {
                text = "Hello World"
            }
            val serialized = label.serialize()

            serialized shouldContain "Label {"
            serialized shouldContain "Text: \"Hello World\""
        }

        test("creates label with id") {
            val label = UiLabel().apply {
                id = "MyLabel"
                text = "Test"
            }
            val serialized = label.serialize()

            serialized shouldContain "Label #MyLabel {"
        }

        test("creates label with style") {
            val label = UiLabel().apply {
                text = "Styled"
                style = UiLabelStyle(
                    fontSize = 16,
                    textColor = "#ffffff",
                    renderBold = true,
                )
            }
            val serialized = label.serialize()

            serialized shouldContain "Style:"
            serialized shouldContain "FontSize: 16"
            serialized shouldContain "TextColor: #ffffff"
            serialized shouldContain "RenderBold: true"
        }
    }

    context("UiTextField") {
        test("creates text field with template by default") {
            val textField = UiTextField().apply {
                placeholderText = "Enter text..."
            }
            val serialized = textField.serialize()

            serialized shouldContain "\$C.@TextField {"
            serialized shouldContain "PlaceholderText: \"Enter text...\""
        }

        test("creates text field with id") {
            val textField = UiTextField().apply {
                id = "UsernameInput"
                placeholderText = "Username"
            }
            val serialized = textField.serialize()

            serialized shouldContain "\$C.@TextField #UsernameInput {"
        }

        test("creates text field with max length") {
            val textField = UiTextField().apply {
                maxLength = 50
            }
            val serialized = textField.serialize()

            serialized shouldContain "MaxLength: 50"
        }

        test("creates text field without template when disabled") {
            val textField = UiTextField().apply {
                useTemplate = false
                placeholderText = "Custom"
            }
            val serialized = textField.serialize()

            serialized shouldContain "TextField {"
            serialized shouldNotContain "\$C.@TextField"
        }
    }

    context("UiSlider") {
        test("creates slider with range") {
            val slider = UiSlider().apply {
                min = 0
                max = 100
            }
            val serialized = slider.serialize()

            serialized shouldContain "Slider {"
            serialized shouldContain "Min: 0"
            serialized shouldContain "Max: 100"
        }

        test("creates slider with id") {
            val slider = UiSlider().apply {
                id = "VolumeSlider"
                min = 0
                max = 100
            }
            val serialized = slider.serialize()

            serialized shouldContain "Slider #VolumeSlider {"
        }

        test("creates slider with step and default value") {
            val slider = UiSlider().apply {
                min = 0
                max = 100
                step = 10
                value = 50
            }
            val serialized = slider.serialize()

            serialized shouldContain "Step: 10"
            serialized shouldContain "Value: 50"
        }

        test("uses default style by default") {
            val slider = UiSlider()
            val serialized = slider.serialize()

            serialized shouldContain "Style: \$C.@DefaultSliderStyle"
        }

        test("omits default style when disabled") {
            val slider = UiSlider().apply {
                useDefaultStyle = false
            }
            val serialized = slider.serialize()

            serialized shouldNotContain "DefaultSliderStyle"
        }
    }

    context("UiFloatSlider") {
        test("creates float slider with range") {
            val slider = UiFloatSlider().apply {
                min = 0.0
                max = 1.0
            }
            val serialized = slider.serialize()

            serialized shouldContain "FloatSlider {"
            serialized shouldContain "Min: 0.0"
            serialized shouldContain "Max: 1.0"
        }

        test("creates float slider with step") {
            val slider = UiFloatSlider().apply {
                min = 0.0
                max = 1.0
                step = 0.1
                value = 0.5
            }
            val serialized = slider.serialize()

            serialized shouldContain "Step: 0.1"
            serialized shouldContain "Value: 0.5"
        }
    }

    context("UiCheckBox") {
        test("creates checkbox with template by default") {
            val checkBox = UiCheckBox()
            val serialized = checkBox.serialize()

            serialized shouldContain "\$C.@CheckBox {"
        }

        test("creates checkbox with id") {
            val checkBox = UiCheckBox().apply {
                id = "AcceptTerms"
            }
            val serialized = checkBox.serialize()

            serialized shouldContain "\$C.@CheckBox #AcceptTerms {"
        }

        test("creates checkbox with initial value") {
            val checkBox = UiCheckBox().apply {
                value = true
            }
            val serialized = checkBox.serialize()

            serialized shouldContain "Value: true"
        }

        test("creates checkbox without template when disabled") {
            val checkBox = UiCheckBox().apply {
                useTemplate = false
            }
            val serialized = checkBox.serialize()

            serialized shouldContain "CheckBox {"
            serialized shouldNotContain "\$C.@CheckBox"
        }
    }

    context("UiTextButton") {
        test("creates text button with text") {
            val button = UiTextButton().apply {
                id = "SubmitBtn"
                text = "Submit"
            }
            val serialized = button.serialize()

            serialized shouldContain "\$C.@TextButton #SubmitBtn {"
            serialized shouldContain "Text: \"Submit\""
        }

        test("creates text button with disabled state") {
            val button = UiTextButton().apply {
                id = "DisabledBtn"
                text = "Cannot Click"
                disabled = true
            }
            val serialized = button.serialize()

            serialized shouldContain "Disabled: true"
        }

        test("creates text button with style") {
            val button = UiTextButton().apply {
                id = "StyledBtn"
                text = "Styled"
                style = UiButtonStyle().apply {
                    defaultBackground = "#1a2636"
                    hoveredBackground = "#243448"
                    labelStyle = UiLabelStyle(fontSize = 14, textColor = "#ffffff")
                }
            }
            val serialized = button.serialize()

            serialized shouldContain "Style:"
            serialized shouldContain "Default:"
            serialized shouldContain "Background: #1a2636"
            serialized shouldContain "Hovered:"
            serialized shouldContain "Background: #243448"
        }
    }

    context("UiButton") {
        test("creates icon button without id") {
            val button = UiButton()
            val serialized = button.serialize()

            serialized shouldContain "Button {"
        }

        test("creates icon button with id") {
            val button = UiButton().apply {
                id = "CloseBtn"
            }
            val serialized = button.serialize()

            serialized shouldContain "Button #CloseBtn {"
        }

        test("creates icon button with style") {
            val button = UiButton().apply {
                id = "IconBtn"
                style = UiIconButtonStyle().apply {
                    defaultBackground = "#333333"
                    hoveredBackground = "#555555"
                }
            }
            val serialized = button.serialize()

            serialized shouldContain "Style:"
            serialized shouldContain "Default: (Background: #333333)"
            serialized shouldContain "Hovered: (Background: #555555)"
        }
    }

    context("UiDropdownBox") {
        test("creates dropdown with template by default") {
            val dropdown = UiDropdownBox()
            val serialized = dropdown.serialize()

            serialized shouldContain "\$C.@DropdownBox {"
        }

        test("creates dropdown with id") {
            val dropdown = UiDropdownBox().apply {
                id = "ThemeSelector"
            }
            val serialized = dropdown.serialize()

            serialized shouldContain "\$C.@DropdownBox #ThemeSelector {"
        }

        test("creates dropdown with no items text") {
            val dropdown = UiDropdownBox().apply {
                noItemsText = "No options available"
            }
            val serialized = dropdown.serialize()

            serialized shouldContain "NoItemsText: \"No options available\""
        }
    }

    context("UiToggleButton") {
        test("creates toggle button with text") {
            val toggle = UiToggleButton().apply {
                id = "DarkMode"
                text = "Dark Mode"
            }
            val serialized = toggle.serialize()

            serialized shouldContain "ToggleButton #DarkMode {"
            serialized shouldContain "Text: \"Dark Mode\""
        }

        test("creates toggle button with initial value") {
            val toggle = UiToggleButton().apply {
                value = true
            }
            val serialized = toggle.serialize()

            serialized shouldContain "Value: true"
        }

        test("creates toggle button with on/off styles") {
            val toggle = UiToggleButton().apply {
                id = "Toggle"
                onStyle = UiButtonStyle().apply {
                    defaultBackground = "#4CAF50"
                    labelStyle = UiLabelStyle(textColor = "#ffffff")
                }
                offStyle = UiButtonStyle().apply {
                    defaultBackground = "#757575"
                    labelStyle = UiLabelStyle(textColor = "#cccccc")
                }
            }
            val serialized = toggle.serialize()

            serialized shouldContain "Style:"
            serialized shouldContain "On:"
            serialized shouldContain "Background: #4CAF50"
            serialized shouldContain "Off:"
            serialized shouldContain "Background: #757575"
        }
    }

    context("UiSprite") {
        test("creates sprite with texture path") {
            val sprite = UiSprite().apply {
                texturePath = "UI/Icons/star.png"
            }
            val serialized = sprite.serialize()

            serialized shouldContain "Sprite {"
            serialized shouldContain "TexturePath: \"UI/Icons/star.png\""
        }

        test("creates sprite with frame properties") {
            val sprite = UiSprite().apply {
                texturePath = "UI/Animations/loading.png"
                frameWidth = 32
                frameHeight = 32
                framesPerRow = 8
                frameCount = 16
                framesPerSecond = 12
            }
            val serialized = sprite.serialize()

            serialized shouldContain "Frame: (Width: 32, Height: 32, PerRow: 8, Count: 16)"
            serialized shouldContain "FramesPerSecond: 12"
        }
    }

    context("UiProgressBar") {
        test("creates progress bar with bar texture") {
            val progressBar = UiProgressBar().apply {
                id = "HealthBar"
                barTexturePath = "UI/Bars/health.png"
            }
            val serialized = progressBar.serialize()

            serialized shouldContain "ProgressBar #HealthBar {"
            serialized shouldContain "BarTexturePath: \"UI/Bars/health.png\""
        }

        test("creates progress bar with effect") {
            val progressBar = UiProgressBar().apply {
                effectTexturePath = "UI/Effects/glow.png"
                effectWidth = 16
                effectHeight = 8
                effectOffset = 2
            }
            val serialized = progressBar.serialize()

            serialized shouldContain "EffectTexturePath: \"UI/Effects/glow.png\""
            serialized shouldContain "EffectWidth: 16"
            serialized shouldContain "EffectHeight: 8"
            serialized shouldContain "EffectOffset: 2"
        }
    }

    context("UiTimerLabel") {
        test("creates timer label with seconds") {
            val timer = UiTimerLabel().apply {
                id = "Countdown"
                seconds = 60
            }
            val serialized = timer.serialize()

            serialized shouldContain "TimerLabel #Countdown {"
            serialized shouldContain "Seconds: 60"
        }

        test("creates timer label with style") {
            val timer = UiTimerLabel().apply {
                seconds = 30
                style = UiLabelStyle(fontSize = 24, textColor = "#ff0000")
            }
            val serialized = timer.serialize()

            serialized shouldContain "Style:"
            serialized shouldContain "FontSize: 24"
            serialized shouldContain "TextColor: #ff0000"
        }
    }

    context("UiMultilineTextField") {
        test("creates multiline text field") {
            val multiline = UiMultilineTextField().apply {
                id = "Description"
                placeholderText = "Enter description..."
            }
            val serialized = multiline.serialize()

            serialized shouldContain "MultilineTextField #Description {"
            serialized shouldContain "PlaceholderText: \"Enter description...\""
        }

        test("creates multiline text field with auto grow") {
            val multiline = UiMultilineTextField().apply {
                autoGrow = true
            }
            val serialized = multiline.serialize()

            serialized shouldContain "AutoGrow: true"
        }
    }

    context("UiElement common properties") {
        test("anchor serializes correctly") {
            val label = UiLabel().apply {
                anchor = UiAnchor(width = 200, height = 50, left = 10, top = 20)
            }
            val serialized = label.serialize()

            serialized shouldContain "Anchor: (Width: 200, Height: 50, Left: 10, Top: 20)"
        }

        test("padding serializes correctly") {
            val label = UiLabel().apply {
                padding = UiPadding(horizontal = 10, vertical = 5)
            }
            val serialized = label.serialize()

            serialized shouldContain "Padding: (Horizontal: 10, Vertical: 5)"
        }

        test("flexWeight serializes correctly") {
            val label = UiLabel().apply {
                flexWeight = 2
            }
            val serialized = label.serialize()

            serialized shouldContain "FlexWeight: 2"
        }

        test("background serializes correctly") {
            val group = UiGroup().apply {
                background = "#1a2636"
            }
            val serialized = group.serialize()

            serialized shouldContain "Background: #1a2636"
        }

        test("visible serializes correctly") {
            val label = UiLabel().apply {
                visible = false
            }
            val serialized = label.serialize()

            serialized shouldContain "Visible: false"
        }

        test("tooltipText serializes correctly") {
            val label = UiLabel().apply {
                tooltipText = "This is a tooltip"
            }
            val serialized = label.serialize()

            serialized shouldContain "TooltipText: \"This is a tooltip\""
        }

        test("hitTestVisible serializes correctly") {
            val label = UiLabel().apply {
                hitTestVisible = false
            }
            val serialized = label.serialize()

            serialized shouldContain "HitTestVisible: false"
        }
    }

    context("UiAnchor") {
        test("fill sets all edges to zero") {
            val anchor = UiAnchor()
            anchor.fill()

            anchor.left shouldBe 0
            anchor.right shouldBe 0
            anchor.top shouldBe 0
            anchor.bottom shouldBe 0
        }

        test("size sets width and height") {
            val anchor = UiAnchor()
            anchor.size(300, 200)

            anchor.width shouldBe 300
            anchor.height shouldBe 200
        }

        test("serializes min/max dimensions") {
            val anchor = UiAnchor(minWidth = 100, maxWidth = 500, minHeight = 50, maxHeight = 300)
            val serialized = anchor.serialize()

            serialized shouldContain "MinWidth: 100"
            serialized shouldContain "MaxWidth: 500"
            serialized shouldContain "MinHeight: 50"
            serialized shouldContain "MaxHeight: 300"
        }

        test("serializes full flag") {
            val anchor = UiAnchor(full = true)
            val serialized = anchor.serialize()

            serialized shouldContain "Full: true"
        }

        test("serializes horizontal and vertical flags") {
            val anchor = UiAnchor(horizontal = true, vertical = true)
            val serialized = anchor.serialize()

            serialized shouldContain "Horizontal: true"
            serialized shouldContain "Vertical: true"
        }
    }

    context("UiPadding") {
        test("serializes all sides") {
            val padding = UiPadding(left = 5, right = 10, top = 15, bottom = 20)
            val serialized = padding.serialize()

            serialized shouldContain "Left: 5"
            serialized shouldContain "Right: 10"
            serialized shouldContain "Top: 15"
            serialized shouldContain "Bottom: 20"
        }

        test("serializes horizontal and vertical shortcuts") {
            val padding = UiPadding(horizontal = 10, vertical = 20)
            val serialized = padding.serialize()

            serialized shouldContain "Horizontal: 10"
            serialized shouldContain "Vertical: 20"
        }
    }

    context("UiLabelStyle") {
        test("serializes all properties") {
            val style = UiLabelStyle(
                fontSize = 18,
                textColor = "#ffffff",
                renderBold = true,
                renderUppercase = true,
                horizontalAlignment = HorizontalAlignment.Center,
                verticalAlignment = VerticalAlignment.Center,
                wrap = true,
                lineSpacing = 4,
            )
            val serialized = style.serialize()

            serialized shouldContain "FontSize: 18"
            serialized shouldContain "TextColor: #ffffff"
            serialized shouldContain "RenderBold: true"
            serialized shouldContain "RenderUppercase: true"
            serialized shouldContain "HorizontalAlignment: Center"
            serialized shouldContain "VerticalAlignment: Center"
            serialized shouldContain "Wrap: true"
            serialized shouldContain "LineSpacing: 4"
        }

        test("alignment enums serialize correctly") {
            HorizontalAlignment.Left.value shouldBe "Left"
            HorizontalAlignment.Center.value shouldBe "Center"
            HorizontalAlignment.Right.value shouldBe "Right"

            VerticalAlignment.Top.value shouldBe "Top"
            VerticalAlignment.Center.value shouldBe "Center"
            VerticalAlignment.Bottom.value shouldBe "Bottom"
        }
    }

    context("UiButtonStyle") {
        test("includes button sounds by default") {
            val style = UiButtonStyle().apply {
                defaultBackground = "#000000"
            }
            val serialized = style.serialize(0)

            serialized shouldContain "Sounds: \$C.@ButtonSounds"
        }

        test("excludes button sounds when disabled") {
            val style = UiButtonStyle().apply {
                defaultBackground = "#000000"
                useSounds = false
            }
            val serialized = style.serialize(0)

            serialized shouldNotContain "Sounds"
        }

        test("serializes pressed and disabled states") {
            val style = UiButtonStyle().apply {
                defaultBackground = "#000000"
                pressedBackground = "#111111"
                disabledBackground = "#222222"
            }
            val serialized = style.serialize(0)

            serialized shouldContain "Pressed: (Background: #111111)"
            serialized shouldContain "Disabled: (Background: #222222)"
        }
    }

    context("LayoutMode enum") {
        test("all layout modes have correct values") {
            LayoutMode.Left.value shouldBe "Left"
            LayoutMode.Right.value shouldBe "Right"
            LayoutMode.Top.value shouldBe "Top"
            LayoutMode.Bottom.value shouldBe "Bottom"
            LayoutMode.Center.value shouldBe "Center"
            LayoutMode.Middle.value shouldBe "Middle"
            LayoutMode.MiddleCenter.value shouldBe "MiddleCenter"
            LayoutMode.CenterMiddle.value shouldBe "CenterMiddle"
            LayoutMode.Full.value shouldBe "Full"
            LayoutMode.TopScrolling.value shouldBe "TopScrolling"
            LayoutMode.LeftScrolling.value shouldBe "LeftScrolling"
            LayoutMode.LeftCenterWrap.value shouldBe "LeftCenterWrap"
        }
    }
})
