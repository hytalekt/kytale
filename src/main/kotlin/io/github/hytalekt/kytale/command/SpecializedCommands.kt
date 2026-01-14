package io.github.hytalekt.kytale.command

/**
 * Specialized command builders for common command patterns.
 *
 * These provide convenient shortcuts for creating specific types of commands.
 */

/**
 * Creates a player-only command (requires sender to be a player)
 *
 * Example:
 * ```
 * val giveCommand = playerCommand("give") {
 *     description("Give yourself an item")
 *     argument("item") {
 *         type(ArgumentTypes.ITEM)
 *         required()
 *     }
 *     execute { player ->
 *         val item = get<String>("item")
 *         // Give item to player
 *     }
 * }
 * ```
 */
@CommandDsl
class PlayerCommandBuilder(
    private val name: String,
    private val aliases: Array<out String>,
) {
    private val baseCommand = CommandBuilder(name, aliases)

    fun description(text: String) = baseCommand.description(text)

    fun permission(perm: String) = baseCommand.permission(perm)

    fun argument(
        name: String,
        configure: ArgumentBuilder.() -> Unit,
    ) = baseCommand.argument(name, configure)

    /**
     * Execute block with player context
     */
    fun execute(handler: PlayerCommandContext.() -> Unit) {
        // TODO: Wrap executor to ensure sender is a player
    }

    fun build() = baseCommand.build()
}

/**
 * Context for player commands with guaranteed player sender
 */
class PlayerCommandContext : CommandContext() {
    // TODO: Add player property
    // val player: Player
}

/**
 * Creates an async command that runs off the main thread
 *
 * Example:
 * ```
 * val slowCommand = asyncCommand("calculate") {
 *     description("Perform a slow calculation")
 *     execute { context ->
 *         // This runs async
 *         val result = performSlowCalculation()
 *         // Reply to player
 *     }
 * }
 * ```
 */
@CommandDsl
class AsyncCommandBuilder(
    private val name: String,
    private val aliases: Array<out String>,
) {
    private val baseCommand = CommandBuilder(name, aliases)

    fun description(text: String) = baseCommand.description(text)

    fun permission(perm: String) = baseCommand.permission(perm)

    fun argument(
        name: String,
        configure: ArgumentBuilder.() -> Unit,
    ) = baseCommand.argument(name, configure)

    /**
     * Execute block that runs asynchronously
     */
    fun execute(handler: suspend CommandContext.() -> Unit) {
        // TODO: Wrap executor to run async
    }

    fun build() = baseCommand.build()
}

/**
 * Creates a command that targets a player
 *
 * Example:
 * ```
 * val kickCommand = targetPlayerCommand("kick") {
 *     description("Kick a player from the server")
 *     execute { sender, target ->
 *         // Kick the target player
 *     }
 * }
 * ```
 */
@CommandDsl
class TargetPlayerCommandBuilder(
    private val name: String,
    private val aliases: Array<out String>,
) {
    private val baseCommand = CommandBuilder(name, aliases)

    fun description(text: String) = baseCommand.description(text)

    fun permission(perm: String) = baseCommand.permission(perm)

    /**
     * Execute block with sender and target player
     */
    fun execute(handler: (sender: Any, target: Any) -> Unit) {
        // TODO: Implement target player resolution
    }

    fun build() = baseCommand.build()
}

/**
 * DSL function to create a player-only command
 */
fun playerCommand(
    name: String,
    vararg aliases: String,
    configure: PlayerCommandBuilder.() -> Unit,
) = PlayerCommandBuilder(name, aliases).apply(configure).build()

/**
 * DSL function to create an async command
 */
fun asyncCommand(
    name: String,
    vararg aliases: String,
    configure: AsyncCommandBuilder.() -> Unit,
) = AsyncCommandBuilder(name, aliases).apply(configure).build()

/**
 * DSL function to create a target player command
 */
fun targetPlayerCommand(
    name: String,
    vararg aliases: String,
    configure: TargetPlayerCommandBuilder.() -> Unit,
) = TargetPlayerCommandBuilder(name, aliases).apply(configure).build()
