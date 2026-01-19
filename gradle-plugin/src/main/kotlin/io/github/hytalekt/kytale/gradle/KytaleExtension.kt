package io.github.hytalekt.kytale.gradle

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * Extension for configuring the Kytale gradle plugin.
 *
 * Provides settings for:
 * - Hytale installation detection and paths
 * - Server run configuration
 * - UI compilation (delegated to KytaleUiExtension)
 *
 * Example usage:
 * ```kotlin
 * kytale {
 *     // Server configuration
 *     patchline.set("release")  // or "pre-release"
 *     allowOp.set(true)         // Enable /op command
 *     includeLocalMods.set(true) // Load mods from local installation
 *
 *     // UI compilation
 *     ui {
 *         packages.set(listOf("com.example.mymod"))
 *     }
 * }
 * ```
 */
abstract class KytaleExtension @Inject constructor(project: Project) {

    /**
     * The Hytale installation directory.
     *
     * Auto-detected based on OS:
     * - Windows: %APPDATA%\Hytale
     * - macOS: ~/Application Support/Hytale
     * - Linux: ~/.var/app/com.hypixel.HytaleLauncher/data/Hytale
     */
    abstract val hytaleHome: DirectoryProperty

    /**
     * Path to the HytaleServer.jar file.
     *
     * Defaults to: {hytaleHome}/install/{patchline}/package/game/latest/Server/HytaleServer.jar
     */
    abstract val serverJar: RegularFileProperty

    /**
     * Path to the Assets.zip file.
     *
     * Defaults to: {hytaleHome}/install/{patchline}/package/game/latest/Assets.zip
     */
    abstract val assetsZip: RegularFileProperty

    /**
     * The patchline to use: "release" or "pre-release".
     *
     * Defaults to "release".
     */
    abstract val patchline: Property<String>

    /**
     * Whether to enable the --allow-op flag when running the server.
     *
     * When enabled, players can use `/op self` to grant themselves operator permissions.
     * Useful for testing commands.
     *
     * Defaults to false.
     */
    abstract val allowOp: Property<Boolean>

    /**
     * The directory where the server will run.
     *
     * Defaults to: {projectDir}/run
     */
    abstract val runDirectory: DirectoryProperty

    /**
     * Whether to include mods from the local Hytale installation.
     *
     * When enabled, mods from {hytaleHome}/UserData/Mods will be loaded.
     *
     * Defaults to false.
     */
    abstract val includeLocalMods: Property<Boolean>

    /**
     * Additional assets directories to include.
     *
     * The project's src/main/resources will be included by default.
     */
    abstract val additionalAssets: ListProperty<String>

    /**
     * Additional JVM arguments for the server.
     */
    abstract val jvmArgs: ListProperty<String>

    init {
        patchline.convention("release")
        allowOp.convention(false)
        runDirectory.convention(project.layout.projectDirectory.dir("run"))
        includeLocalMods.convention(false)
        additionalAssets.convention(emptyList())
        jvmArgs.convention(emptyList())
    }
}
