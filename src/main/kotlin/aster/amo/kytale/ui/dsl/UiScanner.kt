package aster.amo.kytale.ui.dsl

import java.io.File

/**
 * Scans for @UiDefinition annotated classes and compiles all UI definitions.
 *
 * This is the entry point called by the Gradle plugin. It uses reflection to find
 * all classes annotated with @UiDefinition, calls their registerAll() method,
 * and then compiles all registered UI pages.
 */
object UiScanner {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            System.err.println("Usage: UiScanner <outputDir> [package1,package2,...]")
            System.exit(1)
        }

        val outputDir = File(args[0])
        val packages = if (args.size > 1) args[1].split(",") else emptyList()

        println("Scanning for @UiDefinition classes...")

        // Find and process all @UiDefinition annotated classes
        val definitionClasses = findUiDefinitions(packages)

        if (definitionClasses.isEmpty()) {
            println("No @UiDefinition classes found.")
            return
        }

        for (clazz in definitionClasses) {
            println("Found: ${clazz.name}")
            try {
                // Try to call registerAll() method
                val registerMethod = clazz.getDeclaredMethod("registerAll")

                // Get the singleton instance for objects, or create instance for classes
                val instance = try {
                    clazz.getDeclaredField("INSTANCE").get(null)
                } catch (e: NoSuchFieldException) {
                    clazz.getDeclaredConstructor().newInstance()
                }

                registerMethod.invoke(instance)
            } catch (e: NoSuchMethodException) {
                System.err.println("Warning: ${clazz.name} has @UiDefinition but no registerAll() method")
            } catch (e: Exception) {
                System.err.println("Error processing ${clazz.name}: ${e.message}")
                e.printStackTrace()
            }
        }

        // Compile all registered UIs
        val uiPageCount = UiRegistry.getPages().size
        val interactivePageCount = InteractiveUiRegistry.getPages().size

        if (uiPageCount > 0) {
            println("Compiling $uiPageCount UI pages...")
            UiRegistry.compileAll(outputDir)
        }

        if (interactivePageCount > 0) {
            println("Compiling $interactivePageCount interactive pages...")
            InteractiveUiRegistry.compileAll(outputDir)
        }

        println("Total: ${uiPageCount + interactivePageCount} pages compiled.")
    }

    private fun findUiDefinitions(packages: List<String>): List<Class<*>> {
        val results = mutableListOf<Class<*>>()
        val classLoader = Thread.currentThread().contextClassLoader

        // Use classpath scanning
        val classpath = System.getProperty("java.class.path")
        val entries = classpath.split(File.pathSeparator)

        for (entry in entries) {
            val file = File(entry)
            if (file.isDirectory) {
                // Scan directory for .class files
                scanDirectory(file, file, packages, results, classLoader)
            }
        }

        return results
    }

    private fun scanDirectory(
        root: File,
        dir: File,
        packages: List<String>,
        results: MutableList<Class<*>>,
        classLoader: ClassLoader
    ) {
        val files = dir.listFiles() ?: return

        for (file in files) {
            if (file.isDirectory) {
                scanDirectory(root, file, packages, results, classLoader)
            } else if (file.name.endsWith(".class") && !file.name.contains("$")) {
                val relativePath = file.relativeTo(root).path
                val className = relativePath
                    .removeSuffix(".class")
                    .replace(File.separatorChar, '.')

                // Filter by packages if specified
                if (packages.isNotEmpty() && packages.none { className.startsWith(it) }) {
                    continue
                }

                try {
                    val clazz = classLoader.loadClass(className)
                    if (clazz.isAnnotationPresent(UiDefinition::class.java)) {
                        results.add(clazz)
                    }
                } catch (e: Throwable) {
                    // Ignore classes that can't be loaded
                }
            }
        }
    }
}
