package aster.amo.kytale.dsl

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Creates a JSON configuration delegate using kotlinx.serialization.
 *
 * Provides a Kotlin-idiomatic approach to configuration using
 * data classes and kotlinx.serialization.
 *
 * Example:
 * ```kotlin
 * @Serializable
 * data class MyConfig(
 *     val maxPlayers: Int = 100,
 *     val welcomeMessage: String = "Welcome!"
 * )
 *
 * class MyPlugin(init: JavaPluginInit) : KotlinPlugin(init) {
 *     val config by jsonConfig<MyConfig>("config") { MyConfig() }
 *
 *     override fun start() {
 *         super.start()
 *         logger.info { config.welcomeMessage }
 *     }
 * }
 * ```
 *
 * @param T the configuration type (must be @Serializable)
 * @param fileName the config file name (without extension)
 * @param default factory function for default configuration
 * @return a property delegate for the JSON configuration
 */
inline fun <reified T : Any> JavaPlugin.jsonConfig(
    fileName: String,
    noinline default: () -> T
): JsonConfigDelegate<T> = JsonConfigDelegate(
    plugin = this,
    fileName = fileName,
    serializer = serializer(),
    defaultFactory = default
)

/**
 * Property delegate for kotlinx.serialization JSON configuration.
 *
 * @property plugin the owning plugin
 * @property fileName the configuration file name
 * @property serializer the kotlinx.serialization serializer
 * @property defaultFactory factory for creating default configuration
 */
class JsonConfigDelegate<T : Any>(
    private val plugin: JavaPlugin,
    private val fileName: String,
    private val serializer: KSerializer<T>,
    private val defaultFactory: () -> T
) : ReadWriteProperty<Any?, T> {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Volatile
    private var cached: T? = null

    private val configPath: Path
        get() {
            val safeName = plugin.name.split(":").last().replace(":", "_").replace("/", "_").replace("\\", "_")
            return File("mobs/$safeName/$fileName.json").toPath()
        }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (cached == null) {
            synchronized(this) {
                if (cached == null) {
                    cached = load()
                }
            }
        }
        return cached!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cached = value
        save(value)
    }

    private fun load(): T {
        val path = configPath
        return if (Files.exists(path)) {
            try {
                val content = Files.readString(path)
                json.decodeFromString(serializer, content)
            } catch (e: Exception) {
                val default = defaultFactory()
                save(default)
                default
            }
        } else {
            val default = defaultFactory()
            save(default)
            default
        }
    }

    /**
     * Saves the current configuration to disk.
     */
    fun save() {
        cached?.let { save(it) }
    }

    private fun save(value: T) {
        val path = configPath
        Files.createDirectories(path.parent)
        val content = json.encodeToString(serializer, value)
        Files.writeString(path, content)
    }

    /**
     * Reloads the configuration from disk.
     */
    fun reload() {
        synchronized(this) {
            cached = load()
        }
    }
}

/**
 * Creates a simple key-value configuration delegate.
 *
 * For basic configurations that don't need complex serialization.
 *
 * Example:
 * ```kotlin
 * val maxPlayers by configValue("max-players", 100)
 * val serverName by configValue("server-name", "My Server")
 * ```
 *
 * @param key the configuration key
 * @param default the default value
 * @return a property delegate for the configuration value
 */
fun <T : Any> configValue(key: String, default: T): ConfigValueDelegate<T> {
    return ConfigValueDelegate(key, default)
}

/**
 * Property delegate for individual configuration values.
 *
 * @property key the configuration key
 * @property default the default value
 */
class ConfigValueDelegate<T : Any>(
    private val key: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {

    @Volatile
    private var value: T = default

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    /**
     * Resets the value to default.
     */
    fun reset() {
        value = default
    }
}
