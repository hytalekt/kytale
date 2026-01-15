package aster.amo.kytale.util

/**
 * LRU (Least Recently Used) cache with size limit.
 *
 * @param K the key type
 * @param V the value type
 * @property maxSize maximum number of entries
 */
class LRUCache<K : Any, V : Any>(
    private val maxSize: Int
) {
    @PublishedApi
    internal val cache = object : LinkedHashMap<K, V>(maxSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > maxSize
        }
    }

    @PublishedApi
    internal val lock = Any()

    @Synchronized
    fun get(key: K): V? = cache[key]

    inline fun getOrPut(key: K, compute: () -> V): V = synchronized(lock) {
        cache[key] ?: compute().also { cache[key] = it }
    }

    @Synchronized
    fun put(key: K, value: V): V? = cache.put(key, value)

    @Synchronized
    fun remove(key: K): V? = cache.remove(key)

    @Synchronized
    fun contains(key: K): Boolean = cache.containsKey(key)

    @Synchronized
    fun clear() = cache.clear()

    @Synchronized
    fun entries(): Map<K, V> = cache.toMap()

    val size: Int @Synchronized get() = cache.size
}

/**
 * Creates an LRU cache.
 *
 * @param maxSize maximum size
 */
fun <K : Any, V : Any> lruCache(maxSize: Int): LRUCache<K, V> = LRUCache(maxSize)
