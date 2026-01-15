package aster.amo.kytale.util

import java.util.concurrent.ConcurrentHashMap

/**
 * Computed cache that lazily loads values on demand.
 *
 * @param K the key type
 * @param V the value type
 * @property loader function to load values
 */
class LoadingCache<K : Any, V : Any>(
    private val loader: (K) -> V
) {
    private val cache = ConcurrentHashMap<K, V>()

    /**
     * Gets a value, loading it if not present.
     *
     * @param key the key
     * @return the cached or loaded value
     */
    fun get(key: K): V = cache.getOrPut(key) { loader(key) }

    /**
     * Gets a value without loading, or null if not cached.
     *
     * @param key the key
     * @return the cached value or null
     */
    fun getIfPresent(key: K): V? = cache[key]

    /**
     * Forces a value to be reloaded.
     *
     * @param key the key to refresh
     * @return the newly loaded value
     */
    fun refresh(key: K): V {
        val value = loader(key)
        cache[key] = value
        return value
    }

    /**
     * Stores a value directly.
     *
     * @param key the key
     * @param value the value
     */
    fun put(key: K, value: V) {
        cache[key] = value
    }

    /**
     * Invalidates a cache entry.
     *
     * @param key the key to invalidate
     */
    fun invalidate(key: K) {
        cache.remove(key)
    }

    /**
     * Clears all entries.
     */
    fun clear() = cache.clear()

    val size: Int get() = cache.size
}

/**
 * Creates a loading cache.
 *
 * @param loader the value loader function
 */
fun <K : Any, V : Any> loadingCache(loader: (K) -> V): LoadingCache<K, V> = LoadingCache(loader)
