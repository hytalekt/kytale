package aster.amo.hexweave.dsl.commands

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.internal.HexweaveScope
import aster.amo.kytale.dsl.CommandBuilder
import aster.amo.kytale.dsl.command
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World

/**
 * DSL scope for registering commands.
 *
 * ```kotlin
 * commands {
 *     literal("greet", "Greet the player") {
 *         executesPlayer {
 *             sendMessage(Message.raw("Hello!"))
 *         }
 *     }
 * }
 * ```
 */
@HexweaveDsl
class HexweaveCommandsScope internal constructor(
    private val plugin: JavaPlugin,
    private val scope: HexweaveScope
) {
    /** Register a command with the given name. */
    fun literal(
        name: String,
        description: String = "",
        block: HexweaveCommandBuilder.() -> Unit
    ) {
        plugin.command(name, description) {
            HexweaveCommandBuilder(this, scope).apply(block)
        }
    }
}

/**
 * Builder for Hexweave commands.
 */
class HexweaveCommandBuilder internal constructor(
    private val delegate: CommandBuilder,
    private val scope: HexweaveScope
) {
    /** Add command aliases. */
    fun aliases(vararg names: String) {
        delegate.aliases(*names)
    }

    /** Execute a generic async handler. */
    fun executes(block: suspend (CommandContext) -> Unit) {
        delegate.executes(block)
    }

    /** Execute a handler that requires a player sender. */
    fun executesPlayer(
        block: HexweaveCommandContext.() -> Unit
    ) {
        delegate.executesSync { ctx ->
            if (!ctx.isPlayer) {
                ctx.sendMessage(Message.raw("This command can only be used by players."))
                return@executesSync
            }
            block(HexweaveCommandContext(scope, ctx))
        }
    }

    /** Add a subcommand. */
    fun subcommand(
        name: String,
        description: String,
        block: HexweaveCommandBuilder.() -> Unit
    ) {
        delegate.subcommand(name, description) {
            HexweaveCommandBuilder(this, scope).apply(block)
        }
    }
}

/**
 * Context for command execution with Hexweave integration.
 */
class HexweaveCommandContext internal constructor(
    private val scope: HexweaveScope,
    val command: CommandContext
) {
    /**
     * The Player entity from the command sender.
     * Safe to access from any thread.
     */
    val player: Player
        get() = command.sender() as Player

    /**
     * The Player entity (alias for [player]).
     */
    val playerEntity: Player
        get() = player

    /**
     * Gets the world for this player. Safe to call from any thread.
     * Returns null if the player has no valid reference.
     */
    val world: World?
        get() = player.reference?.store?.externalData?.world

    /**
     * Executes a block on the world thread with the PlayerRef.
     * This is the safe way to access thread-sensitive resources.
     */
    inline fun onWorld(crossinline block: (PlayerRef) -> Unit) {
        val w = world ?: return
        w.execute {
            val playerRef = getPlayerRef()
            block(playerRef)
        }
    }

    /**
     * Gets the PlayerRef component. Must be called on the world thread.
     * Prefer using onWorld { } instead.
     */
    fun getPlayerRef(): PlayerRef {
        val ref = player.reference ?: error("Player has no entity reference")
        val store = ref.store
        return store.getComponent(ref, PlayerRef.getComponentType())
            ?: error("Could not get PlayerRef component for player")
    }

    fun sendMessage(message: Message) {
        command.sendMessage(message)
    }
}
