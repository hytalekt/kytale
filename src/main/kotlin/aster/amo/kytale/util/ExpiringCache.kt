package aster.amo.kytale.util

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Thread-safe cache with automatic expiration.
 *
 * Example:
 * ```kotlin
 * val playerDataCache = ExpiringCache<UUID, PlayerData>(5.minutes)
 *
 * fun getPlayerData(id: UUID): PlayerData {
 *     return playerDataCache.getOrPut(id) {
 *         loadFromDatabase(id)
 *     }
 * }
 * ```
 *
 * @param K the key type
 * @param V the value type
 * @property ttl time-to-live for cache entries
 * @property timeSource the time source for expiration
 */
class ExpiringCache<K : Any, V : Any>(
    private val ttl: Duration,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {
    @PublishedApi
    internal data class Entry<V>(val value: V, val created: TimeMark)

    @PublishedApi
    internal val cache = ConcurrentHashMap<K, Entry<V>>()

    /**
     * Gets a value from the cache, or null if not present or expired.
     *
     * @param key the key
     * @return the cached value or null
     */
    fun get(key: K): V? {
        val entry = cache[key] ?: return null
        return if (entry.created.elapsedNow() < ttl) {
            entry.value
        } else {
            cache.remove(key)
            null
        }
    }

    /**
     * Gets a value from the cache, or computes and stores a new value.
     *
     * @param key the key
     * @param compute function to compute the value if missing
     * @return the cached or computed value
     */
    inline fun getOrPut(key: K, compute: () -> V): V {
        val existing = get(key)
        if (existing != null) return existing

        val newValue = compute()
        put(key, newValue)
        return newValue
    }

    /**
     * Stores a value in the cache.
     *
     * @param key the key
     * @param value the value to store
     */
    fun put(key: K, value: V) {
        cache[key] = Entry(value, timeSource.markNow())
    }

    /**
     * Removes a value from the cache.
     *
     * @param key the key to remove
     * @return the removed value, or null
     */
    fun remove(key: K): V? = cache.remove(key)?.value

    /**
     * Checks if the cache contains a non-expired entry.
     *
     * @param key the key to check
     * @return true if present and not expired
     */
    fun contains(key: K): Boolean = get(key) != null

    /**
     * Invalidates (removes) a cache entry.
     *
     * @param key the key to invalidate
     */
    fun invalidate(key: K) {
        cache.remove(key)
    }

    /**
     * Invalidates all entries matching the predicate.
     *
     * @param predicate the condition for removal
     */
    inline fun invalidateIf(crossinline predicate: (K) -> Boolean) {
        cache.keys.removeIf { predicate(it) }
    }

    /**
     * Clears all cache entries.
     */
    fun clear() {
        cache.clear()
    }

    /**
     * Removes expired entries from the cache.
     *
     * Call periodically to prevent memory buildup.
     */
    fun cleanup() {
        cache.entries.removeIf { (_, entry) ->
            entry.created.elapsedNow() >= ttl
        }
    }

    /**
     * Gets all non-expired entries.
     *
     * @return a map of valid entries
     */
    fun entries(): Map<K, V> {
        cleanup()
        return cache.mapValues { it.value.value }
    }

    /**
     * Gets the number of entries (including potentially expired ones).
     */
    val size: Int get() = cache.size
}

/**
 * Creates an expiring cache.
 *
 * @param ttl time-to-live for entries
 */
fun <K : Any, V : Any> expiringCache(ttl: Duration): ExpiringCache<K, V> = ExpiringCache(ttl)
