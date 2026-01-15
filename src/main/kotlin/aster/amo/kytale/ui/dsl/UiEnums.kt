package aster.amo.kytale.ui.dsl

enum class LayoutMode(val value: String) {
    Left("Left"),
    Right("Right"),
    Top("Top"),
    Bottom("Bottom"),
    Center("Center"),
    Middle("Middle"),
    MiddleCenter("MiddleCenter"),
    CenterMiddle("CenterMiddle"),
    Full("Full"),
    TopScrolling("TopScrolling"),
    LeftScrolling("LeftScrolling"),
    LeftCenterWrap("LeftCenterWrap")
}

enum class HorizontalAlignment(val value: String) {
    Left("Left"),
    Center("Center"),
    Right("Right")
}

enum class VerticalAlignment(val value: String) {
    Top("Top"),
    Center("Center"),
    Bottom("Bottom")
}

/**
 * Color format for color pickers.
 */
enum class ColorFormat(val value: String) {
    Rgb("Rgb"),
    Rgba("Rgba"),
    Hsv("Hsv"),
    Hsva("Hsva")
}
