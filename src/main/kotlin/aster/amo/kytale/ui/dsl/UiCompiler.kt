package aster.amo.kytale.ui.dsl

import java.io.File

/**
 * Compiler utility for generating .ui files from DSL definitions.
 *
 * Usage in your build.gradle.kts:
 * ```kotlin
 * tasks.register("compileUi") {
 *     doLast {
 *         // Register your UI pages
 *         aster.amo.kytale.ui.dsl.UiRegistry.register("MyPage", myPageDefinition)
 *
 *         // Compile to output directory
 *         aster.amo.kytale.ui.dsl.UiCompiler.compile(
 *             outputDir = file("src/main/resources/Common/UI/Custom/Pages")
 *         )
 *     }
 * }
 * ```
 *
 * Or create a separate Kotlin file with your UI definitions:
 * ```kotlin
 * // src/main/kotlin/mypackage/UiDefinitions.kt
 * object UiDefinitions {
 *     fun registerAll() {
 *         UiRegistry.register("MyGame/MyPage", myPage)
 *     }
 *
 *     val myPage = uiPage("MyPage") {
 *         title = "My Page"
 *         width = 500
 *         height = 400
 *         content {
 *             label("Title") {
 *                 text = "Hello World"
 *                 style { fontSize = 24; textColor = "#ffffff" }
 *             }
 *         }
 *     }
 * }
 * ```
 */
object UiCompiler {
    /**
     * Compile all registered UI pages to .ui files.
     *
     * @param outputDir The base output directory for .ui files
     */
    fun compile(outputDir: File) {
        UiRegistry.compileAll(outputDir)
    }

    /**
     * Compile a single UI page to a string.
     *
     * @param page The UI page to compile
     * @return The .ui file content
     */
    fun compile(page: UiPage): String {
        return page.serialize()
    }

    /**
     * Write a UI page to a file.
     *
     * @param page The UI page to write
     * @param file The output file
     */
    fun writeTo(page: UiPage, file: File) {
        file.parentFile?.mkdirs()
        file.writeText(page.serialize())
    }
}

/**
 * Main entry point for command-line compilation.
 *
 * Usage: java -cp <classpath> aster.amo.kytale.ui.dsl.UiCompilerKt <outputDir>
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: UiCompiler <outputDir>")
        println("  Compiles all registered UI pages to the specified directory.")
        return
    }

    val outputDir = File(args[0])
    println("Compiling UI pages to: ${outputDir.absolutePath}")
    UiCompiler.compile(outputDir)
    println("Done!")
}
