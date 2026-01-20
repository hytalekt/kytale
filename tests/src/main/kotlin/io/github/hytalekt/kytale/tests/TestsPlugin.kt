package io.github.hytalekt.kytale.tests

import io.github.hytalekt.kytale.KotlinPlugin
import io.github.hytalekt.kytale.tests.test.IntegrationTestRunner
import io.github.hytalekt.kytale.tests.test.commandDslTests
import io.github.hytalekt.kytale.tests.test.eventDslTests
import io.github.hytalekt.kytale.tests.test.hexweaveTests
import io.github.hytalekt.kytale.tests.test.cacheTests
import io.github.hytalekt.kytale.tests.test.mathTests
import io.github.hytalekt.kytale.tests.test.validationTests
import io.github.hytalekt.kytale.tests.test.resultTests
import io.github.hytalekt.kytale.tests.test.cooldownTests
import io.github.hytalekt.kytale.tests.test.vectorTests
import io.github.hytalekt.kytale.tests.test.collectionTests
import io.github.hytalekt.kytale.tests.test.playerUtilTests
import io.github.hytalekt.kytale.tests.test.configDslTests
import io.github.hytalekt.kytale.tests.test.schedulerDslTests
import io.github.hytalekt.kytale.tests.test.coroutinesDispatcherTests
import io.github.hytalekt.kytale.tests.test.coroutinesPluginScopeTests
import io.github.hytalekt.kytale.tests.test.coroutinesAsyncExtensionsTests
import io.github.hytalekt.kytale.tests.test.uiAnchorTests
import io.github.hytalekt.kytale.tests.test.uiPaddingTests
import io.github.hytalekt.kytale.tests.test.uiStylesTests
import io.github.hytalekt.kytale.tests.test.uiElementsTests
import io.github.hytalekt.kytale.tests.test.uiPageTests
import io.github.hytalekt.kytale.tests.test.uiBuilderDslTests
import io.github.hytalekt.kytale.tests.test.uiEnumsTests
import io.github.hytalekt.kytale.tests.test.uiInteractiveTests
import io.github.hytalekt.kytale.tests.test.uiMoreElementsTests
import io.github.hytalekt.kytale.tests.test.uiSpriteAndImageTests
import io.github.hytalekt.kytale.tests.test.uiToggleRadioSeparatorTests
import io.github.hytalekt.kytale.tests.test.uiTabTests
import io.github.hytalekt.kytale.tests.test.uiItemElementsTests
import io.github.hytalekt.kytale.tests.test.uiInteractiveGroupBuilderTests
import io.github.hytalekt.kytale.tests.test.uiIconButtonStyleTests
import io.github.hytalekt.kytale.tests.test.uiWrappersTests
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import java.lang.System.Logger.Level

/**
 * Test plugin for verifying Kytale features via integration tests.
 *
 * When running on the server, this plugin executes integration tests to verify
 * that Kytale APIs work correctly with real Hytale classes.
 *
 * Run with: ./gradlew :tests:runServer
 *
 * Environment variables:
 * - KYTALE_RUN_TESTS=true - Run integration tests on startup (default: true)
 * - KYTALE_SHUTDOWN_AFTER_TESTS=true - Shutdown server after tests (for CI)
 */
class TestsPlugin(
    init: JavaPluginInit,
) : KotlinPlugin(init) {

    private val log = System.getLogger("KytaleTests")

    override fun setup() {
        super.setup()

        // Check if tests should run
        val runTests = System.getenv("KYTALE_RUN_TESTS")?.toBoolean() ?: true
        val shutdownAfter = System.getenv("KYTALE_SHUTDOWN_AFTER_TESTS")?.toBoolean() ?: false

        if (runTests) {
            runIntegrationTests(shutdownAfter)
        } else {
            log.log(Level.INFO, "Skipping integration tests (KYTALE_RUN_TESTS=false)")
        }
    }

    private fun runIntegrationTests(shutdownAfter: Boolean) {
        log.log(Level.INFO, "Running Kytale integration tests...")

        val runner = IntegrationTestRunner(this)

        // Register all test suites
        runner.commandDslTests()
        runner.eventDslTests()
        runner.hexweaveTests()

        // Util tests
        runner.cacheTests()
        runner.mathTests()
        runner.validationTests()
        runner.resultTests()
        runner.cooldownTests()

        // Extension tests
        runner.vectorTests()
        runner.collectionTests()
        runner.playerUtilTests()

        // Additional DSL tests (Config, Scheduler)
        runner.configDslTests()
        runner.schedulerDslTests()

        // Coroutines tests
        runner.coroutinesDispatcherTests()
        runner.coroutinesPluginScopeTests()
        runner.coroutinesAsyncExtensionsTests()

        // UI DSL tests
        runner.uiAnchorTests()
        runner.uiPaddingTests()
        runner.uiStylesTests()
        runner.uiElementsTests()
        runner.uiPageTests()
        runner.uiBuilderDslTests()
        runner.uiEnumsTests()
        runner.uiInteractiveTests()
        runner.uiMoreElementsTests()
        runner.uiSpriteAndImageTests()
        runner.uiToggleRadioSeparatorTests()
        runner.uiTabTests()
        runner.uiItemElementsTests()
        runner.uiInteractiveGroupBuilderTests()
        runner.uiIconButtonStyleTests()
        runner.uiWrappersTests()

        // Run all tests
        val allPassed = runner.run(shutdownAfter = shutdownAfter)

        if (!allPassed && !shutdownAfter) {
            log.log(Level.WARNING, "Some tests failed. See output above for details.")
        }
    }
}
