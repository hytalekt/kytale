package aster.amo.kytale

import aster.amo.kytale.coroutines.HytaleDispatchers
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * Abstract base class for Hytale plugins written in Kotlin.
 *
 * Provides a built-in [CoroutineScope] tied to the plugin's lifecycle,
 * with automatic cancellation when the plugin is unloaded.
 *
 * Hytale plugin lifecycle:
 * 1. Constructor - Basic initialization
 * 2. [setup] - Register events, commands, and entity systems
 * 3. [start] - Post-setup initialization (all plugins loaded)
 * 4. [shutdown] - Cleanup when plugin is unloaded
 *
 * Example usage:
 * ```kotlin
 * class MyPlugin(init: JavaPluginInit) : KotlinPlugin(init) {
 *
 *     override fun setup() {
 *         super.setup()
 *
 *         events {
 *             on<PlayerConnectEvent> { e ->
 *                 // Handle event
 *             }
 *         }
 *
 *         command("mycommand", "Description") {
 *             executes { ctx -> /* ... */ }
 *         }
 *     }
 *
 *     override fun start() {
 *         super.start()
 *         logger.atInfo().log("Plugin started!")
 *
 *         launch {
 *             // Coroutine runs on async dispatcher
 *             performAsyncTask()
 *         }
 *     }
 *
 *     override fun shutdown() {
 *         logger.atInfo().log("Plugin shutting down!")
 *         super.shutdown()
 *     }
 * }
 * ```
 *
 * @param init the plugin initialization context provided by the server
 */
abstract class KotlinPlugin(init: JavaPluginInit) : JavaPlugin(init), CoroutineScope {

    private val supervisorJob = SupervisorJob()

    /**
     * The coroutine context for this plugin's scope.
     *
     * Uses a [SupervisorJob] to prevent child coroutine failures from
     * cancelling sibling coroutines, combined with the async dispatcher.
     */
    override val coroutineContext: CoroutineContext
        get() = supervisorJob + HytaleDispatchers.Async

    /**
     * Called during plugin setup phase to register events, commands, and systems.
     *
     * Override this method and call `super.setup()` first. This is the safe place
     * to register event handlers, commands, and entity systems.
     */
    override fun setup() {
        super.setup()
    }

    /**
     * Called after all plugins have been set up.
     *
     * Override this method and call `super.start()` first. Use this for
     * initialization that depends on other plugins being loaded.
     */
    override fun start() {
        super.start()
    }

    /**
     * Called when the plugin is being shut down by the server.
     *
     * Override this method for cleanup. Call `super.shutdown()` last to ensure
     * coroutines are cancelled after your cleanup completes.
     */
    override fun shutdown() {
        cancelCoroutines()
        super.shutdown()
    }

    /**
     * Cancels all coroutines launched in this plugin's scope.
     *
     * @param message optional cancellation message for debugging
     */
    protected fun cancelCoroutines(message: String = "Plugin unloaded") {
        supervisorJob.cancel(message)
    }
}
