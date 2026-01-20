package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.coroutines.dsl.ticks
import io.github.hytalekt.kytale.coroutines.dsl.toTicks
import io.github.hytalekt.kytale.dsl.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Integration tests for DSL module (ConfigDsl, SchedulerDsl).
 *
 * CommandDsl and EventDsl are covered in their respective test files.
 */

fun IntegrationTestRunner.configDslTests() =
    suite("DSL - Config") {
        // ConfigValueDelegate tests
        test("configValue creates delegate with default") {
            val maxPlayers by configValue("max-players", 100)
            assertEquals(100, maxPlayers)
        }

        test("configValue delegate can be modified") {
            var serverName by configValue("server-name", "Default Server")
            assertEquals("Default Server", serverName)
            serverName = "My Server"
            assertEquals("My Server", serverName)
        }

        test("configValue reset returns to default") {
            val delegate = ConfigValueDelegate("test-key", 42)
            var value by delegate
            value = 100
            assertEquals(100, value)
            delegate.reset()
            assertEquals(42, value)
        }

        test("configValue works with different types - String") {
            val name by configValue("name", "test")
            assertEquals("test", name)
        }

        test("configValue works with different types - Boolean") {
            val enabled by configValue("enabled", true)
            assertEquals(true, enabled)
        }

        test("configValue works with different types - Double") {
            val rate by configValue("rate", 1.5)
            assertEquals(1.5, rate)
        }

        test("configValue works with different types - List") {
            val items by configValue("items", listOf("a", "b", "c"))
            assertEquals(3, items.size)
            assertEquals("a", items[0])
        }

        test("multiple configValues are independent") {
            var config1 by configValue("c1", 10)
            var config2 by configValue("c2", 20)
            config1 = 100
            assertEquals(100, config1)
            assertEquals(20, config2)
        }
    }

fun IntegrationTestRunner.schedulerDslTests() =
    suite("DSL - Scheduler") {
        // Tick conversion tests
        test("Int.ticks converts to Duration at 30 TPS") {
            val duration = 30.ticks() // 30 ticks at 30 TPS = 1 second
            assertEquals(1000L, duration.inWholeMilliseconds)
        }

        test("Int.ticks converts with custom TPS") {
            val duration = 20.ticks(tps = 20) // 20 ticks at 20 TPS = 1 second
            assertEquals(1000L, duration.inWholeMilliseconds)
        }

        test("Int.ticks converts partial second") {
            val duration = 15.ticks() // 15 ticks at 30 TPS = 0.5 seconds
            assertEquals(500L, duration.inWholeMilliseconds)
        }

        test("Duration.toTicks converts from Duration at 30 TPS") {
            val ticks = 1.seconds.toTicks() // 1 second = 30 ticks at 30 TPS
            assertEquals(30, ticks)
        }

        test("Duration.toTicks converts with custom TPS") {
            val ticks = 1.seconds.toTicks(tps = 20) // 1 second = 20 ticks at 20 TPS
            assertEquals(20, ticks)
        }

        test("Duration.toTicks converts partial second") {
            val ticks = 500.milliseconds.toTicks() // 0.5 seconds = 15 ticks at 30 TPS
            assertEquals(15, ticks)
        }

        test("tick conversion round trip") {
            val originalTicks = 60
            val duration = originalTicks.ticks()
            val backToTicks = duration.toTicks()
            assertEquals(originalTicks, backToTicks)
        }

        test("tick conversion with 60 TPS") {
            val duration = 60.ticks(tps = 60) // 60 ticks at 60 TPS = 1 second
            assertEquals(1000L, duration.inWholeMilliseconds)
            assertEquals(60, duration.toTicks(tps = 60))
        }

        test("zero ticks converts to zero duration") {
            val duration = 0.ticks()
            assertEquals(0L, duration.inWholeMilliseconds)
        }

        test("zero duration converts to zero ticks") {
            val ticks = 0.milliseconds.toTicks()
            assertEquals(0, ticks)
        }

        // Note: schedule, scheduleRepeating, async, sync functions require
        // a CoroutineScope and actual time passage to test fully.
        // They are better tested in coroutine-focused integration tests.
        // Here we test that the tick conversion utilities work correctly
        // which is the most testable part without async execution.
    }
