package io.github.hytalekt.kytale.hexweave.dsl

/**
 * DSL marker for Hexweave builders.
 *
 * Prevents scope pollution in nested Hexweave DSL blocks.
 */
@DslMarker
annotation class HexweaveDsl
