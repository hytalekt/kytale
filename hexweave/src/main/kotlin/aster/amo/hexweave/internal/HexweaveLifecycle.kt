package aster.amo.hexweave.internal

import aster.amo.hexweave.internal.system.HexweaveSystemRegistry
import aster.amo.kytale.coroutines.pluginScope
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Tracks Hexweave state for each plugin and exposes a shared coroutine scope.
 */
internal object HexweaveLifecycle {
    private val scopes = ConcurrentHashMap<JavaPlugin, HexweaveScope>()

    fun ensureScope(plugin: JavaPlugin): HexweaveScope =
        scopes.computeIfAbsent(plugin) { javaPlugin ->
            val pluginScope = javaPlugin.pluginScope()
            val playerService = HexweavePlayerService(javaPlugin).apply {
                start()
            }
            val systemRegistry = HexweaveSystemRegistry(javaPlugin)
            HexweaveScope(
                plugin = javaPlugin,
                coroutineScope = pluginScope,
                players = playerService,
                systems = systemRegistry
            )
        }

    /**
     * Boots the system registry after DSL building is complete.
     */
    fun bootSystems(plugin: JavaPlugin) {
        scopes[plugin]?.let { scope ->
            scope.systems.boot(scope)
        }
    }

    fun shutdown(plugin: JavaPlugin) {
        scopes.remove(plugin)?.let { scope ->
            scope.systems.shutdown()
            scope.players.shutdown()
            scope.coroutineScope.cancel("Hexweave shutdown")
        }
    }
}
