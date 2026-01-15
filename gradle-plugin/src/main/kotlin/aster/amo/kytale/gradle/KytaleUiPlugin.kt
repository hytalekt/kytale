package aster.amo.kytale.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer

/**
 * Gradle plugin for compiling Kytale UI DSL definitions to .ui files.
 *
 * Usage in build.gradle.kts:
 * ```kotlin
 * plugins {
 *     id("aster.amo.kytale.ui")
 * }
 *
 * kytaleUi {
 *     // Optional: limit scanning to specific packages (faster builds)
 *     packages.set(listOf("com.example.mymod"))
 *
 *     // Optional: specify output directory (defaults to src/main/resources/Common/UI/Custom/Pages)
 *     outputDir.set(file("src/main/resources/Common/UI/Custom/Pages"))
 * }
 * ```
 *
 * Then annotate your UI definition objects:
 * ```kotlin
 * @UiDefinition
 * object MyGameUi {
 *     fun registerAll() {
 *         UiRegistry.register("MyGame/MainMenu", mainMenu)
 *     }
 *
 *     val mainMenu = uiPage("MainMenu") { ... }
 * }
 * ```
 */
class KytaleUiPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("kytaleUi", KytaleUiExtension::class.java, project)

        val compileUiTask = project.tasks.register("compileUi", JavaExec::class.java) { task: JavaExec ->
            task.group = "build"
            task.description = "Compile Kytale UI DSL definitions to .ui files"

            task.dependsOn("compileKotlin")
            task.dependsOn("compileJava")

            val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
            val mainSourceSet = sourceSets.getByName("main")
            task.classpath = mainSourceSet.compileClasspath.plus(mainSourceSet.output)

            task.mainClass.set("aster.amo.kytale.ui.dsl.UiScanner")

            task.doFirst { _: Task ->
                val args = mutableListOf(extension.outputDir.get().asFile.absolutePath)
                val packages = extension.packages.get()
                if (packages.isNotEmpty()) {
                    args.add(packages.joinToString(","))
                }
                task.setArgs(args)
            }
        }

        project.afterEvaluate {
            if (extension.compileBeforeProcessResources.get()) {
                project.tasks.named("processResources").configure { task: Task ->
                    task.dependsOn(compileUiTask)
                }
            }
        }
    }
}
