package io.github.hytalekt.kytale.gradle

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

/**
 * Gradle plugin for Hytale mod development with Kytale.
 *
 * Provides tasks for running a Hytale server with your plugin loaded:
 * - `runServer` - Builds, installs, and launches the Hytale server
 * - `installPlugin` - Copies the built JAR to the run/mods directory
 *
 * Usage in build.gradle.kts:
 * ```kotlin
 * plugins {
 *     id("io.github.hytalekt.kytale")
 * }
 *
 * kytale {
 *     patchline.set("release")  // or "pre-release"
 *     allowOp.set(true)         // Enable /op command for testing
 * }
 * ```
 *
 * Then run: `./gradlew runServer`
 */
class KytalePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val ext = project.extensions.create("kytale", KytaleExtension::class.java, project)

        // Configure defaults that depend on detected Hytale home (lazy - only evaluated when needed)
        ext.hytaleHome.convention(project.layout.dir(project.provider {
            detectHytaleHome()?.toFile()
        }))
        ext.serverJar.convention(project.layout.file(project.provider {
            val home = ext.hytaleHome.orNull?.asFile?.toPath() ?: return@provider null
            getHytaleBasePath(home, ext.patchline.get()).resolve("Server/HytaleServer.jar").toFile()
        }))
        ext.assetsZip.convention(project.layout.file(project.provider {
            val home = ext.hytaleHome.orNull?.asFile?.toPath() ?: return@provider null
            getHytaleBasePath(home, ext.patchline.get()).resolve("Assets.zip").toFile()
        }))

        // Cache directory for the server JAR
        val cacheDir = project.layout.buildDirectory.dir("hytale")

        // Add flatDir repository for cached HytaleServer.jar
        project.repositories.flatDir { repo ->
            repo.name = "hytaleGenerated"
            repo.dir(cacheDir)
        }

        // Cache server JAR after evaluation (when user config is applied)
        project.afterEvaluate {
            val serverJarFile = ext.serverJar.orNull?.asFile
            if (serverJarFile != null && serverJarFile.exists()) {
                val serverJarPath = serverJarFile.toPath()
                val cacheDirPath = cacheDir.get().asFile.toPath()
                val localCopy = cacheDirPath.resolve("HytaleServer.jar")

                // Copy if different or doesn't exist
                if (!Files.exists(localCopy) || Files.mismatch(serverJarPath, localCopy) != -1L) {
                    Files.createDirectories(cacheDirPath)
                    Files.copy(serverJarPath, localCopy, StandardCopyOption.REPLACE_EXISTING)
                    project.logger.lifecycle("Cached HytaleServer.jar from ${serverJarFile.absolutePath}")
                }

                // Add to compileOnly if not already present
                val compileOnly = project.configurations.findByName("compileOnly")
                if (compileOnly != null) {
                    project.dependencies.add("compileOnly", project.files(localCopy.toFile()))
                }
            }
        }

        // installPlugin task - copies the plugin JAR to run/mods
        // Uses shadowJar if available, otherwise regular jar
        val installTask = project.tasks.register("installPlugin", Copy::class.java) { task ->
            task.group = "kytale"
            task.description = "Install the plugin JAR to the run/mods directory"
            task.into(ext.runDirectory.dir("mods"))
        }

        // Configure installPlugin after evaluation to detect shadowJar
        project.afterEvaluate {
            val jarTaskName = if (project.tasks.names.contains("shadowJar")) "shadowJar" else "jar"
            val jarTask = project.tasks.named(jarTaskName, Jar::class.java)

            installTask.configure { task ->
                task.dependsOn(jarTask)
                task.from(jarTask.flatMap { it.archiveFile })
            }
        }

        // runServer task - launches Hytale server with the plugin
        project.tasks.register("runServer", JavaExec::class.java) { task ->
            task.group = "kytale"
            task.description = "Run the Hytale server with the plugin installed"
            task.dependsOn(installTask)

            task.standardInput = System.`in`
            task.workingDir(ext.runDirectory)
            task.classpath(cacheDir.get().file("HytaleServer.jar"))

            // Validate Hytale installation before running
            task.doFirst {
                val serverJar = ext.serverJar.orNull?.asFile
                val assetsZip = ext.assetsZip.orNull?.asFile

                if (serverJar == null || !serverJar.exists()) {
                    throw GradleException(
                        "Hytale server JAR not found.\n" +
                        "Make sure Hytale is installed, or configure serverJar manually:\n" +
                        "  kytale {\n" +
                        "      serverJar.set(file(\"/path/to/HytaleServer.jar\"))\n" +
                        "  }"
                    )
                }
                if (assetsZip == null || !assetsZip.exists()) {
                    throw GradleException(
                        "Hytale Assets.zip not found.\n" +
                        "Make sure Hytale is installed, or configure assetsZip manually:\n" +
                        "  kytale {\n" +
                        "      assetsZip.set(file(\"/path/to/Assets.zip\"))\n" +
                        "  }"
                    )
                }
            }

            // Build server arguments
            task.doFirst {
                val args = mutableListOf(
                    "--assets", ext.assetsZip.get().asFile.absolutePath,
                    "--assets", project.layout.projectDirectory.dir("src/main/resources").asFile.absolutePath
                )

                // Add additional asset directories
                ext.additionalAssets.get().forEach { assetPath ->
                    args.add("--assets")
                    args.add(assetPath)
                }

                // Include local mods if enabled
                if (ext.includeLocalMods.get()) {
                    val localMods = ext.hytaleHome.dir("UserData/Mods").get().asFile.absolutePath
                    args.add("--mods=$localMods")
                }

                // Enable operator commands if requested
                if (ext.allowOp.get()) {
                    args.add("--allow-op")
                }

                task.args = args
            }

            // Apply custom JVM args
            task.doFirst {
                val customJvmArgs = ext.jvmArgs.get()
                if (customJvmArgs.isNotEmpty()) {
                    task.jvmArgs = customJvmArgs
                }
            }
        }
    }

    /**
     * Gets the base path for Hytale game files based on patchline.
     */
    private fun getHytaleBasePath(home: Path, patchline: String): Path {
        return home.resolve("install/$patchline/package/game/latest")
    }

    /**
     * Detects the Hytale installation directory based on the operating system.
     * Returns null if Hytale is not installed.
     */
    private fun detectHytaleHome(): Path? {
        val basePath = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> Path("${System.getenv("APPDATA")}\\Hytale")
            Os.isFamily(Os.FAMILY_MAC) -> Path("${System.getProperty("user.home")}/Library/Application Support/Hytale")
            Os.isFamily(Os.FAMILY_UNIX) -> Path("${System.getProperty("user.home")}/.var/app/com.hypixel.HytaleLauncher/data/Hytale")
            else -> return null
        }
        return if (basePath.exists()) basePath else null
    }
}
