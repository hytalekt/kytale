package io.github.hytalekt.kytale.ui.dsl

/**
 * DSL marker for Kytale UI builders.
 *
 * Prevents scope pollution in nested UI DSL blocks.
 */
@DslMarker
annotation class UiDsl
