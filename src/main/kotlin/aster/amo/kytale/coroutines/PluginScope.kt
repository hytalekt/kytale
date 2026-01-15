package aster.amo.kytale.coroutines

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * A coroutine scope tied to a plugin's lifecycle.
 *
 * Creates a supervised scope that uses [HytaleDispatchers.Async] by default.
 * All coroutines launched in this scope are automatically cancelled when
 * [cancel] is called, typically during plugin shutdown.
 *
 * Example usage:
 * ```kotlin
 * class MyPlugin(init: JavaPluginInit) : JavaPlugin(init) {
 *     private val scope = PluginScope(this)
 *
 *     init {
 *         scope.launch {
 *             // Background task
 *         }
 *     }
 *
 *     fun shutdown() {
 *         scope.cancel()
 *     }
 * }
 * ```
 *
 * @property plugin the plugin this scope is associated with
 * @property dispatcher the default dispatcher for coroutines in this scope
 */
class PluginScope(
    private val plugin: JavaPlugin,
    private val dispatcher: CoroutineContext = HytaleDispatchers.Async
) : CoroutineScope {

    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = supervisorJob + dispatcher

    /**
     * Cancels all coroutines in this scope.
     *
     * Should be called when the plugin is being disabled to ensure
     * all background tasks are properly terminated.
     *
     * @param message optional message describing why the scope was cancelled
     */
    fun cancel(message: String = "Plugin scope cancelled") {
        supervisorJob.cancel(message)
    }

    /**
     * Returns whether this scope is still active.
     *
     * @return true if the scope has not been cancelled
     */
    val isActive: Boolean
        get() = supervisorJob.isActive
}

/**
 * Creates a new [PluginScope] for this plugin.
 *
 * @param dispatcher the default dispatcher for the scope
 * @return a new plugin scope tied to this plugin
 */
fun JavaPlugin.pluginScope(
    dispatcher: CoroutineContext = HytaleDispatchers.Async
): PluginScope = PluginScope(this, dispatcher)
