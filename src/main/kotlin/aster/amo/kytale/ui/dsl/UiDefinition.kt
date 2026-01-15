package aster.amo.kytale.ui.dsl

/**
 * Marks a class or object as containing UI definitions.
 *
 * Classes/objects annotated with this should have a `registerAll()` method
 * that registers all UI pages with [UiRegistry].
 *
 * Example:
 * ```kotlin
 * @UiDefinition
 * object MyGameUi {
 *     fun registerAll() {
 *         UiRegistry.register("MyGame/MainMenu", mainMenu)
 *         UiRegistry.register("MyGame/Settings", settings)
 *     }
 *
 *     val mainMenu = uiPage("MainMenu") {
 *         title = "Main Menu"
 *         // ...
 *     }
 *
 *     val settings = uiPage("Settings") {
 *         title = "Settings"
 *         // ...
 *     }
 * }
 * ```
 *
 * Then create a compiler entry point:
 * ```kotlin
 * // UiCompiler.kt
 * fun main(args: Array<String>) {
 *     val outputDir = File(args[0])
 *     MyGameUi.registerAll()
 *     UiRegistry.compileAll(outputDir)
 * }
 * ```
 *
 * And configure the Gradle plugin:
 * ```kotlin
 * // build.gradle.kts
 * plugins {
 *     id("aster.amo.kytale.ui")
 * }
 *
 * kytaleUi {
 *     scannerClass.set("com.example.mymod.UiCompilerKt")
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UiDefinition
