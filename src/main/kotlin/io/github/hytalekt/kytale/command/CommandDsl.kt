package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.AbstractCommand

/**
 * Improved command DSL based on Hytale's AbstractCommand system.
 *
 * Example usage:
 * ```
 * val myCommand = command("teleport", "tp") {
 *     description("Teleport to a location or player")
 *     permission("myplugin.teleport")
 *
 *     argument("target") {
 *         type(ArgumentTypes.PLAYER)
 *         required()
 *         suggest { context ->
 *             world.players.map { it.name }
 *         }
 *     }
 *
 *     argument("location") {
 *         type(ArgumentTypes.POSITION)
 *         optional()
 *     }
 *
 *     execute { context ->
 *         val target = context.get<Player>("target")
 *         val location = context.getOptional<Position>("location")
 *         // Handle teleportation
 *     }
 *
 *     subcommand("here") {
 *         description("Teleport a player to your location")
 *         execute { context ->
 *             // Handle teleport here
 *         }
 *     }
 * }
 * ```
 */
@DslMarker
annotation class CommandDsl

/**
 * Main command builder
 */
@CommandDsl
class CommandBuilder(
    private val name: String,
    private val aliases: Array<out String>,
) {
    private var description: String = ""
    private var permission: String? = null
    private val arguments = mutableListOf<ArgumentBuilder>()
    private val subcommands = mutableListOf<CommandBuilder>()
    private var executor: (CommandContext.() -> Unit)? = null

    /**
     * Sets the command description
     */
    fun description(text: String) {
        description = text
    }

    /**
     * Sets the required permission
     */
    fun permission(perm: String) {
        permission = perm
    }

    /**
     * Adds an argument to the command
     */
    fun argument(
        name: String,
        configure: ArgumentBuilder.() -> Unit,
    ) {
        arguments.add(ArgumentBuilder(name).apply(configure))
    }

    /**
     * Adds a required string argument (shorthand)
     */
    fun requiredString(name: String) =
        argument(name) {
            type(ArgumentTypes.STRING)
            required()
        }

    /**
     * Adds an optional string argument (shorthand)
     */
    fun optionalString(
        name: String,
        default: String? = null,
    ) = argument(name) {
        type(ArgumentTypes.STRING)
        optional(default)
    }

    /**
     * Adds a subcommand
     */
    fun subcommand(
        name: String,
        vararg aliases: String,
        configure: CommandBuilder.() -> Unit,
    ) {
        subcommands.add(CommandBuilder(name, aliases).apply(configure))
    }

    /**
     * Sets the command executor
     */
    fun execute(handler: CommandContext.() -> Unit) {
        executor = handler
    }

    /**
     * Builds the command
     */
    fun build(): AbstractCommand {
        TODO("Implement command building using AbstractCommand")
    }
}

/**
 * Builder for command arguments
 */
@CommandDsl
class ArgumentBuilder(
    private val name: String,
) {
    private var argType: String = "STRING"
    private var isRequired: Boolean = false
    private var isFlag: Boolean = false
    private var defaultValue: Any? = null
    private var suggestionProvider: ((CommandContext) -> List<String>)? = null

    /**
     * Sets the argument type
     */
    fun type(type: String) {
        argType = type
    }

    /**
     * Marks the argument as required
     */
    fun required() {
        isRequired = true
    }

    /**
     * Marks the argument as optional with an optional default value
     */
    fun optional(default: Any? = null) {
        isRequired = false
        defaultValue = default
    }

    /**
     * Marks the argument as a flag (boolean)
     */
    fun flag() {
        isFlag = true
        argType = ArgumentTypes.FLAG
    }

    /**
     * Sets a suggestion provider for tab completion
     */
    fun suggest(provider: (CommandContext) -> List<String>) {
        suggestionProvider = provider
    }

    /**
     * Sets static suggestions for tab completion
     */
    fun suggestions(vararg values: String) {
        suggestionProvider = { values.toList() }
    }
}

/**
 * Context for command execution
 */
open class CommandContext {
    private val arguments = mutableMapOf<String, Any>()

    /**
     * Gets a required argument value
     */
    fun <T> get(name: String): T {
        @Suppress("UNCHECKED_CAST")
        return arguments[name] as? T ?: throw IllegalArgumentException("Argument $name not found")
    }

    /**
     * Gets an optional argument value
     */
    fun <T> getOptional(name: String): T? {
        @Suppress("UNCHECKED_CAST")
        return arguments[name] as? T
    }

    /**
     * Checks if a flag is set
     */
    fun hasFlag(name: String): Boolean = arguments[name] as? Boolean ?: false
}

/**
 * Standard argument types for Hytale commands
 */
object ArgumentTypes {
    const val STRING = "STRING"
    const val INTEGER = "INTEGER"
    const val FLOAT = "FLOAT"
    const val DOUBLE = "DOUBLE"
    const val BOOLEAN = "BOOLEAN"
    const val PLAYER = "PLAYER"
    const val ENTITY = "ENTITY"
    const val POSITION = "POSITION"
    const val BLOCK_POSITION = "BLOCK_POSITION"
    const val ITEM = "ITEM"
    const val BLOCK_TYPE = "BLOCK_TYPE"
    const val ENUM = "ENUM"
    const val ASSET = "ASSET"
    const val FLAG = "FLAG"
}

/**
 * DSL function to create a command
 */
fun command(
    name: String,
    vararg aliases: String,
    configure: CommandBuilder.() -> Unit,
): AbstractCommand = CommandBuilder(name, aliases).apply(configure).build()
