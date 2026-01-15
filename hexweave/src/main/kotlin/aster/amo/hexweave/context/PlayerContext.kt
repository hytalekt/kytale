package aster.amo.hexweave.context

import aster.amo.hexweave.internal.HexweaveScope
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef

/**
 * Base context passed to Hexweave player handlers.
 *
 * Provides access to the player reference and plugin logger.
 */
open class PlayerContext @PublishedApi internal constructor(
    val plugin: JavaPlugin,
    @PublishedApi internal val scope: HexweaveScope,
    val playerRef: PlayerRef
) {
    val logger get() = plugin.logger
}

/**
 * Extended context for chat event handlers.
 */
class PlayerChatContext internal constructor(
    plugin: JavaPlugin,
    scope: HexweaveScope,
    playerRef: PlayerRef,
    val chatEvent: PlayerChatEvent
) : PlayerContext(plugin, scope, playerRef) {
    /** The player who sent the message. */
    val sender get() = chatEvent.sender
}
