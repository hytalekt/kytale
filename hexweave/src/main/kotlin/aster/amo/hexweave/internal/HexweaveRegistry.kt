package aster.amo.hexweave.internal

import aster.amo.hexweave.internal.system.HexweaveSystemRegistry
import aster.amo.kytale.coroutines.PluginScope
import aster.amo.kytale.dsl.event
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Central scope holding all Hexweave services for a plugin.
 */
@PublishedApi
internal data class HexweaveScope(
    val plugin: JavaPlugin,
    val coroutineScope: PluginScope,
    val players: HexweavePlayerService,
    @property:PublishedApi internal val systems: HexweaveSystemRegistry
)

/**
 * Manages player join/leave lifecycle events.
 */
internal class HexweavePlayerService(
    private val plugin: JavaPlugin
) {
    private val bootstrapped = AtomicBoolean(false)
    private val joinHandlers = mutableListOf<(PlayerRef) -> Unit>()
    private val leaveHandlers = mutableListOf<(UUID) -> Unit>()

    fun start() {
        if (bootstrapped.getAndSet(true)) {
            return
        }

        plugin.event<PlayerConnectEvent> { event ->
            handleJoin(event.playerRef)
        }

        plugin.event<PlayerDisconnectEvent> { event ->
            handleLeave(event.playerRef.uuid)
        }
    }

    fun onJoin(handler: (PlayerRef) -> Unit) {
        joinHandlers.add(handler)
    }

    fun onLeave(handler: (UUID) -> Unit) {
        leaveHandlers.add(handler)
    }

    internal fun handleJoin(playerRef: PlayerRef) {
        joinHandlers.forEach { it(playerRef) }
    }

    internal fun handleLeave(uuid: UUID) {
        leaveHandlers.forEach { it(uuid) }
    }

    fun shutdown() {
        joinHandlers.clear()
        leaveHandlers.clear()
    }
}
