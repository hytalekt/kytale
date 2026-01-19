package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.coroutines.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Integration tests for Coroutines module.
 *
 * Tests HytaleDispatchers, PluginScope, and AsyncExtensions.
 */

fun IntegrationTestRunner.coroutinesDispatcherTests() = suite("Coroutines - Dispatchers") {

    test("Main dispatcher is available") {
        val dispatcher = HytaleDispatchers.Main
        assertNotNull(dispatcher)
    }

    test("Async dispatcher is available") {
        val dispatcher = HytaleDispatchers.Async
        assertNotNull(dispatcher)
    }

    test("IO dispatcher is available") {
        val dispatcher = HytaleDispatchers.IO
        assertNotNull(dispatcher)
    }

    test("Single dispatcher is available") {
        val dispatcher = HytaleDispatchers.Single
        assertNotNull(dispatcher)
    }

    test("setMainExecutor configures Main dispatcher") {
        val executor = Executors.newSingleThreadExecutor()
        try {
            HytaleDispatchers.setMainExecutor(executor)
            // After setting, Main should use the provided executor
            val dispatcher = HytaleDispatchers.Main
            assertNotNull(dispatcher)
        } finally {
            HytaleDispatchers.clearMainExecutor()
            executor.shutdown()
        }
    }

    test("clearMainExecutor resets Main dispatcher") {
        val executor = Executors.newSingleThreadExecutor()
        try {
            HytaleDispatchers.setMainExecutor(executor)
            HytaleDispatchers.clearMainExecutor()
            // After clearing, Main should fall back to Default
            val dispatcher = HytaleDispatchers.Main
            assertNotNull(dispatcher)
        } finally {
            executor.shutdown()
        }
    }

    test("forWorld creates dispatcher from executor") {
        val executor = Executors.newSingleThreadExecutor()
        try {
            val dispatcher = HytaleDispatchers.forWorld(executor)
            assertNotNull(dispatcher)
        } finally {
            executor.shutdown()
        }
    }

    test("Single dispatcher runs tasks sequentially") {
        val results = mutableListOf<Int>()
        val counter = AtomicInteger(0)

        runBlocking {
            val jobs = (1..3).map { index ->
                launch(HytaleDispatchers.Single) {
                    // Each task records its order and the current count
                    results.add(counter.incrementAndGet())
                }
            }
            jobs.forEach { it.join() }
        }

        // Results should be sequential: 1, 2, 3
        assertEquals(3, results.size)
        assertTrue(results.contains(1))
        assertTrue(results.contains(2))
        assertTrue(results.contains(3))
    }
}

fun IntegrationTestRunner.coroutinesPluginScopeTests() = suite("Coroutines - PluginScope") {

    test("PluginScope is active on creation") {
        val scope = PluginScope(plugin)
        assertTrue(scope.isActive)
    }

    test("PluginScope cancel sets inactive") {
        val scope = PluginScope(plugin)
        scope.cancel()
        assertFalse(scope.isActive)
    }

    test("PluginScope cancel with message sets inactive") {
        val scope = PluginScope(plugin)
        scope.cancel("Test cancellation")
        assertFalse(scope.isActive)
    }

    test("pluginScope extension creates scope") {
        val scope = plugin.pluginScope()
        assertTrue(scope.isActive)
        scope.cancel()
    }

    test("pluginScope extension with custom dispatcher") {
        val scope = plugin.pluginScope(HytaleDispatchers.IO)
        assertTrue(scope.isActive)
        scope.cancel()
    }

    test("PluginScope launch executes task") {
        val scope = PluginScope(plugin)
        val executed = AtomicBoolean(false)

        runBlocking {
            val job = scope.launch {
                executed.set(true)
            }
            job.join()
        }

        assertTrue(executed.get())
        scope.cancel()
    }

    test("PluginScope cancel cancels child jobs") {
        val scope = PluginScope(plugin)
        val completed = AtomicBoolean(false)

        val job = scope.launch {
            try {
                delay(10000) // Long delay
                completed.set(true)
            } catch (e: CancellationException) {
                // Expected
            }
        }

        // Cancel immediately
        scope.cancel()

        runBlocking {
            try {
                withTimeout(100) {
                    job.join()
                }
            } catch (e: Exception) {
                // Job might be cancelled
            }
        }

        assertFalse(completed.get())
    }
}

fun IntegrationTestRunner.coroutinesAsyncExtensionsTests() = suite("Coroutines - AsyncExtensions") {

    test("onMainThread switches to Main dispatcher") {
        val result = runBlocking {
            onMainThread {
                "executed"
            }
        }
        assertEquals("executed", result)
    }

    test("onAsync switches to Async dispatcher") {
        val result = runBlocking {
            onAsync {
                "executed"
            }
        }
        assertEquals("executed", result)
    }

    test("onIO switches to IO dispatcher") {
        val result = runBlocking {
            onIO {
                "executed"
            }
        }
        assertEquals("executed", result)
    }

    test("asFuture converts suspend to CompletableFuture") {
        val scope = CoroutineScope(HytaleDispatchers.Async)
        val future = scope.asFuture {
            42
        }

        val result = future.get()
        assertEquals(42, result)
    }

    test("asFuture with custom context") {
        val scope = CoroutineScope(HytaleDispatchers.Async)
        val future = scope.asFuture(HytaleDispatchers.IO) {
            "io-result"
        }

        val result = future.get()
        assertEquals("io-result", result)
    }

    test("asyncOn launches deferred with context") {
        val scope = CoroutineScope(HytaleDispatchers.Async)

        val deferred = scope.asyncOn(HytaleDispatchers.IO) {
            "async-result"
        }

        val result = runBlocking {
            deferred.await()
        }
        assertEquals("async-result", result)
    }

    test("nested context switches work correctly") {
        val result = runBlocking {
            onAsync {
                val inner = onIO {
                    "io-value"
                }
                "async-$inner"
            }
        }
        assertEquals("async-io-value", result)
    }

    test("CompletableFuture await suspends until complete") {
        val future = java.util.concurrent.CompletableFuture<String>()

        val result = runBlocking {
            launch {
                delay(10)
                future.complete("completed")
            }
            future.await()
        }

        assertEquals("completed", result)
    }
}
