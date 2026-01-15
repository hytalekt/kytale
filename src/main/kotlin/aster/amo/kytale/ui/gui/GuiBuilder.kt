package aster.amo.kytale.ui.gui

/**
 * DSL marker for GUI builders.
 */
@DslMarker
annotation class GuiDsl

/**
 * Configuration for a button binding.
 */
data class ButtonConfig(
    val selector: String,
    val action: String
)

/**
 * Configuration for setting UI text.
 */
data class TextSetterConfig(
    val selector: String,
    val valueProvider: () -> String
)

/**
 * Abstract GUI page configuration.
 *
 * This provides the DSL interface for configuring GUI pages.
 * Implementations will wire this to the actual Hytale API.
 */
@GuiDsl
class GuiPageConfig {
    var uiPath: String = ""
    internal val buttons = mutableListOf<ButtonConfig>()
    internal val textSetters = mutableListOf<TextSetterConfig>()
    internal val eventHandlers = mutableMapOf<String, () -> Unit>()

    /**
     * Bind a button click to an action name.
     */
    fun button(selector: String, action: String) {
        buttons.add(ButtonConfig(selector, action))
    }

    /**
     * Set a UI element's text value dynamically.
     */
    fun setText(selector: String, value: () -> String) {
        textSetters.add(TextSetterConfig(selector, value))
    }

    /**
     * Handle a button action.
     */
    fun on(action: String, handler: () -> Unit) {
        eventHandlers[action] = handler
    }

    /**
     * Get button configurations.
     */
    fun getButtons(): List<ButtonConfig> = buttons.toList()

    /**
     * Get text setter configurations.
     */
    fun getTextSetters(): List<TextSetterConfig> = textSetters.toList()

    /**
     * Handle an event by action name.
     */
    fun handleEvent(action: String?) {
        action?.let { eventHandlers[it]?.invoke() }
    }
}

/**
 * Create a GUI page configuration using the DSL.
 */
inline fun guiPage(block: GuiPageConfig.() -> Unit): GuiPageConfig {
    return GuiPageConfig().apply(block)
}
