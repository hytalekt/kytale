package aster.amo.hexweave.dsl.players

import aster.amo.hexweave.context.PlayerContext
import aster.amo.hexweave.context.PlayerChatContext
import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.internal.HexweaveScope
import aster.amo.kytale.dsl.event
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerRefEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * DSL scope for player lifecycle hooks.
 *
 * ```kotlin
 * players {
 *     onJoin { logger.info { "Welcome ${playerRef.name}" } }
 *     onLeave { logger.info { "Goodbye ${playerRef.name}" } }
 *     onChat { /* handle chat messages */ }
 * }
 * ```
 */
@HexweaveDsl
class HexweavePlayersScope internal constructor(
    @PublishedApi internal val plugin: JavaPlugin,
    @PublishedApi internal val scope: HexweaveScope
) {
    /** Called when a player joins the server. */
    fun onJoin(handler: PlayerContext.() -> Unit) {
        plugin.event<PlayerConnectEvent> { event ->
            scope.players.handleJoin(event.playerRef)
            handler(PlayerContext(plugin, scope, event.playerRef))
        }
    }

    /** Called when a player leaves the server. */
    fun onLeave(handler: PlayerContext.() -> Unit) {
        plugin.event<PlayerDisconnectEvent> { event ->
            handler(PlayerContext(plugin, scope, event.playerRef))
            scope.players.handleLeave(event.playerRef.uuid)
        }
    }

    /** Called when a player sends a chat message. */
    fun onChat(
        filter: (PlayerChatEvent) -> Boolean = { true },
        handler: PlayerChatContext.() -> Unit
    ) {
        plugin.event(filter) { event ->
            handler(PlayerChatContext(plugin, scope, event.sender, event))
        }
    }

    /** Listen to any player-related event with PlayerContext. */
    inline fun <reified T> listen(
        noinline handler: PlayerContext.(T) -> Unit
    ) where T : PlayerRefEvent<*>, T : IBaseEvent<*> {
        plugin.event<T> { event ->
            handler(PlayerContext(plugin, scope, event.playerRef), event)
        }
    }
}
