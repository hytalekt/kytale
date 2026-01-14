package io.github.hytalekt.kytale.util

/**
 * DSL for scheduling tasks.
 *
 * Provides convenient ways to schedule delayed and repeating tasks.
 *
 * Example usage:
 * ```
 * schedule {
 *     delay(20) // 1 second (20 ticks)
 *     run {
 *         player.sendMessage("Delayed message!")
 *     }
 * }
 *
 * schedule {
 *     repeat(100) // Every 5 seconds
 *     run {
 *         // Repeating task
 *     }
 * }
 *
 * schedule {
 *     delay(20)
 *     repeat(100)
 *     run {
 *         // Delayed + repeating
 *     }
 * }
 * ```
 */
@DslMarker
annotation class SchedulerDsl

/**
 * Builder for scheduled tasks
 */
@SchedulerDsl
class ScheduleBuilder {
    private var delayTicks: Long = 0
    private var repeatTicks: Long = 0
    private var async: Boolean = false
    private var task: (() -> Unit)? = null

    /**
     * Sets the delay before first execution (in ticks, 20 ticks = 1 second)
     */
    fun delay(ticks: Long) {
        delayTicks = ticks
    }

    /**
     * Sets the delay before first execution in seconds
     */
    fun delaySeconds(seconds: Long) {
        delayTicks = seconds * 20
    }

    /**
     * Sets the repeat interval (in ticks, 20 ticks = 1 second)
     */
    fun repeat(ticks: Long) {
        repeatTicks = ticks
    }

    /**
     * Sets the repeat interval in seconds
     */
    fun repeatSeconds(seconds: Long) {
        repeatTicks = seconds * 20
    }

    /**
     * Marks the task to run asynchronously
     */
    fun async() {
        async = true
    }

    /**
     * Sets the task to run
     */
    fun run(block: () -> Unit) {
        task = block
    }

    /**
     * Executes the scheduled task
     */
    fun execute(): ScheduledTask {
        require(task != null) { "Task must be set" }
        // TODO: Implement actual task scheduling
        return ScheduledTask()
    }
}

/**
 * Represents a scheduled task that can be cancelled
 */
class ScheduledTask {
    /**
     * Cancels the scheduled task
     */
    fun cancel() {
        // TODO: Implement cancellation
    }

    /**
     * Checks if the task is cancelled
     */
    val isCancelled: Boolean
        get() = TODO("Check if cancelled")
}

/**
 * DSL function to schedule a task
 */
fun schedule(configure: ScheduleBuilder.() -> Unit): ScheduledTask {
    val builder = ScheduleBuilder()
    builder.configure()
    return builder.execute()
}

/**
 * Shorthand to run a task after a delay
 */
fun runLater(
    ticks: Long,
    task: () -> Unit,
): ScheduledTask =
    schedule {
        delay(ticks)
        run(task)
    }

/**
 * Shorthand to run a repeating task
 */
fun runRepeating(
    intervalTicks: Long,
    task: () -> Unit,
): ScheduledTask =
    schedule {
        repeat(intervalTicks)
        run(task)
    }

/**
 * Shorthand to run an async task
 */
fun runAsync(task: () -> Unit): ScheduledTask =
    schedule {
        async()
        run(task)
    }
