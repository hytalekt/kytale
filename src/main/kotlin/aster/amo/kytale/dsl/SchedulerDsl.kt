package aster.amo.kytale.dsl

import aster.amo.kytale.coroutines.HytaleDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Schedules a task to run after a delay.
 *
 * Example:
 * ```kotlin
 * schedule(delay = 5.seconds) {
 *     logger.info { "5 seconds have passed!" }
 * }
 * ```
 *
 * @param delay the delay before execution
 * @param block the task to execute
 * @return a Job that can be used to cancel the scheduled task
 */
fun CoroutineScope.schedule(
    delay: Duration,
    block: suspend () -> Unit
): Job = launch(HytaleDispatchers.Async) {
    delay(delay)
    block()
}

/**
 * Schedules a repeating task.
 *
 * The task runs repeatedly with the specified period until cancelled.
 *
 * Example:
 * ```kotlin
 * val job = scheduleRepeating(
 *     delay = 1.seconds,
 *     period = 30.seconds
 * ) {
 *     saveAllData()
 * }
 *
 * // Later, to cancel:
 * job.cancel()
 * ```
 *
 * @param delay the initial delay before the first execution
 * @param period the interval between executions
 * @param block the task to execute
 * @return a Job that can be used to cancel the repeating task
 */
fun CoroutineScope.scheduleRepeating(
    delay: Duration = Duration.ZERO,
    period: Duration,
    block: suspend () -> Unit
): Job = launch(HytaleDispatchers.Async) {
    delay(delay)
    while (isActive) {
        block()
        delay(period)
    }
}

/**
 * Schedules a task to run on the main thread after a delay.
 *
 * @param delay the delay before execution
 * @param block the task to execute on the main thread
 * @return a Job that can be used to cancel the scheduled task
 */
fun CoroutineScope.scheduleOnMain(
    delay: Duration,
    block: suspend () -> Unit
): Job = launch(HytaleDispatchers.Async) {
    delay(delay)
    kotlinx.coroutines.withContext(HytaleDispatchers.Main) {
        block()
    }
}

/**
 * Schedules a repeating task on the main thread.
 *
 * @param delay the initial delay before the first execution
 * @param period the interval between executions
 * @param block the task to execute on the main thread
 * @return a Job that can be used to cancel the repeating task
 */
fun CoroutineScope.scheduleRepeatingOnMain(
    delay: Duration = Duration.ZERO,
    period: Duration,
    block: suspend () -> Unit
): Job = launch(HytaleDispatchers.Async) {
    delay(delay)
    while (isActive) {
        kotlinx.coroutines.withContext(HytaleDispatchers.Main) {
            block()
        }
        delay(period)
    }
}

/**
 * Schedules a task using tick-based timing.
 *
 * Hytale runs at 30 TPS by default, so 1 tick = ~33.33ms.
 *
 * Example:
 * ```kotlin
 * scheduleInTicks(delay = 60) { // 2 seconds at 30 TPS
 *     performAction()
 * }
 * ```
 *
 * @param delay the delay in ticks
 * @param tps the server tick rate (default: 30)
 * @param block the task to execute
 * @return a Job that can be used to cancel the scheduled task
 */
fun CoroutineScope.scheduleInTicks(
    delay: Int,
    tps: Int = 30,
    block: suspend () -> Unit
): Job {
    val delayMs = (delay * 1000L / tps).milliseconds
    return schedule(delayMs, block)
}

/**
 * Schedules a repeating task using tick-based timing.
 *
 * @param delay the initial delay in ticks
 * @param period the interval in ticks between executions
 * @param tps the server tick rate (default: 30)
 * @param block the task to execute
 * @return a Job that can be used to cancel the repeating task
 */
fun CoroutineScope.scheduleRepeatingInTicks(
    delay: Int = 0,
    period: Int,
    tps: Int = 30,
    block: suspend () -> Unit
): Job {
    val delayMs = (delay * 1000L / tps).milliseconds
    val periodMs = (period * 1000L / tps).milliseconds
    return scheduleRepeating(delayMs, periodMs, block)
}

/**
 * Runs a task immediately on the async dispatcher.
 *
 * @param block the task to execute
 * @return a Job representing the running task
 */
fun CoroutineScope.async(block: suspend () -> Unit): Job =
    launch(HytaleDispatchers.Async) { block() }

/**
 * Runs a task immediately on the main thread.
 *
 * @param block the task to execute
 * @return a Job representing the running task
 */
fun CoroutineScope.sync(block: suspend () -> Unit): Job =
    launch(HytaleDispatchers.Main) { block() }

/**
 * Converts ticks to a Duration.
 *
 * @param tps the tick rate (default: 30)
 * @return the duration equivalent to these ticks
 */
fun Int.ticks(tps: Int = 30): Duration = (this * 1000L / tps).milliseconds

/**
 * Converts Duration to ticks.
 *
 * @param tps the tick rate (default: 30)
 * @return the number of ticks equivalent to this duration
 */
fun Duration.toTicks(tps: Int = 30): Int = (this.inWholeMilliseconds * tps / 1000).toInt()
