package io.github.hytalekt.kytale.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class CacheTest : FunSpec({
    
    context("SimpleCache") {
        test("put and get") {
            val cache = simpleCache<String, Int>()
            
            cache.put("a", 1)
            
            cache.get("a") shouldBe 1
        }
        
        test("get returns null for missing key") {
            val cache = simpleCache<String, Int>()
            
            cache.get("missing").shouldBeNull()
        }
        
        test("getOrPut returns existing value") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            
            val result = cache.getOrPut("a") { 99 }
            
            result shouldBe 1
        }
        
        test("getOrPut computes and stores for missing key") {
            val cache = simpleCache<String, Int>()
            
            val result = cache.getOrPut("a") { 42 }
            
            result shouldBe 42
            cache.get("a") shouldBe 42
        }
        
        test("contains returns true for existing key") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            
            cache.contains("a") shouldBe true
        }
        
        test("contains returns false for missing key") {
            val cache = simpleCache<String, Int>()
            
            cache.contains("a") shouldBe false
        }
        
        test("remove removes entry") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            
            cache.remove("a")
            
            cache.contains("a") shouldBe false
        }
        
        test("clear removes all entries") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            cache.put("b", 2)
            
            cache.clear()
            
            cache.size shouldBe 0
        }
        
        test("size returns number of entries") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            cache.put("b", 2)
            
            cache.size shouldBe 2
        }
        
        test("isEmpty returns true when empty") {
            val cache = simpleCache<String, Int>()
            
            cache.isEmpty shouldBe true
        }
        
        test("isNotEmpty returns true when has entries") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)

            cache.isNotEmpty shouldBe true
        }

        test("entries returns copy of all entries") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            cache.put("b", 2)

            val entries = cache.entries()

            entries.size shouldBe 2
            entries["a"] shouldBe 1
            entries["b"] shouldBe 2
        }

        test("keys returns all keys") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            cache.put("b", 2)

            val keys = cache.keys()

            keys.size shouldBe 2
            keys.contains("a") shouldBe true
            keys.contains("b") shouldBe true
        }

        test("values returns all values") {
            val cache = simpleCache<String, Int>()
            cache.put("a", 1)
            cache.put("b", 2)

            val values = cache.values()

            values.size shouldBe 2
            values.contains(1) shouldBe true
            values.contains(2) shouldBe true
        }
    }

    context("LRUCache") {
        test("evicts oldest entry when at capacity") {
            val cache = lruCache<String, Int>(maxSize = 2)
            
            cache.put("a", 1)
            cache.put("b", 2)
            cache.put("c", 3) // Should evict "a"
            
            cache.contains("a") shouldBe false
            cache.contains("b") shouldBe true
            cache.contains("c") shouldBe true
        }
        
        test("access refreshes entry preventing eviction") {
            val cache = lruCache<String, Int>(maxSize = 2)
            
            cache.put("a", 1)
            cache.put("b", 2)
            cache.get("a") // Access "a" to refresh it
            cache.put("c", 3) // Should evict "b" (least recently used)
            
            cache.contains("a") shouldBe true
            cache.contains("b") shouldBe false
            cache.contains("c") shouldBe true
        }
        
        test("respects maxSize") {
            val cache = lruCache<String, Int>(maxSize = 3)

            cache.put("a", 1)
            cache.put("b", 2)
            cache.put("c", 3)
            cache.put("d", 4)

            cache.size shouldBe 3
        }

        test("getOrPut returns existing value") {
            val cache = lruCache<String, Int>(maxSize = 3)
            cache.put("a", 1)

            val result = cache.getOrPut("a") { 99 }

            result shouldBe 1
        }

        test("getOrPut computes and stores for missing key") {
            val cache = lruCache<String, Int>(maxSize = 3)

            val result = cache.getOrPut("a") { 42 }

            result shouldBe 42
            cache.get("a") shouldBe 42
        }

        test("remove removes entry") {
            val cache = lruCache<String, Int>(maxSize = 3)
            cache.put("a", 1)

            val removed = cache.remove("a")

            removed shouldBe 1
            cache.contains("a") shouldBe false
        }

        test("remove returns null for missing key") {
            val cache = lruCache<String, Int>(maxSize = 3)

            cache.remove("missing").shouldBeNull()
        }

        test("clear removes all entries") {
            val cache = lruCache<String, Int>(maxSize = 3)
            cache.put("a", 1)
            cache.put("b", 2)

            cache.clear()

            cache.size shouldBe 0
            cache.contains("a") shouldBe false
        }

        test("entries returns copy of all entries") {
            val cache = lruCache<String, Int>(maxSize = 3)
            cache.put("a", 1)
            cache.put("b", 2)

            val entries = cache.entries()

            entries.size shouldBe 2
            entries["a"] shouldBe 1
            entries["b"] shouldBe 2
        }
    }

    context("LoadingCache") {
        test("loads value on first access") {
            var loadCount = 0
            val cache = loadingCache<String, Int> { key ->
                loadCount++
                key.length
            }
            
            val result = cache.get("hello")
            
            result shouldBe 5
            loadCount shouldBe 1
        }
        
        test("returns cached value on subsequent access") {
            var loadCount = 0
            val cache = loadingCache<String, Int> { key ->
                loadCount++
                key.length
            }
            
            cache.get("hello")
            cache.get("hello")
            
            loadCount shouldBe 1
        }
        
        test("getIfPresent returns null for uncached key") {
            val cache = loadingCache<String, Int> { it.length }
            
            cache.getIfPresent("hello").shouldBeNull()
        }
        
        test("getIfPresent returns cached value") {
            val cache = loadingCache<String, Int> { it.length }
            cache.get("hello")
            
            cache.getIfPresent("hello") shouldBe 5
        }
        
        test("refresh reloads value") {
            var loadCount = 0
            val cache = loadingCache<String, Int> { key ->
                loadCount++
                key.length + loadCount
            }
            
            cache.get("hello") // First load
            cache.refresh("hello") // Force reload
            
            loadCount shouldBe 2
            cache.get("hello") shouldBe 7 // 5 + 2 (second load)
        }
        
        test("invalidate removes entry") {
            val cache = loadingCache<String, Int> { it.length }
            cache.get("hello")

            cache.invalidate("hello")

            cache.getIfPresent("hello").shouldBeNull()
        }

        test("put stores value directly without loading") {
            var loadCount = 0
            val cache = loadingCache<String, Int> {
                loadCount++
                it.length
            }

            cache.put("hello", 99)

            cache.get("hello") shouldBe 99
            loadCount shouldBe 0
        }

        test("clear removes all entries") {
            val cache = loadingCache<String, Int> { it.length }
            cache.get("hello")
            cache.get("world")

            cache.clear()

            cache.getIfPresent("hello").shouldBeNull()
            cache.getIfPresent("world").shouldBeNull()
            cache.size shouldBe 0
        }

        test("size returns number of cached entries") {
            val cache = loadingCache<String, Int> { it.length }
            cache.get("hello")
            cache.get("world")

            cache.size shouldBe 2
        }
    }

    context("ExpiringCache") {
        test("stores and retrieves value") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            
            cache.put("a", 1)
            
            cache.get("a") shouldBe 1
        }
        
        test("contains returns true for non-expired entry") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            
            cache.contains("a") shouldBe true
        }
        
        test("getOrPut returns existing value") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            
            val result = cache.getOrPut("a") { 99 }
            
            result shouldBe 1
        }
        
        test("getOrPut computes for missing key") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            
            val result = cache.getOrPut("a") { 42 }
            
            result shouldBe 42
        }
        
        test("remove removes entry") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            
            cache.remove("a")
            
            cache.contains("a") shouldBe false
        }
        
        test("invalidate removes entry by key") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            cache.put("b", 2)
            
            cache.invalidate("a")
            
            cache.contains("a") shouldBe false
            cache.contains("b") shouldBe true
        }
        
        test("invalidateIf removes matching entries") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            cache.put("b", 2)
            cache.put("c", 3)
            
            cache.invalidateIf { key -> key != "a" }
            
            cache.contains("a") shouldBe true
            cache.contains("b") shouldBe false
            cache.contains("c") shouldBe false
        }
        
        test("clear removes all entries") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            cache.put("b", 2)

            cache.clear()

            cache.size shouldBe 0
        }

        test("entries returns non-expired entries") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            cache.put("b", 2)

            val entries = cache.entries()

            entries.size shouldBe 2
            entries["a"] shouldBe 1
            entries["b"] shouldBe 2
        }

        test("size returns count including potentially expired") {
            val cache = expiringCache<String, Int>(ttl = 1.minutes)
            cache.put("a", 1)
            cache.put("b", 2)

            cache.size shouldBe 2
        }
    }
})
