package io.github.hytalekt.kytale.tests.test

import com.hypixel.hytale.server.core.HytaleServer
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import java.lang.System.Logger.Level

/**
 * Lightweight integration test runner for Hytale server tests.
 *
 * Tests run inside the actual plugin on server startup, with real Hytale APIs available.
 * Results are logged to the server console and optionally the server shuts down after tests.
 *
 * Example:
 * ```kotlin
 * val runner = IntegrationTestRunner(plugin)
 * runner.suite("CommandDsl") {
 *     test("registers command with name") {
 *         val cmd = commandRegistry.getCommand("test")
 *         assertNotNull(cmd)
 *         assertEquals("test", cmd.name)
 *     }
 * }
 * runner.run(shutdownAfter = true)
 * ```
 */
class IntegrationTestRunner(private val plugin: JavaPlugin) {
    private val suites = mutableListOf<TestSuite>()
    private val logger = System.getLogger("IntegrationTests")

    // ANSI color codes
    private object Colors {
        const val RESET = "\u001B[0m"
        const val GREEN = "\u001B[32m"
        const val RED = "\u001B[31m"
        const val YELLOW = "\u001B[33m"
        const val CYAN = "\u001B[36m"
        const val BOLD = "\u001B[1m"
    }

    /**
     * Defines a test suite with the given name.
     */
    fun suite(name: String, block: TestSuite.() -> Unit) {
        val suite = TestSuite(name, plugin)
        suite.block()
        suites.add(suite)
    }

    /**
     * Runs all registered test suites.
     *
     * @param shutdownAfter if true, shuts down the server after tests complete (for CI)
     * @return true if all tests passed, false otherwise
     */
    fun run(shutdownAfter: Boolean = false): Boolean {
        logger.log(Level.INFO, "")
        logger.log(Level.INFO, "${Colors.CYAN}============================================================${Colors.RESET}")
        logger.log(Level.INFO, "${Colors.CYAN}${Colors.BOLD}  KYTALE TEST SUITE${Colors.RESET}")
        logger.log(Level.INFO, "${Colors.CYAN}============================================================${Colors.RESET}")

        var totalPassed = 0
        var totalFailed = 0
        var totalSkipped = 0
        val failedTests = mutableListOf<Pair<String, String>>()

        for (suite in suites) {
            val results = suite.run()
            logger.log(Level.INFO, "")
            logger.log(Level.INFO, "${Colors.BOLD}--- ${suite.name} ---${Colors.RESET}")

            for (result in results) {
                when (result.status) {
                    TestStatus.PASSED -> {
                        totalPassed++
                        logger.log(Level.INFO, "  ${Colors.GREEN}[PASS]${Colors.RESET} ${result.name}")
                    }
                    TestStatus.FAILED -> {
                        totalFailed++
                        failedTests.add(suite.name to result.name)
                        logger.log(Level.ERROR, "  ${Colors.RED}[FAIL]${Colors.RESET} ${result.name}")
                        result.error?.let { error ->
                            logger.log(Level.ERROR, "      ${Colors.RED}-> ${error.message ?: error::class.simpleName}${Colors.RESET}")
                        }
                    }
                    TestStatus.SKIPPED -> {
                        totalSkipped++
                        logger.log(Level.WARNING, "  ${Colors.YELLOW}[SKIP]${Colors.RESET} ${result.name}")
                    }
                }
            }
        }

        logger.log(Level.INFO, "")
        logger.log(Level.INFO, "${Colors.CYAN}============================================================${Colors.RESET}")
        val allPassed = totalFailed == 0
        if (allPassed) {
            logger.log(Level.INFO, "  ${Colors.GREEN}${Colors.BOLD}RESULT: ALL TESTS PASSED${Colors.RESET}")
        } else {
            logger.log(Level.ERROR, "  ${Colors.RED}${Colors.BOLD}RESULT: TESTS FAILED${Colors.RESET}")
        }
        logger.log(Level.INFO, "  ${Colors.GREEN}Passed: $totalPassed${Colors.RESET} | ${Colors.RED}Failed: $totalFailed${Colors.RESET} | ${Colors.YELLOW}Skipped: $totalSkipped${Colors.RESET}")
        logger.log(Level.INFO, "${Colors.CYAN}============================================================${Colors.RESET}")

        if (failedTests.isNotEmpty()) {
            logger.log(Level.ERROR, "")
            logger.log(Level.ERROR, "${Colors.RED}Failed tests:${Colors.RESET}")
            for ((suiteName, testName) in failedTests) {
                logger.log(Level.ERROR, "  ${Colors.RED}- $suiteName :: $testName${Colors.RESET}")
            }
        }

        logger.log(Level.INFO, "")

        if (shutdownAfter) {
            logger.log(Level.INFO, "Shutting down server after tests...")
            HytaleServer.get().shutdownServer()
        }

        return allPassed
    }
}

/**
 * A group of related tests.
 */
class TestSuite(val name: String, val plugin: JavaPlugin) {
    private val tests = mutableListOf<TestCase>()

    /**
     * Adds a test to this suite.
     */
    fun test(name: String, block: TestContext.() -> Unit) {
        tests.add(TestCase(name, block))
    }

    /**
     * Adds a skipped test.
     */
    fun skip(name: String, reason: String = "") {
        tests.add(TestCase(name, skip = true, skipReason = reason))
    }

    internal fun run(): List<TestResult> {
        return tests.map { testCase ->
            if (testCase.skip) {
                TestResult(testCase.name, TestStatus.SKIPPED, null)
            } else {
                try {
                    val context = TestContext(plugin)
                    testCase.block!!.invoke(context)
                    TestResult(testCase.name, TestStatus.PASSED, null)
                } catch (e: AssertionError) {
                    TestResult(testCase.name, TestStatus.FAILED, e)
                } catch (e: Exception) {
                    TestResult(testCase.name, TestStatus.FAILED, e)
                }
            }
        }
    }
}

/**
 * Context provided to each test, with access to plugin and assertion helpers.
 */
class TestContext(val plugin: JavaPlugin) {

    /**
     * Asserts that a condition is true.
     */
    fun assertTrue(condition: Boolean, message: String = "Expected true but was false") {
        if (!condition) throw AssertionError(message)
    }

    /**
     * Asserts that a condition is false.
     */
    fun assertFalse(condition: Boolean, message: String = "Expected false but was true") {
        if (condition) throw AssertionError(message)
    }

    /**
     * Asserts that two values are equal.
     */
    fun <T> assertEquals(expected: T, actual: T, message: String? = null) {
        if (expected != actual) {
            val msg = message ?: "Expected <$expected> but was <$actual>"
            throw AssertionError(msg)
        }
    }

    /**
     * Asserts that a value is not null.
     */
    fun <T> assertNotNull(value: T?, message: String = "Expected non-null value but was null"): T {
        if (value == null) throw AssertionError(message)
        return value
    }

    /**
     * Asserts that a value is null.
     */
    fun assertNull(value: Any?, message: String = "Expected null but was non-null") {
        if (value != null) throw AssertionError(message)
    }

    /**
     * Asserts that the block throws an exception of the specified type.
     */
    inline fun <reified T : Throwable> assertThrows(message: String? = null, block: () -> Unit): T {
        try {
            block()
            throw AssertionError(message ?: "Expected ${T::class.simpleName} but no exception was thrown")
        } catch (e: Throwable) {
            if (e is T) return e
            if (e is AssertionError) throw e
            throw AssertionError(message ?: "Expected ${T::class.simpleName} but got ${e::class.simpleName}: ${e.message}")
        }
    }

    /**
     * Asserts that a collection contains the expected element.
     */
    fun <T> assertContains(collection: Collection<T>, element: T, message: String? = null) {
        if (element !in collection) {
            val msg = message ?: "Expected collection to contain <$element>"
            throw AssertionError(msg)
        }
    }

    /**
     * Asserts that a string contains the expected substring.
     */
    fun assertContains(string: String, substring: String, message: String? = null) {
        if (substring !in string) {
            val msg = message ?: "Expected string to contain <$substring>"
            throw AssertionError(msg)
        }
    }

    /**
     * Asserts that a collection has the expected size.
     */
    fun assertSize(expected: Int, collection: Collection<*>, message: String? = null) {
        if (collection.size != expected) {
            val msg = message ?: "Expected size <$expected> but was <${collection.size}>"
            throw AssertionError(msg)
        }
    }

    /**
     * Fails the test with the given message.
     */
    fun fail(message: String): Nothing {
        throw AssertionError(message)
    }
}

/**
 * Internal test case representation.
 */
internal class TestCase(
    val name: String,
    val block: (TestContext.() -> Unit)? = null,
    val skip: Boolean = false,
    val skipReason: String = ""
)

/**
 * Result of a single test execution.
 */
data class TestResult(
    val name: String,
    val status: TestStatus,
    val error: Throwable?
)

/**
 * Possible test outcomes.
 */
enum class TestStatus {
    PASSED,
    FAILED,
    SKIPPED
}
