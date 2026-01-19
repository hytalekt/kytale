package io.github.hytalekt.kytale.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ValidationTest : FunSpec({
    
    context("validate function") {
        test("returns success when condition is true") {
            val result = validate(true) { "Error" }
            
            result.isSuccess shouldBe true
        }
        
        test("returns failure when condition is false") {
            val result = validate(false) { "Error message" }
            
            result.isFailure shouldBe true
        }
        
        test("failure contains error message") {
            val result = validate(false) { "Custom error" }
            
            (result as Result.Failure).error shouldBe "Custom error"
        }
    }
    
    context("validateNotNull function") {
        test("returns success with non-null value") {
            val result = validateNotNull("hello") { "Error" }
            
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe "hello"
        }
        
        test("returns failure with null value") {
            val result = validateNotNull<String>(null) { "Value required" }
            
            result.isFailure shouldBe true
        }
    }
    
    context("ValidationBuilder") {
        test("builds success when all requirements pass") {
            val result = validation<String>()
                .require(true) { "First" }
                .require(true) { "Second" }
                .build { "Success" }
            
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe "Success"
        }
        
        test("builds failure when any requirement fails") {
            val result = validation<String>()
                .require(true) { "First" }
                .require(false) { "Second failed" }
                .build { "Never reached" }
            
            result.isFailure shouldBe true
        }
        
        test("isValid returns true when all requirements pass") {
            val builder = validation<String>()
                .require(true) { "First" }
                .require(true) { "Second" }
            
            builder.isValid() shouldBe true
        }
        
        test("isValid returns false when any requirement fails") {
            val builder = validation<String>()
                .require(true) { "First" }
                .require(false) { "Second" }
            
            builder.isValid() shouldBe false
        }
        
        test("errors returns list of failure messages") {
            val builder = validation<String>()
                .require(false) { "First error" }
                .require(true) { "Not an error" }
                .require(false) { "Second error" }
            
            val errors = builder.errors()
            errors.size shouldBe 2
            errors[0] shouldBe "First error"
            errors[1] shouldBe "Second error"
        }
        
        test("errors returns empty list when all pass") {
            val builder = validation<String>()
                .require(true) { "Not an error" }

            builder.errors().size shouldBe 0
        }

        test("requireThat validates value with predicate") {
            val result = validation<String>()
                .requireThat("hello", { it.length > 3 }) { "Too short" }
                .build { "Success" }

            result.isSuccess shouldBe true
        }

        test("requireThat fails when predicate returns false") {
            val result = validation<String>()
                .requireThat("hi", { it.length > 3 }) { "Too short" }
                .build { "Success" }

            result.isFailure shouldBe true
        }
    }

    context("String validation extensions") {
        test("validateNotBlank succeeds for non-blank string") {
            val result = "hello".validateNotBlank()
            
            result.isSuccess shouldBe true
        }
        
        test("validateNotBlank fails for blank string") {
            val result = "   ".validateNotBlank()
            
            result.isFailure shouldBe true
        }
        
        test("validateNotBlank fails for empty string") {
            val result = "".validateNotBlank()
            
            result.isFailure shouldBe true
        }
        
        test("validateLength succeeds within range") {
            val result = "hello".validateLength(1, 10)
            
            result.isSuccess shouldBe true
        }
        
        test("validateLength fails when too short") {
            val result = "hi".validateLength(5, 10)
            
            result.isFailure shouldBe true
        }
        
        test("validateLength fails when too long") {
            val result = "hello world".validateLength(1, 5)
            
            result.isFailure shouldBe true
        }
        
        test("validatePattern succeeds for matching pattern") {
            val result = "abc123".validatePattern(Regex("[a-z]+[0-9]+"), "Error")
            
            result.isSuccess shouldBe true
        }
        
        test("validatePattern fails for non-matching pattern") {
            val result = "123abc".validatePattern(Regex("[a-z]+[0-9]+"), "Error")
            
            result.isFailure shouldBe true
        }
        
        test("validateAlphanumeric succeeds for alphanumeric") {
            val result = "abc123".validateAlphanumeric()
            
            result.isSuccess shouldBe true
        }
        
        test("validateAlphanumeric fails for special chars") {
            val result = "abc-123".validateAlphanumeric()
            
            result.isFailure shouldBe true
        }
    }
    
    context("Int validation extensions") {
        test("validateRange succeeds within range") {
            val result = 5.validateRange(1, 10)
            
            result.isSuccess shouldBe true
        }
        
        test("validateRange fails below min") {
            val result = 0.validateRange(1, 10)
            
            result.isFailure shouldBe true
        }
        
        test("validateRange fails above max") {
            val result = 11.validateRange(1, 10)
            
            result.isFailure shouldBe true
        }
        
        test("validatePositive succeeds for positive") {
            val result = 5.validatePositive()
            
            result.isSuccess shouldBe true
        }
        
        test("validatePositive fails for zero") {
            val result = 0.validatePositive()
            
            result.isFailure shouldBe true
        }
        
        test("validatePositive fails for negative") {
            val result = (-5).validatePositive()
            
            result.isFailure shouldBe true
        }
        
        test("validateNonNegative succeeds for positive") {
            val result = 5.validateNonNegative()
            
            result.isSuccess shouldBe true
        }
        
        test("validateNonNegative succeeds for zero") {
            val result = 0.validateNonNegative()
            
            result.isSuccess shouldBe true
        }
        
        test("validateNonNegative fails for negative") {
            val result = (-5).validateNonNegative()
            
            result.isFailure shouldBe true
        }
    }
    
    context("Collection validation extensions") {
        test("validateNotEmpty succeeds for non-empty list") {
            val result = listOf(1, 2, 3).validateNotEmpty()
            
            result.isSuccess shouldBe true
        }
        
        test("validateNotEmpty fails for empty list") {
            val result = emptyList<Int>().validateNotEmpty()
            
            result.isFailure shouldBe true
        }
        
        test("validateSize succeeds within range") {
            val result = listOf(1, 2, 3).validateSize(1, 5)
            
            result.isSuccess shouldBe true
        }
        
        test("validateSize fails when too small") {
            val result = listOf(1).validateSize(2, 5)
            
            result.isFailure shouldBe true
        }
        
        test("validateSize fails when too large") {
            val result = listOf(1, 2, 3, 4, 5, 6).validateSize(1, 5)
            
            result.isFailure shouldBe true
        }
    }
    
    context("UUID validation") {
        test("validateUUID succeeds for valid UUID") {
            val result = "550e8400-e29b-41d4-a716-446655440000".validateUUID()

            result.isSuccess shouldBe true
        }

        test("validateUUID fails for invalid UUID") {
            val result = "not-a-uuid".validateUUID()

            result.isFailure shouldBe true
        }
    }

    context("Double validation extensions") {
        test("validateRange succeeds within range") {
            val result = 5.5.validateRange(1.0, 10.0)

            result.isSuccess shouldBe true
        }

        test("validateRange fails below min") {
            val result = 0.5.validateRange(1.0, 10.0)

            result.isFailure shouldBe true
        }

        test("validateRange fails above max") {
            val result = 10.5.validateRange(1.0, 10.0)

            result.isFailure shouldBe true
        }

        test("validatePositive succeeds for positive") {
            val result = 5.5.validatePositive()

            result.isSuccess shouldBe true
        }

        test("validatePositive fails for zero") {
            val result = 0.0.validatePositive()

            result.isFailure shouldBe true
        }

        test("validatePositive fails for negative") {
            val result = (-5.5).validatePositive()

            result.isFailure shouldBe true
        }
    }

    context("Username validation") {
        test("validateUsername succeeds for valid username") {
            val result = "player_123".validateUsername()

            result.isSuccess shouldBe true
        }

        test("validateUsername fails for too short") {
            val result = "ab".validateUsername()

            result.isFailure shouldBe true
        }

        test("validateUsername fails for too long") {
            val result = "a".repeat(17).validateUsername()

            result.isFailure shouldBe true
        }

        test("validateUsername fails for special characters") {
            val result = "player@123".validateUsername()

            result.isFailure shouldBe true
        }
    }

    context("Email validation") {
        test("validateEmail succeeds for valid email") {
            val result = "user@example.com".validateEmail()

            result.isSuccess shouldBe true
        }

        test("validateEmail fails for missing @") {
            val result = "userexample.com".validateEmail()

            result.isFailure shouldBe true
        }

        test("validateEmail fails for missing domain") {
            val result = "user@".validateEmail()

            result.isFailure shouldBe true
        }
    }

    context("Patterns object") {
        test("USERNAME pattern matches valid usernames") {
            "abc_123".matches(Patterns.USERNAME) shouldBe true
            "Player1".matches(Patterns.USERNAME) shouldBe true
        }

        test("USERNAME pattern rejects invalid usernames") {
            "ab".matches(Patterns.USERNAME) shouldBe false
            "user@name".matches(Patterns.USERNAME) shouldBe false
        }

        test("EMAIL pattern matches valid emails") {
            "test@example.com".matches(Patterns.EMAIL) shouldBe true
            "user.name+tag@domain.co".matches(Patterns.EMAIL) shouldBe true
        }

        test("ALPHANUMERIC pattern matches alphanumeric") {
            "abc123".matches(Patterns.ALPHANUMERIC) shouldBe true
            "abc-123".matches(Patterns.ALPHANUMERIC) shouldBe false
        }

        test("NUMERIC pattern matches numbers only") {
            "12345".matches(Patterns.NUMERIC) shouldBe true
            "123a45".matches(Patterns.NUMERIC) shouldBe false
        }

        test("HEX_COLOR pattern matches hex colors") {
            "#FF00AA".matches(Patterns.HEX_COLOR) shouldBe true
            "#ff00aa".matches(Patterns.HEX_COLOR) shouldBe true
            "FF00AA".matches(Patterns.HEX_COLOR) shouldBe false
            "#FFF".matches(Patterns.HEX_COLOR) shouldBe false
        }
    }
})
