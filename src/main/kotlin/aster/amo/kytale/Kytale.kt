package aster.amo.kytale

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

/**
 * Kytale - Kotlin Language Loader for Hytale.
 *
 * Provides Kotlin runtime and idiomatic DSLs for Hytale server plugin development.
 * Bundles Kotlin stdlib, reflect, coroutines, and serialization libraries with
 * relocated packages to prevent conflicts with other plugins.
 *
 * @property init the plugin initialization context provided by the server
 */
class Kytale(init: JavaPluginInit) : JavaPlugin(init) {

    /**
     * Companion object holding the singleton instance and version information.
     */
    companion object {
        private lateinit var instance: Kytale

        /**
         * Returns the singleton instance of the Kytale plugin.
         *
         * @return the active Kytale instance
         * @throws IllegalStateException if accessed before plugin initialization
         */
        @JvmStatic
        fun getInstance(): Kytale = instance

        /**
         * The Kotlin version bundled with this loader.
         */
        const val KOTLIN_VERSION: String = "2.2.0"

        /**
         * The kotlinx.coroutines version bundled with this loader.
         */
        const val COROUTINES_VERSION: String = "1.9.0"

        /**
         * The kotlinx.serialization version bundled with this loader.
         */
        const val SERIALIZATION_VERSION: String = "1.7.3"
    }

    private val logger: HytaleLogger = HytaleLogger.forEnclosingClass()

    init {
        instance = this

        logger.atInfo().log(
            "Kytale v%s initialized - Kotlin %s, Coroutines %s, Serialization %s",
            manifest.version.toString(),
            KOTLIN_VERSION,
            COROUTINES_VERSION,
            SERIALIZATION_VERSION
        )
    }
}
