package io.github.hytalekt.kytale.tests.test

import com.hypixel.hytale.math.vector.Vector3d
import io.github.hytalekt.kytale.extension.*

/**
 * Integration tests for Extension module.
 *
 * These tests verify extension functions work correctly in the Hytale environment.
 */

fun IntegrationTestRunner.vectorTests() = suite("Extension - Vector") {

    test("vec3 with doubles creates correct vector") {
        val v = vec3(1.0, 2.0, 3.0)
        assertEquals(1.0, v.x)
        assertEquals(2.0, v.y)
        assertEquals(3.0, v.z)
    }

    test("vec3 with ints creates correct vector") {
        val v = vec3(1, 2, 3)
        assertEquals(1.0, v.x)
        assertEquals(2.0, v.y)
        assertEquals(3.0, v.z)
    }

    test("VECTOR_ZERO is (0, 0, 0)") {
        assertEquals(0.0, VECTOR_ZERO.x)
        assertEquals(0.0, VECTOR_ZERO.y)
        assertEquals(0.0, VECTOR_ZERO.z)
    }

    test("VECTOR_UP is (0, 1, 0)") {
        assertEquals(0.0, VECTOR_UP.x)
        assertEquals(1.0, VECTOR_UP.y)
        assertEquals(0.0, VECTOR_UP.z)
    }

    test("vector addition works") {
        val a = vec3(1.0, 2.0, 3.0)
        val b = vec3(4.0, 5.0, 6.0)
        val result = a + b
        assertEquals(5.0, result.x)
        assertEquals(7.0, result.y)
        assertEquals(9.0, result.z)
    }

    test("vector subtraction works") {
        val a = vec3(5.0, 7.0, 9.0)
        val b = vec3(1.0, 2.0, 3.0)
        val result = a - b
        assertEquals(4.0, result.x)
        assertEquals(5.0, result.y)
        assertEquals(6.0, result.z)
    }

    test("vector scalar multiplication works") {
        val v = vec3(1.0, 2.0, 3.0)
        val result = v * 2.0
        assertEquals(2.0, result.x)
        assertEquals(4.0, result.y)
        assertEquals(6.0, result.z)
    }

    test("vector division works") {
        val v = vec3(6.0, 9.0, 12.0)
        val result = v / 3.0
        assertEquals(2.0, result.x)
        assertEquals(3.0, result.y)
        assertEquals(4.0, result.z)
    }

    test("unaryMinus negates vector") {
        val v = vec3(1.0, -2.0, 3.0)
        val result = -v
        assertEquals(-1.0, result.x)
        assertEquals(2.0, result.y)
        assertEquals(-3.0, result.z)
    }

    test("lengthSquared calculates correctly") {
        val v = vec3(3.0, 4.0, 0.0)
        assertEquals(25.0, v.lengthSquared())
    }

    test("normalized returns unit vector") {
        val v = vec3(10.0, 0.0, 0.0)
        val result = v.normalized()
        assertEquals(1.0, result.x)
        assertEquals(0.0, result.y)
        assertEquals(0.0, result.z)
    }

    test("distanceTo calculates correctly") {
        val a = vec3(0.0, 0.0, 0.0)
        val b = vec3(3.0, 4.0, 0.0)
        assertEquals(5.0, a.distanceTo(b))
    }

    test("dot product calculates correctly") {
        val a = vec3(1.0, 2.0, 3.0)
        val b = vec3(4.0, 5.0, 6.0)
        assertEquals(32.0, a.dot(b)) // 1*4 + 2*5 + 3*6
    }

    test("cross product of X and Y gives Z") {
        val x = vec3(1.0, 0.0, 0.0)
        val y = vec3(0.0, 1.0, 0.0)
        val result = x.cross(y)
        assertTrue(result.z > 0.99, "Z should be ~1.0")
    }

    test("lerp at midpoint") {
        val a = vec3(0.0, 0.0, 0.0)
        val b = vec3(10.0, 20.0, 30.0)
        val result = a.lerp(b, 0.5)
        assertEquals(5.0, result.x)
        assertEquals(10.0, result.y)
        assertEquals(15.0, result.z)
    }

    test("withX creates modified vector") {
        val v = vec3(1.0, 2.0, 3.0)
        val result = v.withX(10.0)
        assertEquals(10.0, result.x)
        assertEquals(2.0, result.y)
        assertEquals(3.0, result.z)
    }

    test("horizontal zeroes Y component") {
        val v = vec3(5.0, 10.0, 15.0)
        val result = v.horizontal()
        assertEquals(5.0, result.x)
        assertEquals(0.0, result.y)
        assertEquals(15.0, result.z)
    }

    test("horizontalLength ignores Y") {
        val v = vec3(3.0, 100.0, 4.0)
        assertEquals(5.0, v.horizontalLength())
    }

    test("isWithinRange works") {
        val a = vec3(0.0, 0.0, 0.0)
        val b = vec3(3.0, 4.0, 0.0)
        assertTrue(a.isWithinRange(b, 10.0))
        assertFalse(a.isWithinRange(b, 4.0))
    }

    test("clamp restricts components") {
        val v = vec3(-5.0, 50.0, 5.0)
        val result = v.clamp(0.0, 10.0)
        assertEquals(0.0, result.x)
        assertEquals(10.0, result.y)
        assertEquals(5.0, result.z)
    }

    test("isZero returns true for zero vector") {
        assertTrue(VECTOR_ZERO.isZero())
    }

    test("approxEquals works") {
        val a = vec3(1.0, 2.0, 3.0)
        val b = vec3(1.00001, 2.00001, 3.00001)
        assertTrue(a.approxEquals(b))
    }
}

fun IntegrationTestRunner.collectionTests() = suite("Extension - Collection") {

    test("randomOrNull returns null for empty") {
        val result = emptyList<Int>().randomOrNull()
        assertNull(result)
    }

    test("randomOrNull returns element from non-empty") {
        val list = listOf(42)
        val result = list.randomOrNull()
        assertEquals(42, result)
    }

    test("randomElements returns requested count") {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.randomElements(3)
        assertEquals(3, result.size)
    }

    test("randomElements caps at collection size") {
        val list = listOf(1, 2, 3)
        val result = list.randomElements(10)
        assertEquals(3, result.size)
    }

    test("allMatch returns true when all match") {
        val list = listOf(2, 4, 6, 8)
        assertTrue(list.allMatch { it % 2 == 0 })
    }

    test("allMatch returns false when any fails") {
        val list = listOf(2, 3, 6, 8)
        assertFalse(list.allMatch { it % 2 == 0 })
    }

    test("anyMatch returns true when any matches") {
        val list = listOf(1, 2, 3)
        assertTrue(list.anyMatch { it == 2 })
    }

    test("anyMatch returns false when none match") {
        val list = listOf(1, 3, 5)
        assertFalse(list.anyMatch { it == 2 })
    }

    test("noneMatch returns true when none match") {
        val list = listOf(1, 3, 5)
        assertTrue(list.noneMatch { it == 2 })
    }

    test("countBy counts occurrences") {
        val list = listOf("a", "b", "a", "c", "a")
        val result = list.countBy { it }
        assertEquals(3, result["a"])
        assertEquals(1, result["b"])
    }

    test("duplicates finds duplicate elements") {
        val list = listOf("a", "b", "a", "c", "b")
        val result = list.duplicates()
        assertTrue(result.contains("a"))
        assertTrue(result.contains("b"))
        assertFalse(result.contains("c"))
    }

    test("secondOrNull returns second element") {
        val list = listOf(1, 2, 3)
        assertEquals(2, list.secondOrNull())
    }

    test("secondOrNull returns null for single element") {
        val list = listOf(1)
        assertNull(list.secondOrNull())
    }

    test("thirdOrNull returns third element") {
        val list = listOf(1, 2, 3)
        assertEquals(3, list.thirdOrNull())
    }

    test("replaceFirst replaces first match") {
        val list = listOf(1, 2, 3, 2, 4)
        val result = list.replaceFirst({ it == 2 }, 99)
        assertEquals(listOf(1, 99, 3, 2, 4), result)
    }

    test("replaceAll replaces all matches") {
        val list = listOf(1, 2, 3, 2, 4)
        val result = list.replaceAll({ it == 2 }, 99)
        assertEquals(listOf(1, 99, 3, 99, 4), result)
    }

    test("swap swaps elements") {
        val list = mutableListOf(1, 2, 3, 4, 5)
        list.swap(1, 3)
        assertEquals(listOf(1, 4, 3, 2, 5), list)
    }

    test("removeFirstOrNull removes and returns first") {
        val list = mutableListOf(1, 2, 3)
        val removed = list.removeFirstOrNull()
        assertEquals(1, removed)
        assertEquals(listOf(2, 3), list)
    }

    test("removeLastOrNull removes and returns last") {
        val list = mutableListOf(1, 2, 3)
        val removed = list.removeLastOrNull()
        assertEquals(3, removed)
        assertEquals(listOf(1, 2), list)
    }
}

fun IntegrationTestRunner.playerUtilTests() = suite("Extension - Player Utils") {

    // String truncate
    test("truncate shortens long string") {
        val result = "Hello World".truncate(8)
        assertEquals("Hello...", result)
    }

    test("truncate keeps short string") {
        val result = "Hello".truncate(10)
        assertEquals("Hello", result)
    }

    test("truncate with custom suffix") {
        val result = "Hello World".truncate(8, ">>")
        assertEquals("Hello >>", result)
    }

    // String center
    test("center pads string") {
        val result = "Hi".center(6, '-')
        assertEquals("--Hi--", result)
    }

    // Case conversion
    test("camelToTitleCase converts") {
        val result = "helloWorld".camelToTitleCase()
        assertEquals("Hello World", result)
    }

    test("snakeToTitleCase converts") {
        val result = "hello_world".snakeToTitleCase()
        assertEquals("Hello World", result)
    }

    // Number formatting
    test("formatWithCommas formats large numbers") {
        val result = 1_000_000L.formatWithCommas()
        assertEquals("1,000,000", result)
    }

    test("formatCompact formats thousands") {
        val result = 1_500L.formatCompact()
        assertEquals("1.5K", result)
    }

    test("formatCompact formats millions") {
        val result = 2_500_000L.formatCompact()
        assertEquals("2.5M", result)
    }

    test("formatCompact formats billions") {
        val result = 3_500_000_000L.formatCompact()
        assertEquals("3.5B", result)
    }

    test("Double.format formats decimal places") {
        val result = 3.14159.format(2)
        assertEquals("3.14", result)
    }

    test("toPercentString converts to percentage") {
        val result = 0.756.toPercentString()
        assertEquals("75.6%", result)
    }

    // Ordinals
    test("1 becomes 1st") {
        assertEquals("1st", 1.toOrdinal())
    }

    test("2 becomes 2nd") {
        assertEquals("2nd", 2.toOrdinal())
    }

    test("3 becomes 3rd") {
        assertEquals("3rd", 3.toOrdinal())
    }

    test("11 becomes 11th") {
        assertEquals("11th", 11.toOrdinal())
    }

    test("21 becomes 21st") {
        assertEquals("21st", 21.toOrdinal())
    }

    // Pluralize
    test("pluralize singular for 1") {
        assertEquals("1 cat", 1.pluralize("cat"))
    }

    test("pluralize plural for multiple") {
        assertEquals("5 cats", 5.pluralize("cat"))
    }

    test("pluralize custom plural") {
        assertEquals("5 children", 5.pluralize("child", "children"))
    }

    // UUID utilities
    test("isValidUUID returns true for valid") {
        assertTrue("550e8400-e29b-41d4-a716-446655440000".isValidUUID())
    }

    test("isValidUUID returns false for invalid") {
        assertFalse("not-a-uuid".isValidUUID())
    }

    test("toUUIDOrNull returns UUID for valid") {
        val result = "550e8400-e29b-41d4-a716-446655440000".toUUIDOrNull()
        assertNotNull(result)
    }

    test("toUUIDOrNull returns null for invalid") {
        val result = "not-a-uuid".toUUIDOrNull()
        assertNull(result)
    }

    // Duration formatting
    test("formatDuration formats hours minutes seconds") {
        val result = formatDuration(3661)
        assertEquals("1h 1m 1s", result)
    }

    test("formatDuration omits zero hours") {
        val result = formatDuration(61)
        assertEquals("1m 1s", result)
    }

    test("formatDurationCompact formats compactly") {
        val result = formatDurationCompact(3661)
        assertEquals("1:01:01", result)
    }

    // safely functions
    test("safely returns result on success") {
        val result = safely { "hello" }
        assertEquals("hello", result)
    }

    test("safely returns null on exception") {
        val result = safely<String> { throw RuntimeException() }
        assertNull(result)
    }

    test("safelyOrDefault returns result on success") {
        val result = safelyOrDefault("default") { "hello" }
        assertEquals("hello", result)
    }

    test("safelyOrDefault returns default on exception") {
        val result = safelyOrDefault("default") { throw RuntimeException() }
        assertEquals("default", result)
    }

    // Location
    test("location creates with position") {
        val loc = location("world", 10.0, 64.0, 20.0)
        assertEquals("world", loc.worldName)
        assertEquals(10.0, loc.x)
        assertEquals(64.0, loc.y)
        assertEquals(20.0, loc.z)
    }

    test("location offset creates new location") {
        val loc = location("world", 0.0, 64.0, 0.0)
        val offset = loc.offset(10.0, 5.0, 20.0)
        assertEquals(10.0, offset.x)
        assertEquals(69.0, offset.y)
        assertEquals(20.0, offset.z)
    }

    test("location distanceTo in same world") {
        val loc1 = location("world", 0.0, 0.0, 0.0)
        val loc2 = location("world", 3.0, 4.0, 0.0)
        assertEquals(5.0, loc1.distanceTo(loc2))
    }

    test("location distanceTo different worlds is infinity") {
        val loc1 = location("world1", 0.0, 0.0, 0.0)
        val loc2 = location("world2", 0.0, 0.0, 0.0)
        assertEquals(Double.POSITIVE_INFINITY, loc1.distanceTo(loc2))
    }

    // Additional Location tests
    test("location with int coordinates") {
        val loc = location("world", 10, 64, 20)
        assertEquals(10.0, loc.x)
        assertEquals(64.0, loc.y)
        assertEquals(20.0, loc.z)
    }

    test("location with yaw and pitch") {
        val loc = location("world", 10.0, 64.0, 20.0, 90.0f, -45.0f)
        assertEquals(90.0f, loc.yaw)
        assertEquals(-45.0f, loc.pitch)
    }

    test("location toBlockCoordinates") {
        val loc = location("world", 10.7, 64.9, -20.3)
        val (blockX, blockY, blockZ) = loc.toBlockCoordinates()
        assertEquals(10, blockX)
        assertEquals(64, blockY)
        assertEquals(-20, blockZ)
    }

    test("location toChunkCoordinates") {
        val loc = location("world", 100.0, 64.0, -50.0)
        val (chunkX, chunkZ) = loc.toChunkCoordinates()
        assertEquals(6, chunkX)  // 100 >> 4 = 6
        assertEquals(-4, chunkZ) // -50 >> 4 = -4 (signed right shift floors negative numbers)
    }

    test("location distanceSquaredTo same world") {
        val loc1 = location("world", 0.0, 0.0, 0.0)
        val loc2 = location("world", 3.0, 4.0, 0.0)
        assertEquals(25.0, loc1.distanceSquaredTo(loc2))
    }

    test("location distanceSquaredTo different worlds is infinity") {
        val loc1 = location("world1", 0.0, 0.0, 0.0)
        val loc2 = location("world2", 0.0, 0.0, 0.0)
        assertEquals(Double.POSITIVE_INFINITY, loc1.distanceSquaredTo(loc2))
    }

    test("location isWithinRange true when in range") {
        val loc1 = location("world", 0.0, 0.0, 0.0)
        val loc2 = location("world", 3.0, 4.0, 0.0)
        assertTrue(loc1.isWithinRange(loc2, 10.0))
    }

    test("location isWithinRange false when out of range") {
        val loc1 = location("world", 0.0, 0.0, 0.0)
        val loc2 = location("world", 3.0, 4.0, 0.0)
        assertFalse(loc1.isWithinRange(loc2, 4.0))
    }

    test("location isWithinRange false for different worlds") {
        val loc1 = location("world1", 0.0, 0.0, 0.0)
        val loc2 = location("world2", 0.0, 0.0, 0.0)
        assertFalse(loc1.isWithinRange(loc2, 100.0))
    }
}
