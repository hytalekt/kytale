package aster.amo.kytale.ui.dsl

import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.universe.PlayerRef
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Registry for interactive UI page definitions.
 *
 * This registry serves two purposes:
 * 1. **Compile-time**: Generates .ui files from DSL definitions
 * 2. **Runtime**: Provides page factories to create InteractiveCustomUIPage instances
 *
 * Usage:
 * ```kotlin
 * @UiDefinition
 * object MyGameUi {
 *     fun registerAll() {
 *         // Register interactive pages
 *         InteractiveUiRegistry.register("MyGame/Settings", settingsPage)
 *         InteractiveUiRegistry.register("MyGame/Inventory", inventoryPage)
 *     }
 *
 *     val settingsPage = interactivePage("Settings") {
 *         width = 500; height = 400
 *         content {
 *             slider("Volume") {
 *                 min = 0; max = 100; value = 75
 *                 onValueChange = { value ->
 *                     player.sendMessage(Message.raw("Volume: $value%"))
 *                 }
 *             }
 *         }
 *     }
 * }
 *
 * // At runtime:
 * val page = InteractiveUiRegistry.createPage("MyGame/Settings", playerRef)
 * player.pageManager.openCustomPage(ref, store, page)
 *
 * // Or use the extension:
 * player.openPage("MyGame/Settings", playerRef)
 * ```
 */
object InteractiveUiRegistry {
    private val pages = ConcurrentHashMap<String, InteractiveUiPage>()

    /**
     * Register an interactive UI page.
     *
     * @param path The path for the .ui file (e.g., "MyGame/Settings")
     * @param page The interactive UI page definition
     */
    fun register(path: String, page: InteractiveUiPage) {
        // Update the page's uiFilePath to match the registry path
        page.uiFilePath = "Pages/$path.ui"
        pages[path] = page
    }

    /**
     * Get a registered page definition by path.
     *
     * @param path The registered path
     * @return The page definition, or null if not found
     */
    fun get(path: String): InteractiveUiPage? = pages[path]

    /**
     * Get all registered pages.
     */
    fun getPages(): Map<String, InteractiveUiPage> = pages.toMap()

    /**
     * Create a page instance for a player.
     *
     * @param path The registered path
     * @param playerRef The player reference
     * @return The page instance, or null if not found
     */
    fun createPage(path: String, playerRef: PlayerRef): InteractiveCustomUIPage<DynamicEventData>? {
        return pages[path]?.createPage(playerRef)
    }

    /**
     * Check if a page is registered.
     *
     * @param path The path to check
     * @return true if registered
     */
    fun contains(path: String): Boolean = pages.containsKey(path)

    /**
     * Get all registered page paths.
     */
    fun getPaths(): Set<String> = pages.keys.toSet()

    /**
     * Compile all registered pages to .ui files.
     *
     * @param outputDir The base output directory (e.g., "src/main/resources/Common/UI/Custom/Pages")
     */
    fun compileAll(outputDir: File) {
        pages.forEach { (path, page) ->
            val file = File(outputDir, "$path.ui")
            file.parentFile.mkdirs()
            file.writeText(page.generateUiFile())
            println("Generated (Interactive): ${file.absolutePath}")
        }
    }

    /**
     * Compile a single page to a string.
     *
     * @param path The page path
     * @return The .ui file content, or null if not found
     */
    fun compile(path: String): String? {
        return pages[path]?.generateUiFile()
    }

    /**
     * Clear all registered pages.
     */
    fun clear() {
        pages.clear()
    }
}

/**
 * Create and register an interactive page in one step.
 *
 * This is a convenience function that creates the page and registers it immediately.
 *
 * @param path The path for the .ui file (e.g., "MyGame/Settings")
 * @param name The page name (used for debugging)
 * @param block The DSL block to define the page
 * @return The created page
 */
fun registerInteractivePage(path: String, name: String, block: InteractiveUiPage.() -> Unit): InteractiveUiPage {
    val page = InteractiveUiPage(name).apply(block)
    InteractiveUiRegistry.register(path, page)
    return page
}

/**
 * Extension function to open a registered interactive page for a player.
 *
 * @param path The registered page path
 * @param playerRef The player reference
 * @return true if the page was opened, false if not found
 */
fun Player.openPage(path: String, playerRef: PlayerRef): Boolean {
    val ref = playerRef.reference ?: return false
    val page = InteractiveUiRegistry.createPage(path, playerRef) ?: return false
    this.pageManager.openCustomPage(ref, ref.store, page)
    return true
}
