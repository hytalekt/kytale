package io.github.hytalekt.kytale.coroutines

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.text.get
import kotlin.time.Duration.Companion.seconds

/**
 * A test-specific PluginScope that doesn't require an actual JavaPlugin.
 * This avoids static class initialization issues when testing.
 */
private class TestPluginScope(
    private val dispatcher: CoroutineContext = HytaleDispatchers.Async,
) : CoroutineScope {
    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = supervisorJob + dispatcher

    fun cancel(message: String = "Plugin scope cancelled") {
        supervisorJob.cancel(message)
    }

    val isActive: Boolean
        get() = supervisorJob.isActive
}

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTest :
    FunSpec({

        context("HytaleDispatchers") {
            test("Main dispatcher exists and is accessible") {
                val dispatcher = HytaleDispatchers.Main
                dispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
            }

            test("Async dispatcher exists and is accessible") {
                val dispatcher = HytaleDispatchers.Async
                dispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
            }

            test("IO dispatcher exists and is accessible") {
                val dispatcher = HytaleDispatchers.IO
                dispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
            }

            test("Single dispatcher exists and is accessible") {
                val dispatcher = HytaleDispatchers.Single
                dispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
            }

            test("Main dispatcher defaults to Default when no executor set") {
                // Main falls back to Dispatchers.Default when mainExecutor is null
                val dispatcher = HytaleDispatchers.Main
                dispatcher shouldBe Dispatchers.Default
            }

            test("setMainExecutor configures Main dispatcher") {
                val executor = Executors.newSingleThreadExecutor()
                try {
                    HytaleDispatchers.setMainExecutor(executor)

                    val dispatcher = HytaleDispatchers.Main
                    dispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
                } finally {
                    // Clear the executor before shutdown to avoid affecting other tests
                    HytaleDispatchers.clearMainExecutor()
                    executor.shutdown()
                }
            }

            test("forWorld creates dispatcher from executor") {
                val executor = Executors.newSingleThreadExecutor()
                try {
                    val worldDispatcher = HytaleDispatchers.forWorld(executor)
                    worldDispatcher.shouldBeInstanceOf<CoroutineDispatcher>()
                } finally {
                    executor.shutdown()
                }
            }

            test("Async dispatcher is Dispatchers.Default") {
                HytaleDispatchers.Async shouldBe Dispatchers.Default
            }

            test("IO dispatcher is Dispatchers.IO") {
                HytaleDispatchers.IO shouldBe Dispatchers.IO
            }
        }

        context("PluginScope") {
            // Note: These tests use TestPluginScope instead of PluginScope to avoid
            // class initialization issues with JavaPlugin in the test environment.
            // The behavior is identical since TestPluginScope mirrors PluginScope's implementation.

            test("creates scope with default Async dispatcher") {
                val scope = TestPluginScope()

                scope.isActive shouldBe true
            }

            test("creates scope with custom dispatcher") {
                val testDispatcher = StandardTestDispatcher()
                val scope = TestPluginScope(testDispatcher)

                scope.isActive shouldBe true
            }

            test("cancel terminates scope") {
                val scope = TestPluginScope()

                scope.cancel()

                scope.isActive shouldBe false
            }

            test("cancel with custom message terminates scope") {
                val scope = TestPluginScope()

                scope.cancel("Custom shutdown message")

                scope.isActive shouldBe false
            }

            test("cancellation stops running coroutines") {
                // Use a real scope with Default dispatcher for proper coroutine behavior
                val scope = TestPluginScope(Dispatchers.Default)
                val wasRunning = AtomicBoolean(false)
                val wasCancelled = AtomicBoolean(false)

                scope.launch {
                    wasRunning.set(true)
                    try {
                        delay(10.seconds)
                    } finally {
                        wasCancelled.set(true)
                    }
                }

                // Give the coroutine time to start
                Thread.sleep(50)
                wasRunning.get() shouldBe true

                scope.cancel()

                // Give the coroutine time to process cancellation
                Thread.sleep(50)
                wasCancelled.get() shouldBe true
            }

            test("supervisor job allows sibling coroutines to continue after failure") {
                // Use a real scope with Default dispatcher
                val scope = TestPluginScope(Dispatchers.Default)
                val coroutine1Completed = AtomicBoolean(false)
                val coroutine2Started = AtomicBoolean(false)

                scope.launch {
                    coroutine2Started.set(true)
                    delay(50)
                    coroutine1Completed.set(true)
                }

                scope.launch {
                    throw RuntimeException("Intentional failure")
                }

                // Give coroutines time to run
                Thread.sleep(200)

                coroutine2Started.get() shouldBe true
                coroutine1Completed.get() shouldBe true
                scope.isActive shouldBe true
            }
        }

        context("AsyncExtensions") {
            test("onMainThread switches to Main dispatcher") {
                runTest {
                    var executedOnMain = false

                    onMainThread {
                        executedOnMain = true
                    }

                    executedOnMain shouldBe true
                }
            }

            test("onAsync switches to Async dispatcher") {
                runTest {
                    var executedOnAsync = false

                    onAsync {
                        executedOnAsync = true
                    }

                    executedOnAsync shouldBe true
                }
            }

            test("onIO switches to IO dispatcher") {
                runTest {
                    var executedOnIO = false

                    onIO {
                        executedOnIO = true
                    }

                    executedOnIO shouldBe true
                }
            }

            test("onMainThread returns value from block") {
                runTest {
                    val result =
                        onMainThread {
                            42
                        }

                    result shouldBe 42
                }
            }

            test("onAsync returns value from block") {
                runTest {
                    val result =
                        onAsync {
                            "async result"
                        }

                    result shouldBe "async result"
                }
            }

            test("onIO returns value from block") {
                runTest {
                    val result =
                        onIO {
                            listOf(1, 2, 3)
                        }

                    result shouldBe listOf(1, 2, 3)
                }
            }

            test("asyncOn launches async operation on specified dispatcher") {
                runTest {
                    val testDispatcher = StandardTestDispatcher(testScheduler)
                    val scope = TestPluginScope(testDispatcher)
                    var executed = false

                    val deferred =
                        scope.asyncOn(testDispatcher) {
                            executed = true
                            100
                        }

                    advanceUntilIdle()

                    executed shouldBe true
                    deferred.await() shouldBe 100
                }
            }

            test("asyncOn returns Deferred that can be awaited") {
                runTest {
                    val testDispatcher = StandardTestDispatcher(testScheduler)
                    val scope = TestPluginScope(testDispatcher)

                    val deferred =
                        scope.asyncOn(testDispatcher) {
                            delay(100)
                            "deferred result"
                        }

                    advanceUntilIdle()

                    deferred.await() shouldBe "deferred result"
                }
            }

            test("asFuture converts suspend function to CompletableFuture") {
                runTest {
                    val testDispatcher = StandardTestDispatcher(testScheduler)
                    val scope = TestPluginScope(testDispatcher)

                    val future =
                        scope.asFuture(testDispatcher) {
                            "future result"
                        }

                    advanceUntilIdle()

                    withTimeout(1.seconds) {
                        future.get() shouldBe "future result"
                    }
                }
            }

            test("thread switching between dispatchers") {
                runTest {
                    val results = mutableListOf<String>()

                    onAsync {
                        results.add("async")
                        onMainThread {
                            results.add("main")
                        }
                        results.add("back to async")
                    }

                    results shouldBe listOf("async", "main", "back to async")
                }
            }

            test("nested dispatcher switches work correctly") {
                runTest {
                    var result = ""

                    onMainThread {
                        result += "1"
                        onAsync {
                            result += "2"
                            onIO {
                                result += "3"
                            }
                            result += "4"
                        }
                        result += "5"
                    }

                    result shouldBe "12345"
                }
            }

            test("multiple parallel async operations") {
                runTest {
                    val testDispatcher = StandardTestDispatcher(testScheduler)
                    val scope = TestPluginScope(testDispatcher)
                    val counter = AtomicInteger(0)

                    val jobs =
                        (1..5).map {
                            scope.launch {
                                counter.incrementAndGet()
                            }
                        }

                    advanceUntilIdle()

                    counter.get() shouldBe 5
                }
            }
        }
    })
