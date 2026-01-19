package io.github.hytalekt.kytale.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class CooldownTest : FunSpec({
    
    context("Cooldown") {
        test("isReady returns true for new key") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.isReady("test") shouldBe true
        }
        
        test("isOnCooldown returns false for new key") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.isOnCooldown("test") shouldBe false
        }
        
        test("trigger starts cooldown") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("test")
            
            cooldown.isOnCooldown("test") shouldBe true
            cooldown.isReady("test") shouldBe false
        }
        
        test("remaining returns null for new key") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.remaining("test").shouldBeNull()
        }
        
        test("remaining returns duration after trigger") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("test")
            
            val remaining = cooldown.remaining("test")
            remaining.shouldNotBeNull()
            remaining shouldBeGreaterThan 0.milliseconds
            remaining shouldBeLessThanOrEqualTo 1.seconds
        }
        
        test("reset clears cooldown") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("test")
            cooldown.reset("test")
            
            cooldown.isReady("test") shouldBe true
            cooldown.remaining("test").shouldBeNull()
        }
        
        test("tryTrigger returns true and triggers when ready") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            val result = cooldown.tryTrigger("test")
            
            result shouldBe true
            cooldown.isOnCooldown("test") shouldBe true
        }
        
        test("tryTrigger returns false when on cooldown") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("test")
            val result = cooldown.tryTrigger("test")
            
            result shouldBe false
        }
        
        test("clear removes all cooldowns") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("a")
            cooldown.trigger("b")
            cooldown.trigger("c")
            cooldown.clear()
            
            cooldown.isReady("a") shouldBe true
            cooldown.isReady("b") shouldBe true
            cooldown.isReady("c") shouldBe true
            cooldown.size shouldBe 0
        }
        
        test("size returns number of active cooldowns") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            
            cooldown.trigger("a")
            cooldown.trigger("b")
            
            cooldown.size shouldBe 2
        }
        
        test("ifReady executes action when ready") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            var executed = false
            
            cooldown.ifReady("test", action = { executed = true })
            
            executed shouldBe true
        }
        
        test("ifReady executes fallback when on cooldown") {
            val cooldown = Cooldown<String>(duration = 1.seconds)
            var fallbackExecuted = false
            
            cooldown.trigger("test")
            cooldown.ifReady(
                "test",
                action = { },
                fallback = { fallbackExecuted = true }
            )
            
            fallbackExecuted shouldBe true
        }
    }
    
    context("MultiCooldown") {
        test("independent cooldowns per action") {
            val cooldown = MultiCooldown<String>()
            
            cooldown.trigger("player", "attack", 1.seconds)
            cooldown.trigger("player", "heal", 2.seconds)
            
            cooldown.isOnCooldown("player", "attack") shouldBe true
            cooldown.isOnCooldown("player", "heal") shouldBe true
            cooldown.isReady("player", "sprint") shouldBe true
        }
        
        test("reset only clears specific action") {
            val cooldown = MultiCooldown<String>()
            
            cooldown.trigger("player", "attack", 1.seconds)
            cooldown.trigger("player", "heal", 1.seconds)
            cooldown.reset("player", "attack")
            
            cooldown.isReady("player", "attack") shouldBe true
            cooldown.isOnCooldown("player", "heal") shouldBe true
        }
        
        test("resetAll clears all actions for key") {
            val cooldown = MultiCooldown<String>()
            
            cooldown.trigger("player", "attack", 1.seconds)
            cooldown.trigger("player", "heal", 1.seconds)
            cooldown.resetAll("player")
            
            cooldown.isReady("player", "attack") shouldBe true
            cooldown.isReady("player", "heal") shouldBe true
        }
    }
    
    context("factory functions") {
        test("playerCooldown creates UUID-keyed cooldown") {
            val cooldown = playerCooldown(1.seconds)
            val uuid = UUID.randomUUID()
            
            cooldown.trigger(uuid)
            
            cooldown.isOnCooldown(uuid) shouldBe true
        }
        
        test("namedCooldown creates String-keyed cooldown") {
            val cooldown = namedCooldown(1.seconds)
            
            cooldown.trigger("ability")
            
            cooldown.isOnCooldown("ability") shouldBe true
        }
    }
})
