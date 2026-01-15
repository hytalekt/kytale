package aster.amo.kytale.gradle

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * Extension for configuring the Kytale UI plugin.
 */
abstract class KytaleUiExtension @Inject constructor(project: Project) {

    /**
     * Packages to scan for @UiDefinition annotated classes.
     *
     * If empty, scans all packages in the classpath.
     * For faster builds, specify your mod's package(s).
     *
     * Example:
     * ```kotlin
     * kytaleUi {
     *     packages.set(listOf("com.example.mymod"))
     * }
     * ```
     */
    abstract val packages: ListProperty<String>

    /**
     * The output directory for compiled .ui files.
     *
     * Defaults to: src/main/resources/Common/UI/Custom/Pages
     */
    abstract val outputDir: DirectoryProperty

    /**
     * Whether to automatically run compileUi before processResources.
     *
     * Defaults to: false (to avoid circular dependency issues)
     */
    abstract val compileBeforeProcessResources: Property<Boolean>

    init {
        packages.convention(emptyList())
        outputDir.convention(project.layout.projectDirectory.dir("src/main/resources/Common/UI/Custom/Pages"))
        compileBeforeProcessResources.convention(false)
    }
}
