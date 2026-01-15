package aster.amo.kytale.ui.dsl

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Registry for UI page definitions.
 *
 * UI pages registered here can be compiled to .ui files at build time.
 */
object UiRegistry {
    private val pages = ConcurrentHashMap<String, UiPage>()

    /**
     * Register a UI page.
     *
     * @param path The path for the .ui file (e.g., "SurvivalGames/SurvivalGames_Lobby")
     * @param page The UI page definition
     */
    fun register(path: String, page: UiPage) {
        pages[path] = page
    }

    /**
     * Get all registered pages.
     */
    fun getPages(): Map<String, UiPage> = pages.toMap()

    /**
     * Compile all registered pages to .ui files.
     *
     * @param outputDir The base output directory (e.g., "src/main/resources/Common/UI/Custom/Pages")
     */
    fun compileAll(outputDir: File) {
        pages.forEach { (path, page) ->
            val file = File(outputDir, "$path.ui")
            file.parentFile.mkdirs()
            file.writeText(page.serialize())
            println("Generated: ${file.absolutePath}")
        }
    }

    /**
     * Compile a single page to a string.
     */
    fun compile(path: String): String? {
        return pages[path]?.serialize()
    }

    /**
     * Clear all registered pages.
     */
    fun clear() {
        pages.clear()
    }
}

/**
 * Register a UI page with the registry.
 */
fun uiPage(path: String, name: String, block: UiPage.() -> Unit): UiPage {
    val page = UiPage(name).apply(block)
    UiRegistry.register(path, page)
    return page
}
