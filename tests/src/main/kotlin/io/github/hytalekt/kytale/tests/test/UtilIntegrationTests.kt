package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Integration tests for Util module.
 *
 * These are "unit-style" tests that run on the server to verify
 * util classes work correctly in the Hytale environment.
 */

fun IntegrationTestRunner.cacheTests() = suite("Util - Caches") {

    // SimpleCache tests
    test("SimpleCache put and get") {
        val cache = simpleCache<String, Int>()
        cache.put("a", 1)
        assertEquals(1, cache.get("a"))
    }

    test("SimpleCache getOrPut computes for missing") {
        val cache = simpleCache<String, Int>()
        val result = cache.getOrPut("a") { 42 }
        assertEquals(42, result)
        assertEquals(42, cache.get("a"))
    }

    test("SimpleCache size and clear") {
        val cache = simpleCache<String, Int>()
        cache.put("a", 1)
        cache.put("b", 2)
        assertEquals(2, cache.size)
        cache.clear()
        assertEquals(0, cache.size)
    }

    // LRUCache tests
    test("LRUCache evicts oldest entry") {
        val cache = lruCache<String, Int>(maxSize = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.put("c", 3)
        assertFalse(cache.contains("a"), "Should have evicted 'a'")
        assertTrue(cache.contains("b"))
        assertTrue(cache.contains("c"))
    }

    test("LRUCache access refreshes entry") {
        val cache = lruCache<String, Int>(maxSize = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.get("a") // refresh 'a'
        cache.put("c", 3) // should evict 'b'
        assertTrue(cache.contains("a"))
        assertFalse(cache.contains("b"), "Should have evicted 'b'")
    }

    // LoadingCache tests
    test("LoadingCache loads on first access") {
        var loadCount = 0
        val cache = loadingCache<String, Int> { loadCount++; it.length }
        val result = cache.get("hello")
        assertEquals(5, result)
        assertEquals(1, loadCount)
    }

    test("LoadingCache caches subsequent access") {
        var loadCount = 0
        val cache = loadingCache<String, Int> { loadCount++; it.length }
        cache.get("hello")
        cache.get("hello")
        assertEquals(1, loadCount)
    }

    // ExpiringCache tests
    test("ExpiringCache stores and retrieves") {
        val cache = expiringCache<String, Int>(ttl = 1.minutes)
        cache.put("a", 1)
        assertEquals(1, cache.get("a"))
    }

    test("ExpiringCache getOrPut works") {
        val cache = expiringCache<String, Int>(ttl = 1.minutes)
        val result = cache.getOrPut("a") { 42 }
        assertEquals(42, result)
    }
}

fun IntegrationTestRunner.mathTests() = suite("Util - Math") {

    test("clamp restricts to range") {
        assertEquals(0.0, (-5.0).clamp(0.0, 100.0))
        assertEquals(100.0, 150.0.clamp(0.0, 100.0))
        assertEquals(50.0, 50.0.clamp(0.0, 100.0))
    }

    test("lerp interpolates correctly") {
        assertEquals(0.0, lerp(0.0, 100.0, 0.0))
        assertEquals(100.0, lerp(0.0, 100.0, 1.0))
        assertEquals(50.0, lerp(0.0, 100.0, 0.5))
    }

    test("inverseLerp finds t value") {
        assertEquals(0.0, inverseLerp(0.0, 100.0, 0.0))
        assertEquals(1.0, inverseLerp(0.0, 100.0, 100.0))
        assertEquals(0.5, inverseLerp(0.0, 100.0, 50.0))
    }

    test("remap transforms between ranges") {
        assertEquals(0.5, 50.0.remap(0.0, 100.0, 0.0, 1.0))
        assertEquals(150.0, 5.0.remap(0.0, 10.0, 100.0, 200.0))
    }

    test("distance2D calculates correctly") {
        assertEquals(0.0, distance2D(5.0, 5.0, 5.0, 5.0))
        assertEquals(5.0, distance2D(0.0, 0.0, 3.0, 4.0))
    }

    test("distance3D calculates correctly") {
        assertEquals(0.0, distance3D(5.0, 5.0, 5.0, 5.0, 5.0, 5.0))
        assertEquals(3.0, distance3D(0.0, 0.0, 0.0, 1.0, 2.0, 2.0))
    }

    test("wrapDegrees normalizes angles") {
        assertEquals(270.0, (-90.0).wrapDegrees())
        assertEquals(90.0, 450.0.wrapDegrees())
    }

    test("approxEquals handles floating point") {
        assertTrue(1.0.approxEquals(1.0))
        assertTrue(1.0.approxEquals(1.0000001))
        assertFalse(1.0.approxEquals(2.0))
    }

    test("smoothstep provides smooth interpolation") {
        assertEquals(0.0, smoothstep(0.0, 1.0, 0.0))
        assertEquals(1.0, smoothstep(0.0, 1.0, 1.0))
        assertEquals(0.5, smoothstep(0.0, 1.0, 0.5))
    }

    test("manhattanDistance3D sums differences") {
        assertEquals(0, manhattanDistance3D(5, 5, 5, 5, 5, 5))
        assertEquals(6, manhattanDistance3D(0, 0, 0, 1, 2, 3))
    }
}

fun IntegrationTestRunner.validationTests() = suite("Util - Validation") {

    test("validate returns success when true") {
        val result = validate(true) { "Error" }
        assertTrue(result.isSuccess)
    }

    test("validate returns failure when false") {
        val result = validate(false) { "Error" }
        assertTrue(result.isFailure)
    }

    test("validateNotNull succeeds for non-null") {
        val result = validateNotNull("hello") { "Error" }
        assertTrue(result.isSuccess)
        assertEquals("hello", result.getOrNull())
    }

    test("ValidationBuilder chains requirements") {
        val result = validation<String>()
            .require(true) { "First" }
            .require(true) { "Second" }
            .build { "Success" }
        assertTrue(result.isSuccess)
    }

    test("ValidationBuilder fails on any false") {
        val result = validation<String>()
            .require(true) { "First" }
            .require(false) { "Second" }
            .build { "Never reached" }
        assertTrue(result.isFailure)
    }

    test("String validateNotBlank works") {
        assertTrue("hello".validateNotBlank().isSuccess)
        assertTrue("   ".validateNotBlank().isFailure)
    }

    test("String validateLength works") {
        assertTrue("hello".validateLength(1, 10).isSuccess)
        assertTrue("hi".validateLength(5, 10).isFailure)
    }

    test("Int validateRange works") {
        assertTrue(5.validateRange(1, 10).isSuccess)
        assertTrue(0.validateRange(1, 10).isFailure)
    }

    test("Int validatePositive works") {
        assertTrue(5.validatePositive().isSuccess)
        assertTrue(0.validatePositive().isFailure)
        assertTrue((-5).validatePositive().isFailure)
    }

    test("Collection validateNotEmpty works") {
        assertTrue(listOf(1, 2, 3).validateNotEmpty().isSuccess)
        assertTrue(emptyList<Int>().validateNotEmpty().isFailure)
    }

    test("UUID validation works") {
        assertTrue("550e8400-e29b-41d4-a716-446655440000".validateUUID().isSuccess)
        assertTrue("not-a-uuid".validateUUID().isFailure)
    }
}

fun IntegrationTestRunner.resultTests() = suite("Util - Result") {

    test("Success isSuccess returns true") {
        val result = Result.success("hello")
        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
    }

    test("Failure isFailure returns true") {
        val result: Result<String> = Result.failure("error")
        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
    }

    test("getOrNull returns value on success") {
        assertEquals("hello", Result.success("hello").getOrNull())
    }

    test("getOrNull returns null on failure") {
        val result: Result<String> = Result.failure("error")
        assertNull(result.getOrNull())
    }

    test("getOrDefault returns value on success") {
        assertEquals("hello", Result.success("hello").getOrDefault("default"))
    }

    test("getOrDefault returns default on failure") {
        val result: Result<String> = Result.failure("error")
        assertEquals("default", result.getOrDefault("default"))
    }

    test("map transforms success value") {
        val result = Result.success("hello").map { it.uppercase() }
        assertEquals("HELLO", result.getOrNull())
    }

    test("map passes through failure") {
        val result: Result<String> = Result.failure("error")
        val mapped = result.map { it.uppercase() }
        assertTrue(mapped.isFailure)
    }

    test("flatMap chains successful results") {
        val result = Result.success("5").flatMap { Result.success(it.toInt()) }
        assertEquals(5, result.getOrNull())
    }

    test("catching wraps successful block") {
        val result = Result.catching { "hello" }
        assertTrue(result.isSuccess)
    }

    test("catching wraps throwing block") {
        val result = Result.catching<String> { throw RuntimeException("boom") }
        assertTrue(result.isFailure)
    }

    test("toResult converts non-null to success") {
        val value: String? = "hello"
        assertTrue(value.toResult().isSuccess)
    }

    test("toResult converts null to failure") {
        val value: String? = null
        assertTrue(value.toResult().isFailure)
    }
}

fun IntegrationTestRunner.cooldownTests() = suite("Util - Cooldowns") {

    test("isReady returns true for new key") {
        val cooldown = Cooldown<String>(duration = 1.seconds)
        assertTrue(cooldown.isReady("test"))
    }

    test("trigger starts cooldown") {
        val cooldown = Cooldown<String>(duration = 1.seconds)
        cooldown.trigger("test")
        assertTrue(cooldown.isOnCooldown("test"))
        assertFalse(cooldown.isReady("test"))
    }

    test("reset clears cooldown") {
        val cooldown = Cooldown<String>(duration = 1.seconds)
        cooldown.trigger("test")
        cooldown.reset("test")
        assertTrue(cooldown.isReady("test"))
    }

    test("tryTrigger returns true when ready") {
        val cooldown = Cooldown<String>(duration = 1.seconds)
        assertTrue(cooldown.tryTrigger("test"))
        assertFalse(cooldown.tryTrigger("test"))
    }

    test("clear removes all cooldowns") {
        val cooldown = Cooldown<String>(duration = 1.seconds)
        cooldown.trigger("a")
        cooldown.trigger("b")
        cooldown.clear()
        assertTrue(cooldown.isReady("a"))
        assertTrue(cooldown.isReady("b"))
    }

    test("MultiCooldown tracks independent actions") {
        val cooldown = MultiCooldown<String>()
        cooldown.trigger("player", "attack", 1.seconds)
        cooldown.trigger("player", "heal", 2.seconds)
        assertTrue(cooldown.isOnCooldown("player", "attack"))
        assertTrue(cooldown.isOnCooldown("player", "heal"))
        assertTrue(cooldown.isReady("player", "sprint"))
    }

    test("MultiCooldown resetAll clears all for key") {
        val cooldown = MultiCooldown<String>()
        cooldown.trigger("player", "attack", 1.seconds)
        cooldown.trigger("player", "heal", 1.seconds)
        cooldown.resetAll("player")
        assertTrue(cooldown.isReady("player", "attack"))
        assertTrue(cooldown.isReady("player", "heal"))
    }
}
