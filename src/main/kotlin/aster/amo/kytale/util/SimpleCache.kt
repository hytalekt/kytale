package aster.amo.kytale.util

import java.util.concurrent.ConcurrentHashMap

/**
 * Simple non-expiring concurrent cache.
 *
 * @param K the key type
 * @param V the value type
 */
class SimpleCache<K : Any, V : Any> {
    @PublishedApi
    internal val cache = ConcurrentHashMap<K, V>()

    fun get(key: K): V? = cache[key]

    inline fun getOrPut(key: K, compute: () -> V): V = cache.getOrPut(key, compute)

    fun put(key: K, value: V): V? = cache.put(key, value)

    fun remove(key: K): V? = cache.remove(key)

    fun contains(key: K): Boolean = cache.containsKey(key)

    fun clear() = cache.clear()

    fun entries(): Map<K, V> = cache.toMap()

    fun keys(): Set<K> = cache.keys.toSet()

    fun values(): Collection<V> = cache.values.toList()

    val size: Int get() = cache.size

    val isEmpty: Boolean get() = cache.isEmpty()

    val isNotEmpty: Boolean get() = cache.isNotEmpty()
}

/**
 * Creates a simple cache.
 */
fun <K : Any, V : Any> simpleCache(): SimpleCache<K, V> = SimpleCache()
