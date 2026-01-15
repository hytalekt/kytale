package aster.amo.kytale.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

/**
 * Converts this [CompletableFuture] to a suspending function result.
 *
 * Suspends the coroutine until the future completes, then returns
 * the result or throws the exception if the future failed.
 *
 * Example:
 * ```kotlin
 * val result = someAsyncJavaMethod().await()
 * ```
 *
 * @return the result of the completed future
 */
suspend fun <T> CompletableFuture<T>.await(): T = this.await()

/**
 * Executes the given block on the main server thread.
 *
 * Suspends the current coroutine and resumes on the main thread,
 * then returns to the original context after the block completes.
 *
 * Example:
 * ```kotlin
 * launch(HytaleDispatchers.Async) {
 *     val data = fetchData()
 *     onMainThread {
 *         world.applyData(data)
 *     }
 * }
 * ```
 *
 * @param block the code to execute on the main thread
 * @return the result of the block
 */
suspend fun <T> onMainThread(block: suspend CoroutineScope.() -> T): T {
    return withContext(HytaleDispatchers.Main, block)
}

/**
 * Executes the given block on the async dispatcher.
 *
 * Suspends the current coroutine and resumes on a background thread,
 * then returns to the original context after the block completes.
 *
 * Example:
 * ```kotlin
 * onMainThread {
 *     val result = onAsync {
 *         performExpensiveCalculation()
 *     }
 *     displayResult(result)
 * }
 * ```
 *
 * @param block the code to execute asynchronously
 * @return the result of the block
 */
suspend fun <T> onAsync(block: suspend CoroutineScope.() -> T): T {
    return withContext(HytaleDispatchers.Async, block)
}

/**
 * Executes the given block on the I/O dispatcher.
 *
 * Optimized for blocking I/O operations such as file access,
 * database queries, and network requests.
 *
 * Example:
 * ```kotlin
 * val fileContent = onIO {
 *     File("config.json").readText()
 * }
 * ```
 *
 * @param block the code to execute on the I/O dispatcher
 * @return the result of the block
 */
suspend fun <T> onIO(block: suspend CoroutineScope.() -> T): T {
    return withContext(HytaleDispatchers.IO, block)
}

/**
 * Converts a suspend function to a [CompletableFuture].
 *
 * Useful for interoperability with Java code that expects
 * CompletableFuture return types.
 *
 * Example:
 * ```kotlin
 * fun loadDataAsync(): CompletableFuture<Data> = scope.asFuture {
 *     loadData()
 * }
 * ```
 *
 * @param context optional coroutine context override
 * @param block the suspend function to convert
 * @return a CompletableFuture that completes with the block's result
 */
fun <T> CoroutineScope.asFuture(
    context: CoroutineContext = HytaleDispatchers.Async,
    block: suspend CoroutineScope.() -> T
): CompletableFuture<T> = future(context, block = block)

/**
 * Launches an async operation and returns a [Deferred] result.
 *
 * The operation starts immediately and can be awaited later.
 *
 * Example:
 * ```kotlin
 * val deferred = scope.asyncOn(HytaleDispatchers.IO) {
 *     loadFromDatabase()
 * }
 * // Do other work...
 * val result = deferred.await()
 * ```
 *
 * @param context the coroutine context to run on
 * @param block the code to execute
 * @return a Deferred that completes with the block's result
 */
fun <T> CoroutineScope.asyncOn(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = async(context, block = block)
