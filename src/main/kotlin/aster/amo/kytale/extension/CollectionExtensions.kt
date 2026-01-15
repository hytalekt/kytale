package aster.amo.kytale.extension

import java.util.concurrent.ConcurrentHashMap

/**
 * Extension functions for collections and functional programming.
 *
 * These extensions provide Kotlin-idiomatic utilities for working with
 * collections in game plugin contexts.
 */

/**
 * Returns a random element from this collection, or null if empty.
 *
 * @return a random element or null
 */
fun <T> Collection<T>.randomOrNull(): T? {
    if (isEmpty()) return null
    return elementAt((Math.random() * size).toInt())
}

/**
 * Returns a random element from this list, or null if empty.
 *
 * More efficient than the Collection version for random access lists.
 *
 * @return a random element or null
 */
fun <T> List<T>.randomOrNull(): T? {
    if (isEmpty()) return null
    return this[(Math.random() * size).toInt()]
}

/**
 * Returns multiple random elements from this collection.
 *
 * @param count the number of elements to return
 * @return a list of random elements (may contain duplicates)
 */
fun <T> Collection<T>.randomElements(count: Int): List<T> {
    if (isEmpty()) return emptyList()
    return (0 until count).map { elementAt((Math.random() * size).toInt()) }
}

/**
 * Returns multiple unique random elements from this collection.
 *
 * @param count the number of elements to return
 * @return a list of unique random elements
 */
fun <T> Collection<T>.randomDistinct(count: Int): List<T> {
    if (isEmpty() || count <= 0) return emptyList()
    return shuffled().take(count.coerceAtMost(size))
}

/**
 * Finds the element with the maximum value according to the selector, or null if empty.
 *
 * @param selector function to extract the comparable value
 * @return the element with the maximum value, or null
 */
inline fun <T, R : Comparable<R>> Iterable<T>.maxByOrNull(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var maxElement = iterator.next()
    var maxValue = selector(maxElement)
    while (iterator.hasNext()) {
        val element = iterator.next()
        val value = selector(element)
        if (value > maxValue) {
            maxElement = element
            maxValue = value
        }
    }
    return maxElement
}

/**
 * Finds the element with the minimum value according to the selector, or null if empty.
 *
 * @param selector function to extract the comparable value
 * @return the element with the minimum value, or null
 */
inline fun <T, R : Comparable<R>> Iterable<T>.minByOrNull(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElement = iterator.next()
    var minValue = selector(minElement)
    while (iterator.hasNext()) {
        val element = iterator.next()
        val value = selector(element)
        if (value < minValue) {
            minElement = element
            minValue = value
        }
    }
    return minElement
}

/**
 * Partitions this collection into chunks of the specified size.
 *
 * @param size the chunk size
 * @return a list of chunks
 */
fun <T> Collection<T>.chunkedList(size: Int): List<List<T>> {
    return chunked(size)
}

/**
 * Returns true if all elements satisfy the predicate.
 *
 * Returns true for an empty collection.
 *
 * @param predicate the condition to test
 * @return true if all elements match
 */
inline fun <T> Collection<T>.allMatch(predicate: (T) -> Boolean): Boolean {
    return all(predicate)
}

/**
 * Returns true if any element satisfies the predicate.
 *
 * Returns false for an empty collection.
 *
 * @param predicate the condition to test
 * @return true if any element matches
 */
inline fun <T> Collection<T>.anyMatch(predicate: (T) -> Boolean): Boolean {
    return any(predicate)
}

/**
 * Returns true if no elements satisfy the predicate.
 *
 * Returns true for an empty collection.
 *
 * @param predicate the condition to test
 * @return true if no elements match
 */
inline fun <T> Collection<T>.noneMatch(predicate: (T) -> Boolean): Boolean {
    return none(predicate)
}

/**
 * Applies an action to each element with its index.
 *
 * @param action the action to apply
 */
inline fun <T> Iterable<T>.forEachIndexed(action: (index: Int, T) -> Unit) {
    var index = 0
    for (element in this) action(index++, element)
}

/**
 * Creates a mutable map from this collection using the key selector.
 *
 * @param keySelector function to extract the key
 * @return a mutable map
 */
inline fun <T, K> Iterable<T>.toMutableMapBy(keySelector: (T) -> K): MutableMap<K, T> {
    val map = mutableMapOf<K, T>()
    for (element in this) {
        map[keySelector(element)] = element
    }
    return map
}

/**
 * Creates a concurrent map from this collection using the key selector.
 *
 * @param keySelector function to extract the key
 * @return a concurrent hash map
 */
inline fun <T, K> Iterable<T>.toConcurrentMapBy(keySelector: (T) -> K): ConcurrentHashMap<K, T> {
    val map = ConcurrentHashMap<K, T>()
    for (element in this) {
        map[keySelector(element)] = element
    }
    return map
}

/**
 * Groups elements by key and counts occurrences.
 *
 * @param keySelector function to extract the grouping key
 * @return a map of keys to counts
 */
inline fun <T, K> Iterable<T>.countBy(keySelector: (T) -> K): Map<K, Int> {
    val counts = mutableMapOf<K, Int>()
    for (element in this) {
        val key = keySelector(element)
        counts[key] = counts.getOrDefault(key, 0) + 1
    }
    return counts
}

/**
 * Returns elements that appear more than once.
 *
 * @return a set of duplicate elements
 */
fun <T> Iterable<T>.duplicates(): Set<T> {
    val seen = mutableSetOf<T>()
    val duplicates = mutableSetOf<T>()
    for (element in this) {
        if (!seen.add(element)) {
            duplicates.add(element)
        }
    }
    return duplicates
}

/**
 * Returns the second element, or null if the collection has fewer than 2 elements.
 *
 * @return the second element or null
 */
fun <T> List<T>.secondOrNull(): T? = getOrNull(1)

/**
 * Returns the third element, or null if the collection has fewer than 3 elements.
 *
 * @return the third element or null
 */
fun <T> List<T>.thirdOrNull(): T? = getOrNull(2)

/**
 * Replaces the first element matching the predicate.
 *
 * @param predicate the condition to match
 * @param replacement the replacement element
 * @return a new list with the replacement
 */
inline fun <T> List<T>.replaceFirst(predicate: (T) -> Boolean, replacement: T): List<T> {
    val result = toMutableList()
    val index = indexOfFirst(predicate)
    if (index >= 0) {
        result[index] = replacement
    }
    return result
}

/**
 * Replaces all elements matching the predicate.
 *
 * @param predicate the condition to match
 * @param replacement the replacement element
 * @return a new list with replacements
 */
inline fun <T> List<T>.replaceAll(predicate: (T) -> Boolean, replacement: T): List<T> {
    return map { if (predicate(it)) replacement else it }
}

/**
 * Swaps two elements in a mutable list.
 *
 * @param i the first index
 * @param j the second index
 */
fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

/**
 * Returns the element if present, otherwise computes and stores the default.
 *
 * Thread-safe for ConcurrentHashMap.
 *
 * @param key the key
 * @param defaultValue function to compute the default
 * @return the existing or computed value
 */
inline fun <K, V> MutableMap<K, V>.getOrCompute(key: K, defaultValue: () -> V): V {
    return get(key) ?: defaultValue().also { put(key, it) }
}

/**
 * Removes and returns the first element, or null if empty.
 *
 * @return the removed element or null
 */
fun <T> MutableList<T>.removeFirstOrNull(): T? {
    return if (isEmpty()) null else removeAt(0)
}

/**
 * Removes and returns the last element, or null if empty.
 *
 * @return the removed element or null
 */
fun <T> MutableList<T>.removeLastOrNull(): T? {
    return if (isEmpty()) null else removeAt(lastIndex)
}
