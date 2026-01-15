package aster.amo.hexweave.dsl.tasks

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.kytale.dsl.scheduleRepeating
import aster.amo.kytale.extension.info
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.time.Duration

/**
 * DSL scope for scheduling tasks.
 *
 * ```kotlin
 * tasks {
 *     repeating("heartbeat", every = 60.seconds) {
 *         logger.info { "Still alive!" }
 *     }
 * }
 * ```
 */
@HexweaveDsl
class HexweaveTasksScope internal constructor(
    private val plugin: JavaPlugin,
    private val scope: CoroutineScope
) {
    private val jobs = mutableListOf<Job>()

    val logger get() = plugin.logger

    /**
     * Schedule a repeating task.
     *
     * @param name Task name for logging
     * @param delay Initial delay before first execution
     * @param every Interval between executions
     * @param block The task to run
     */
    fun repeating(
        name: String,
        delay: Duration = Duration.ZERO,
        every: Duration,
        block: suspend () -> Unit
    ) {
        val job = scope.scheduleRepeating(delay, every, block)
        jobs += job
        logger.info { "[Hexweave] scheduled repeating task '$name'" }
    }
}
