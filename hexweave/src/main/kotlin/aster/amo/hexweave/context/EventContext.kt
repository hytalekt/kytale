package aster.amo.hexweave.context

import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * Generic event context used by the Hexweave event DSL.
 */
class EventContext<T : IBaseEvent<*>>(
    val plugin: JavaPlugin,
    val event: T
) {
    val logger get() = plugin.logger
}
