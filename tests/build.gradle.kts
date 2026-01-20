plugins {
    id("buildlogic.common")
    id("com.gradleup.shadow")
}

// Create fat JAR with all dependencies for Hytale plugin
tasks.shadowJar {
    archiveClassifier.set("") // Remove "-all" suffix
    mergeServiceFiles()
}

dependencies {
    compileOnly(libs.hytale.stubs)

    implementation(project(":"))
    implementation(project(":coroutines"))
    implementation(project(":serialization"))

    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}

// Replace placeholders in manifest.json
tasks.processResources {
    val props =
        mapOf(
            "plugin_group" to "io.github.hytalekt",
            "plugin_name" to "KytaleTests",
            "plugin_version" to project.version.toString(),
            "plugin_description" to "Test plugin for verifying Kytale features",
            "plugin_author" to "Kytale",
            "plugin_website" to "https://github.com/hytalekt/kytale",
            "server_version" to "*",
            "plugin_main_entrypoint" to "io.github.hytalekt.kytale.tests.TestsPlugin",
        )
    inputs.properties(props)
    filesMatching("manifest.json") {
        expand(props)
    }
}

// Test suite task - runs all tests on the Hytale server
// Usage: ./gradlew tests
val kytaleExt = project.extensions.getByType(io.github.hytalekt.kytale.gradle.KytaleExtension::class.java)

// Custom output filter that only shows test results and key events
class TestOutputFilter(
    private val delegate: java.io.OutputStream,
) : java.io.OutputStream() {
    private val buffer = StringBuilder()
    private var serverReady = false
    private var testingStarted = false

    private val passPatterns =
        listOf(
            "IntegrationTests", // All test output
            "KYTALE TEST SUITE", // Header
            "KytaleTests] Running", // Test start message
            "Enabled plugin", // Plugin loaded
            "Shutdown completed", // Clean exit
        )

    override fun write(b: Int) {
        if (b == '\n'.code) {
            processLine(buffer.toString())
            buffer.clear()
        } else {
            buffer.append(b.toChar())
        }
    }

    private fun processLine(line: String) {
        // Strip ANSI codes for pattern matching only
        val cleanLine = line.replace(Regex("\u001B\\[[;\\d]*m"), "").trim()

        // Always show test output
        if (passPatterns.any { cleanLine.contains(it) }) {
            // Remove timestamp and logger prefix but preserve ANSI codes
            // Pattern: optional ANSI codes, then [timestamp], then whitespace, then [logger], then content
            val displayLine =
                line
                    .replace(Regex("^(\u001B\\[[;\\d]*m)*\\[.*?]\\s*"), "") // Remove timestamp (with any leading ANSI)
                    .replace(Regex("^(\u001B\\[[;\\d]*m)*\\[.*?]\\s*"), "") // Remove logger name
                    .trim()

            if (displayLine.isNotEmpty()) {
                delegate.write((displayLine + "\n").toByteArray())
            }
        }
        // Show server ready message once
        else if (!serverReady && cleanLine.contains("Setup phase completed")) {
            delegate.write("\u001B[32mHytale server: ready\u001B[0m\n".toByteArray())
            serverReady = true
        }
    }

    override fun flush() = delegate.flush()

    override fun close() {
        if (buffer.isNotEmpty()) processLine(buffer.toString())
        delegate.close()
    }
}

tasks.register<JavaExec>("tests") {
    group = "verification"
    description = "Run all Kytale tests on the Hytale server"
    dependsOn("installPlugin")

    val cacheDir = layout.buildDirectory.dir("hytale")
    val runDir = layout.projectDirectory.dir("run")

    // Capture values at configuration time for configuration cache compatibility
    val serverJarProvider = kytaleExt.serverJar
    val assetsZipProvider = kytaleExt.assetsZip

    standardInput = System.`in`
    workingDir(runDir)
    classpath(cacheDir.get().file("HytaleServer.jar"))

    // Set environment variable for auto-shutdown after tests
    environment("KYTALE_SHUTDOWN_AFTER_TESTS", "true")

    doFirst {
        // Filter output to only show test results (must be set at execution time for config cache)
        standardOutput = TestOutputFilter(System.out)
        errorOutput = TestOutputFilter(System.err)
        val serverJar = serverJarProvider.orNull?.asFile
        val assetsZip = assetsZipProvider.orNull?.asFile

        if (serverJar == null || !serverJar.exists()) {
            throw GradleException(
                "Hytale server JAR not found. Make sure Hytale is installed.",
            )
        }
        if (assetsZip == null || !assetsZip.exists()) {
            throw GradleException(
                "Hytale Assets.zip not found. Make sure Hytale is installed.",
            )
        }

        args =
            mutableListOf(
                "--assets",
                assetsZip.absolutePath,
                "--allow-op",
            )
    }
}
