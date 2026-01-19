package io.github.hytalekt.kytale.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class ResultTest : FunSpec({
    
    context("Success") {
        test("isSuccess returns true") {
            val result = Result.success("hello")
            
            result.isSuccess shouldBe true
        }
        
        test("isFailure returns false") {
            val result = Result.success("hello")
            
            result.isFailure shouldBe false
        }
        
        test("getOrNull returns value") {
            val result = Result.success("hello")
            
            result.getOrNull() shouldBe "hello"
        }
        
        test("getOrDefault returns value") {
            val result = Result.success("hello")
            
            result.getOrDefault("default") shouldBe "hello"
        }
        
        test("getOrThrow returns value") {
            val result = Result.success("hello")
            
            result.getOrThrow() shouldBe "hello"
        }
    }
    
    context("Failure") {
        test("isSuccess returns false") {
            val result: Result<String> = Result.failure("error")
            
            result.isSuccess shouldBe false
        }
        
        test("isFailure returns true") {
            val result: Result<String> = Result.failure("error")
            
            result.isFailure shouldBe true
        }
        
        test("getOrNull returns null") {
            val result: Result<String> = Result.failure("error")
            
            result.getOrNull().shouldBeNull()
        }
        
        test("getOrDefault returns default") {
            val result: Result<String> = Result.failure("error")
            
            result.getOrDefault("default") shouldBe "default"
        }
        
        test("failure contains error message") {
            val result = Result.failure("custom error")
            
            (result as Result.Failure).error shouldBe "custom error"
        }
        
        test("failure can include cause") {
            val cause = RuntimeException("boom")
            val result = Result.failure("error", cause)
            
            (result as Result.Failure).cause shouldBe cause
        }
    }
    
    context("map") {
        test("transforms success value") {
            val result = Result.success("hello")
            val mapped = result.map { it.uppercase() }
            
            mapped.getOrNull() shouldBe "HELLO"
        }
        
        test("passes through failure") {
            val result: Result<String> = Result.failure("error")
            val mapped = result.map { it.uppercase() }
            
            mapped.isFailure shouldBe true
        }
    }
    
    context("flatMap") {
        test("chains successful results") {
            val result = Result.success("5")
            val flatMapped = result.flatMap { 
                Result.success(it.toInt()) 
            }
            
            flatMapped.getOrNull() shouldBe 5
        }
        
        test("returns failure from chain") {
            val result = Result.success("not a number")
            val flatMapped = result.flatMap { str ->
                try {
                    Result.success(str.toInt())
                } catch (e: NumberFormatException) {
                    Result.failure("Invalid number")
                }
            }
            
            flatMapped.isFailure shouldBe true
        }
        
        test("passes through original failure") {
            val result: Result<String> = Result.failure("original error")
            val flatMapped = result.flatMap { Result.success(it.length) }
            
            flatMapped.isFailure shouldBe true
            (flatMapped as Result.Failure).error shouldBe "original error"
        }
    }
    
    context("onSuccess") {
        test("executes action for success") {
            var executed = false
            val result = Result.success("hello")
            
            result.onSuccess { executed = true }
            
            executed shouldBe true
        }
        
        test("does not execute action for failure") {
            var executed = false
            val result: Result<String> = Result.failure("error")
            
            result.onSuccess { executed = true }
            
            executed shouldBe false
        }
        
        test("returns original result for chaining") {
            val result = Result.success("hello")
            
            val returned = result.onSuccess { }
            
            returned shouldBe result
        }
    }
    
    context("onFailure") {
        test("executes action for failure") {
            var executed = false
            val result: Result<String> = Result.failure("error")
            
            result.onFailure { _, _ -> executed = true }
            
            executed shouldBe true
        }
        
        test("passes error and cause to action") {
            var capturedError: String? = null
            var capturedCause: Throwable? = null
            val cause = RuntimeException("boom")
            val result: Result<String> = Result.failure("error", cause)
            
            result.onFailure { error, c -> 
                capturedError = error
                capturedCause = c
            }
            
            capturedError shouldBe "error"
            capturedCause shouldBe cause
        }
        
        test("does not execute action for success") {
            var executed = false
            val result = Result.success("hello")
            
            result.onFailure { _, _ -> executed = true }
            
            executed shouldBe false
        }
    }
    
    context("catching") {
        test("returns success for non-throwing block") {
            val result = Result.catching { "hello" }
            
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe "hello"
        }
        
        test("returns failure for throwing block") {
            val result = Result.catching<String> { 
                throw RuntimeException("boom") 
            }
            
            result.isFailure shouldBe true
        }
        
        test("captures exception message") {
            val result = Result.catching<String> { 
                throw RuntimeException("boom") 
            }
            
            (result as Result.Failure).error shouldBe "boom"
        }
        
        test("captures exception as cause") {
            val result = Result.catching<String> { 
                throw RuntimeException("boom") 
            }
            
            (result as Result.Failure).cause.shouldNotBeNull()
        }
    }
    
    context("toResult extension") {
        test("converts non-null to success") {
            val value: String? = "hello"
            val result = value.toResult("was null")
            
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe "hello"
        }
        
        test("converts null to failure") {
            val value: String? = null
            val result = value.toResult("was null")
            
            result.isFailure shouldBe true
            (result as Result.Failure).error shouldBe "was null"
        }
    }
})
