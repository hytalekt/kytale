package aster.amo.kytale.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Provides coroutine dispatchers tailored for Hytale server operations.
 *
 * Use these dispatchers to ensure coroutines run on the appropriate threads:
 * - [Main] for operations requiring the main server thread
 * - [Async] for background operations that don't need the main thread
 * - [IO] for I/O-bound operations like file or network access
 *
 * Example usage:
 * ```kotlin
 * launch(HytaleDispatchers.Async) {
 *     val data = loadDataFromDatabase()
 *     withContext(HytaleDispatchers.Main) {
 *         applyDataToWorld(data)
 *     }
 * }
 * ```
 */
object HytaleDispatchers {

    @Volatile
    private var mainExecutor: Executor? = null

    /**
     * Dispatcher for the main server thread.
     *
     * Use this dispatcher when you need to interact with game state that
     * must be accessed from the main thread, such as entity manipulation
     * or world modifications.
     *
     * Falls back to [Dispatchers.Default] if the main executor has not
     * been configured.
     */
    val Main: CoroutineDispatcher
        get() = mainExecutor?.asCoroutineDispatcher() ?: Dispatchers.Default

    /**
     * Dispatcher for asynchronous background operations.
     *
     * Uses a cached thread pool suitable for CPU-bound tasks that can
     * run in parallel without blocking the main thread.
     */
    val Async: CoroutineDispatcher = Dispatchers.Default

    /**
     * Dispatcher for I/O-bound operations.
     *
     * Optimized for blocking I/O operations such as file access,
     * database queries, and network requests.
     */
    val IO: CoroutineDispatcher = Dispatchers.IO

    /**
     * A dispatcher backed by a single-threaded executor.
     *
     * Useful for operations that require sequential execution guarantees
     * without blocking the main thread.
     */
    val Single: CoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "HyKot-Single").apply {
                isDaemon = true
            }
        }.asCoroutineDispatcher()
    }

    /**
     * Configures the main thread executor.
     *
     * This should be called during server initialization to provide
     * the executor that schedules tasks on the main server thread.
     *
     * @param executor the executor for the main server thread
     */
    @JvmStatic
    fun setMainExecutor(executor: Executor) {
        mainExecutor = executor
    }

    /**
     * Creates a dispatcher that runs on a specific world's thread.
     *
     * @param worldExecutor the executor for the world's thread
     * @return a dispatcher bound to the world's execution context
     */
    @JvmStatic
    fun forWorld(worldExecutor: Executor): CoroutineDispatcher {
        return worldExecutor.asCoroutineDispatcher()
    }
}
