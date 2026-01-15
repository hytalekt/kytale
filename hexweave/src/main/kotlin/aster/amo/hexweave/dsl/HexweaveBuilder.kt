package aster.amo.hexweave.dsl

import aster.amo.hexweave.dsl.commands.HexweaveCommandsScope
import aster.amo.hexweave.dsl.events.HexweaveEventsScope
import aster.amo.hexweave.dsl.players.HexweavePlayersScope
import aster.amo.hexweave.dsl.tasks.HexweaveTasksScope
import aster.amo.hexweave.dsl.systems.HexweaveSystemsScope
import aster.amo.hexweave.internal.HexweaveScope
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * Root builder that exposes all Hexweave DSL entry points.
 *
 * Access via [aster.amo.hexweave.enableHexweave]:
 * ```kotlin
 * enableHexweave {
 *     players { /* player lifecycle hooks */ }
 *     events { /* event subscriptions */ }
 *     commands { /* command registration */ }
 *     tasks { /* scheduled/repeating tasks */ }
 *     systems { /* ECS event systems */ }
 * }
 * ```
 */
@HexweaveDsl
class HexweaveBuilder internal constructor(
    internal val plugin: JavaPlugin,
    internal val scope: HexweaveScope
) {
    /** Configure player lifecycle hooks (onJoin, onLeave, etc.). */
    fun players(block: HexweavePlayersScope.() -> Unit) {
        HexweavePlayersScope(plugin, scope).apply(block)
    }

    /** Subscribe to Hytale events. */
    fun events(block: HexweaveEventsScope.() -> Unit) {
        HexweaveEventsScope(plugin).apply(block)
    }

    /** Register commands. */
    fun commands(block: HexweaveCommandsScope.() -> Unit) {
        HexweaveCommandsScope(plugin, scope).apply(block)
    }

    /** Schedule one-shot or repeating tasks. */
    fun tasks(block: HexweaveTasksScope.() -> Unit) {
        HexweaveTasksScope(plugin, scope.coroutineScope).apply(block)
    }

    /** Register ECS event systems. */
    fun systems(block: HexweaveSystemsScope.() -> Unit) {
        HexweaveSystemsScope(plugin, scope).apply(block)
    }
}
