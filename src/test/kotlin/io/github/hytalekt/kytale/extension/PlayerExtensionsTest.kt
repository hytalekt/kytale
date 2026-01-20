package io.github.hytalekt.kytale.extension

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import java.util.UUID

class PlayerExtensionsTest : FunSpec({
    
    context("String.truncate") {
        test("truncates long string with ellipsis") {
            val result = "Hello World".truncate(8)
            
            result shouldBe "Hello..."
        }
        
        test("returns original string if shorter than maxLength") {
            val result = "Hello".truncate(10)
            
            result shouldBe "Hello"
        }
        
        test("returns original string if equal to maxLength") {
            val result = "Hello".truncate(5)
            
            result shouldBe "Hello"
        }
        
        test("uses custom suffix") {
            val result = "Hello World".truncate(8, ">>")
            
            result shouldBe "Hello >>"
        }
    }
    
    context("String.center") {
        test("centers string with padding") {
            val result = "Hi".center(6, '-')
            
            result shouldBe "--Hi--"
        }
        
        test("adds extra padding on right for odd difference") {
            val result = "Hi".center(7, '-')
            
            result shouldBe "--Hi---"
        }
        
        test("returns original string if longer than width") {
            val result = "Hello".center(3, '-')
            
            result shouldBe "Hello"
        }
    }
    
    context("String case conversion") {
        test("camelToTitleCase converts camelCase") {
            val result = "helloWorld".camelToTitleCase()
            
            result shouldBe "Hello World"
        }
        
        test("camelToTitleCase handles multiple words") {
            val result = "thisIsATest".camelToTitleCase()
            
            result shouldBe "This Is A Test"
        }
        
        test("snakeToTitleCase converts snake_case") {
            val result = "hello_world".snakeToTitleCase()
            
            result shouldBe "Hello World"
        }
        
        test("snakeToTitleCase handles multiple underscores") {
            val result = "this_is_a_test".snakeToTitleCase()
            
            result shouldBe "This Is A Test"
        }
    }
    
    context("Number formatting") {
        test("formatWithCommas formats large numbers") {
            val result = 1_000_000L.formatWithCommas()
            
            result shouldBe "1,000,000"
        }
        
        test("formatWithCommas handles small numbers") {
            val result = 100L.formatWithCommas()
            
            result shouldBe "100"
        }
        
        test("Int.formatWithCommas works") {
            val result = 1_000.formatWithCommas()
            
            result shouldBe "1,000"
        }
        
        test("formatCompact formats thousands as K") {
            val result = 1_500L.formatCompact()
            
            result shouldBe "1.5K"
        }
        
        test("formatCompact formats millions as M") {
            val result = 2_500_000L.formatCompact()
            
            result shouldBe "2.5M"
        }
        
        test("formatCompact formats billions as B") {
            val result = 3_500_000_000L.formatCompact()
            
            result shouldBe "3.5B"
        }
        
        test("formatCompact handles small numbers") {
            val result = 500L.formatCompact()
            
            result shouldBe "500"
        }
        
        test("Double.format formats to decimal places") {
            val result = 3.14159.format(2)
            
            result shouldBe "3.14"
        }
        
        test("Double.format rounds correctly") {
            val result = 3.145.format(2)
            
            result shouldBe "3.15"
        }
        
        test("toPercentString converts to percentage") {
            val result = 0.756.toPercentString()
            
            result shouldBe "75.6%"
        }
        
        test("toPercentString with decimals") {
            val result = 0.5.toPercentString(0)
            
            result shouldBe "50%"
        }
    }
    
    context("Int.toOrdinal") {
        test("1 becomes 1st") {
            1.toOrdinal() shouldBe "1st"
        }
        
        test("2 becomes 2nd") {
            2.toOrdinal() shouldBe "2nd"
        }
        
        test("3 becomes 3rd") {
            3.toOrdinal() shouldBe "3rd"
        }
        
        test("4 becomes 4th") {
            4.toOrdinal() shouldBe "4th"
        }
        
        test("11 becomes 11th (exception)") {
            11.toOrdinal() shouldBe "11th"
        }
        
        test("12 becomes 12th (exception)") {
            12.toOrdinal() shouldBe "12th"
        }
        
        test("13 becomes 13th (exception)") {
            13.toOrdinal() shouldBe "13th"
        }
        
        test("21 becomes 21st") {
            21.toOrdinal() shouldBe "21st"
        }
        
        test("22 becomes 22nd") {
            22.toOrdinal() shouldBe "22nd"
        }
        
        test("23 becomes 23rd") {
            23.toOrdinal() shouldBe "23rd"
        }
        
        test("111 becomes 111th") {
            111.toOrdinal() shouldBe "111th"
        }
    }
    
    context("Int.pluralize") {
        test("singular for 1") {
            1.pluralize("cat") shouldBe "1 cat"
        }
        
        test("plural for 0") {
            0.pluralize("cat") shouldBe "0 cats"
        }
        
        test("plural for multiple") {
            5.pluralize("cat") shouldBe "5 cats"
        }
        
        test("custom plural form") {
            5.pluralize("child", "children") shouldBe "5 children"
        }
        
        test("custom singular form") {
            1.pluralize("child", "children") shouldBe "1 child"
        }
    }
    
    context("UUID utilities") {
        test("isValidUUID returns true for valid UUID") {
            val result = "550e8400-e29b-41d4-a716-446655440000".isValidUUID()
            
            result shouldBe true
        }
        
        test("isValidUUID returns false for invalid format") {
            val result = "not-a-uuid".isValidUUID()
            
            result shouldBe false
        }
        
        test("isValidUUID returns false for empty string") {
            val result = "".isValidUUID()
            
            result shouldBe false
        }
        
        test("toUUIDOrNull returns UUID for valid string") {
            val result = "550e8400-e29b-41d4-a716-446655440000".toUUIDOrNull()
            
            result.shouldNotBeNull()
            result shouldBe UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        }
        
        test("toUUIDOrNull returns null for invalid string") {
            val result = "not-a-uuid".toUUIDOrNull()
            
            result.shouldBeNull()
        }
    }
    
    context("Duration formatting") {
        test("formatDuration formats hours minutes seconds") {
            val result = formatDuration(3661) // 1h 1m 1s
            
            result shouldBe "1h 1m 1s"
        }
        
        test("formatDuration omits zero hours") {
            val result = formatDuration(61) // 1m 1s
            
            result shouldBe "1m 1s"
        }
        
        test("formatDuration shows only seconds") {
            val result = formatDuration(45)
            
            result shouldBe "45s"
        }
        
        test("formatDurationCompact formats compactly") {
            val result = formatDurationCompact(3661)
            
            result shouldBe "1:01:01"
        }
    }
    
    context("safely functions") {
        test("safely returns result on success") {
            val result = safely { "hello" }
            
            result shouldBe "hello"
        }
        
        test("safely returns null on exception") {
            val result = safely<String> { throw RuntimeException() }
            
            result.shouldBeNull()
        }
        
        test("safelyOrDefault returns result on success") {
            val result = safelyOrDefault("default") { "hello" }
            
            result shouldBe "hello"
        }
        
        test("safelyOrDefault returns default on exception") {
            val result = safelyOrDefault("default") { throw RuntimeException() }
            
            result shouldBe "default"
        }
    }
    
    context("Location") {
        test("creates location with world and position") {
            val loc = location("world", 10.0, 64.0, 20.0)
            
            loc.worldName shouldBe "world"
            loc.x shouldBe 10.0
            loc.y shouldBe 64.0
            loc.z shouldBe 20.0
        }
        
        test("offset creates new location") {
            val loc = location("world", 0.0, 64.0, 0.0)
            
            val offset = loc.offset(10.0, 5.0, 20.0)
            
            offset.x shouldBe 10.0
            offset.y shouldBe 69.0
            offset.z shouldBe 20.0
        }
        
        test("distanceTo calculates distance in same world") {
            val loc1 = location("world", 0.0, 0.0, 0.0)
            val loc2 = location("world", 3.0, 4.0, 0.0)
            
            val distance = loc1.distanceTo(loc2)
            
            distance shouldBe 5.0
        }
        
        test("distanceTo returns infinity for different worlds") {
            val loc1 = location("world1", 0.0, 0.0, 0.0)
            val loc2 = location("world2", 0.0, 0.0, 0.0)
            
            val distance = loc1.distanceTo(loc2)
            
            distance shouldBe Double.POSITIVE_INFINITY
        }
        
        test("isWithinRange returns true when within range") {
            val loc1 = location("world", 0.0, 0.0, 0.0)
            val loc2 = location("world", 3.0, 4.0, 0.0)
            
            loc1.isWithinRange(loc2, 10.0) shouldBe true
        }
        
        test("isWithinRange returns false when out of range") {
            val loc1 = location("world", 0.0, 0.0, 0.0)
            val loc2 = location("world", 3.0, 4.0, 0.0)
            
            loc1.isWithinRange(loc2, 4.0) shouldBe false
        }
    }
})
