package aster.amo.kytale.util

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * A thread-safe cooldown manager for tracking action cooldowns.
 *
 * Commonly used for command cooldowns, ability cooldowns, and rate limiting.
 *
 * Example:
 * ```kotlin
 * val teleportCooldown = Cooldown<UUID>(30.seconds)
 *
 * fun onTeleportCommand(playerId: UUID) {
 *     if (teleportCooldown.isOnCooldown(playerId)) {
 *         val remaining = teleportCooldown.remaining(playerId)
 *         player.sendMessage("Wait ${remaining.inWholeSeconds}s")
 *         return
 *     }
 *
 *     teleportCooldown.trigger(playerId)
 *     // Perform teleport
 * }
 * ```
 *
 * @param K the key type (usually UUID for player-specific cooldowns)
 * @property duration the cooldown duration
 * @property timeSource the time source for measuring elapsed time
 */
class Cooldown<K : Any>(
    private val duration: Duration,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {
    private val cooldowns = ConcurrentHashMap<K, TimeMark>()

    /**
     * Checks if the given key is currently on cooldown.
     *
     * @param key the key to check
     * @return true if on cooldown, false if ready
     */
    fun isOnCooldown(key: K): Boolean {
        val mark = cooldowns[key] ?: return false
        return mark.elapsedNow() < duration
    }

    /**
     * Checks if the given key is ready (not on cooldown).
     *
     * @param key the key to check
     * @return true if ready, false if on cooldown
     */
    fun isReady(key: K): Boolean = !isOnCooldown(key)

    /**
     * Gets the remaining cooldown duration for a key.
     *
     * @param key the key to check
     * @return the remaining duration, or [Duration.ZERO] if ready
     */
    fun remaining(key: K): Duration {
        val mark = cooldowns[key] ?: return Duration.ZERO
        val elapsed = mark.elapsedNow()
        return if (elapsed >= duration) Duration.ZERO else duration - elapsed
    }

    /**
     * Triggers the cooldown for the given key.
     *
     * @param key the key to put on cooldown
     */
    fun trigger(key: K) {
        cooldowns[key] = timeSource.markNow()
    }

    /**
     * Resets (removes) the cooldown for the given key.
     *
     * @param key the key to reset
     */
    fun reset(key: K) {
        cooldowns.remove(key)
    }

    /**
     * Checks if ready and triggers cooldown in one atomic operation.
     *
     * Returns true if the action was allowed (was ready), false if on cooldown.
     *
     * @param key the key to check and trigger
     * @return true if the action was allowed
     */
    fun tryTrigger(key: K): Boolean {
        return if (isReady(key)) {
            trigger(key)
            true
        } else {
            false
        }
    }

    /**
     * Executes an action if not on cooldown, otherwise runs the fallback.
     *
     * Example:
     * ```kotlin
     * cooldown.ifReady(playerId,
     *     action = { performAction() },
     *     fallback = { remaining -> sendMessage("Wait $remaining") }
     * )
     * ```
     *
     * @param key the key to check
     * @param action the action to run if ready
     * @param fallback the action to run if on cooldown (receives remaining time)
     */
    inline fun ifReady(
        key: K,
        action: () -> Unit,
        fallback: (remaining: Duration) -> Unit = {}
    ) {
        if (tryTrigger(key)) {
            action()
        } else {
            fallback(remaining(key))
        }
    }

    /**
     * Clears all cooldowns.
     */
    fun clear() {
        cooldowns.clear()
    }

    /**
     * Removes expired cooldowns to free memory.
     *
     * Call periodically for long-lived cooldown managers.
     */
    fun cleanup() {
        cooldowns.entries.removeIf { (_, mark) ->
            mark.elapsedNow() >= duration
        }
    }

    /**
     * Gets the number of active cooldowns.
     */
    val size: Int get() = cooldowns.size
}

/**
 * A multi-action cooldown manager for tracking different cooldown types per key.
 *
 * Example:
 * ```kotlin
 * val cooldowns = MultiCooldown<UUID>()
 *
 * // Different cooldowns for different actions
 * cooldowns.trigger(playerId, "teleport", 30.seconds)
 * cooldowns.trigger(playerId, "heal", 60.seconds)
 *
 * if (cooldowns.isReady(playerId, "teleport")) {
 *     // Can teleport
 * }
 * ```
 *
 * @param K the key type
 */
class MultiCooldown<K : Any>(
    private val timeSource: TimeSource = TimeSource.Monotonic
) {
    private data class CooldownEntry(val mark: TimeMark, val duration: Duration)

    private val cooldowns = ConcurrentHashMap<Pair<K, String>, CooldownEntry>()

    /**
     * Checks if the given key is on cooldown for the specified action.
     *
     * @param key the key to check
     * @param action the action identifier
     * @return true if on cooldown
     */
    fun isOnCooldown(key: K, action: String): Boolean {
        val entry = cooldowns[key to action] ?: return false
        return entry.mark.elapsedNow() < entry.duration
    }

    /**
     * Checks if the given key is ready for the specified action.
     *
     * @param key the key to check
     * @param action the action identifier
     * @return true if ready
     */
    fun isReady(key: K, action: String): Boolean = !isOnCooldown(key, action)

    /**
     * Gets the remaining cooldown duration.
     *
     * @param key the key to check
     * @param action the action identifier
     * @return the remaining duration
     */
    fun remaining(key: K, action: String): Duration {
        val entry = cooldowns[key to action] ?: return Duration.ZERO
        val elapsed = entry.mark.elapsedNow()
        return if (elapsed >= entry.duration) Duration.ZERO else entry.duration - elapsed
    }

    /**
     * Triggers a cooldown for the given key and action.
     *
     * @param key the key to put on cooldown
     * @param action the action identifier
     * @param duration the cooldown duration
     */
    fun trigger(key: K, action: String, duration: Duration) {
        cooldowns[key to action] = CooldownEntry(timeSource.markNow(), duration)
    }

    /**
     * Resets the cooldown for the given key and action.
     *
     * @param key the key to reset
     * @param action the action identifier
     */
    fun reset(key: K, action: String) {
        cooldowns.remove(key to action)
    }

    /**
     * Resets all cooldowns for the given key.
     *
     * @param key the key to reset
     */
    fun resetAll(key: K) {
        cooldowns.keys.removeIf { it.first == key }
    }

    /**
     * Checks if ready and triggers cooldown atomically.
     *
     * @param key the key to check
     * @param action the action identifier
     * @param duration the cooldown duration
     * @return true if the action was allowed
     */
    fun tryTrigger(key: K, action: String, duration: Duration): Boolean {
        return if (isReady(key, action)) {
            trigger(key, action, duration)
            true
        } else {
            false
        }
    }

    /**
     * Clears all cooldowns.
     */
    fun clear() {
        cooldowns.clear()
    }

    /**
     * Removes expired cooldowns.
     */
    fun cleanup() {
        cooldowns.entries.removeIf { (_, entry) ->
            entry.mark.elapsedNow() >= entry.duration
        }
    }
}

/**
 * Creates a player cooldown manager.
 *
 * @param duration the cooldown duration
 * @return a Cooldown instance keyed by UUID
 */
fun playerCooldown(duration: Duration): Cooldown<UUID> = Cooldown(duration)

/**
 * Creates a string-keyed cooldown manager.
 *
 * @param duration the cooldown duration
 * @return a Cooldown instance keyed by String
 */
fun namedCooldown(duration: Duration): Cooldown<String> = Cooldown(duration)
