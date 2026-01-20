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
