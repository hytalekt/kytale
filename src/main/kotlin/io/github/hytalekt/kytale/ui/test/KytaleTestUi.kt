package io.github.hytalekt.kytale.ui.test

import io.github.hytalekt.kytale.ui.dsl.UiDefinition

/**
 * UI definitions for Kytale's built-in test pages.
 *
 * Registers interactive UI test pages that demonstrate the combined DSL.
 */
@UiDefinition
object KytaleTestUi {

    fun registerAll() {
        // Interactive UI pages with full event handling
        InteractiveTestPages.registerAll()
    }
}
