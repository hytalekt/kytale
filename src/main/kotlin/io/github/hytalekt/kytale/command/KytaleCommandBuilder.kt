package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.CommandOwner
import com.hypixel.hytale.server.core.command.system.arguments.system.ArgWrapper
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg
import com.hypixel.hytale.server.core.command.system.arguments.system.FlagArg
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.system.WrappedArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgumentType
import java.util.concurrent.CompletableFuture

/**
 * [KytaleCommand] builder class
 *
 * @param command The command being built
 *
 * @see command
 * @see KytaleCommand
 */
@JvmInline
@KytaleCommandDsl
value class KytaleCommandBuilder(
    val command: KytaleCommand,
) {
    /**
     * Adds aliases for the command
     *
     * @param aliases The command aliases
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.addAliases
     */
    fun alias(vararg aliases: String) {
        command.addAliases(*aliases)
    }

    /**
     * Adds a default argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     * @param defaultValue The default value
     * @param defaultValueDescription Description of the default value
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withDefaultArg
     */
    fun <T> defaultArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
        defaultValue: T,
        defaultValueDescription: String,
    ): DefaultArg<T> = command.withDefaultArg(name, description, argType, defaultValue, defaultValueDescription)

    /**
     * Adds a wrapped default argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param wrapper The argument wrapper
     * @param defaultValue The default value
     * @param defaultValueDescription Description of the default value
     *
     * @return The created wrapped argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withDefaultArg
     */
    fun <W : WrappedArg<T>, T> defaultArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
        defaultValue: T,
        defaultValueDescription: String,
    ): W = command.withDefaultArg(name, description, wrapper, defaultValue, defaultValueDescription)

    /**
     * Adds an optional argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withOptionalArg
     */
    fun <T> optionalArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): OptionalArg<T> = command.withOptionalArg(name, description, argType)

    /**
     * Adds a wrapped optional argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param wrapper The argument wrapper
     *
     * @return The created wrapped argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withOptionalArg
     */
    fun <W : WrappedArg<T>, T> optionalArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
    ): W = command.withOptionalArg(name, description, wrapper)

    /**
     * Adds a required argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withRequiredArg
     */
    fun <T> requiredArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): RequiredArg<T> = command.withRequiredArg(name, description, argType)

    /**
     * Adds a wrapped required argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param wrapper The argument wrapper
     *
     * @return The created wrapped argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withRequiredArg
     */
    fun <W : WrappedArg<T>, T> requiredArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
    ): W = command.withRequiredArg(name, description, wrapper)

    /**
     * Adds a flag argument to the command
     *
     * @param name The flag name
     * @param description The flag description
     *
     * @return The created flag
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withFlagArg
     */
    fun flagArg(
        name: String,
        description: String,
    ): FlagArg = command.withFlagArg(name, description)

    /**
     * Adds a list default argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     * @param defaultValue The default list value
     * @param defaultValueDescription Description of the default value
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withListDefaultArg
     */
    fun <T> listDefaultArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
        defaultValue: List<T>,
        defaultValueDescription: String,
    ): DefaultArg<List<T>> = command.withListDefaultArg(name, description, argType, defaultValue, defaultValueDescription)

    /**
     * Adds a list required argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withListRequiredArg
     */
    fun <T> listRequiredArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): RequiredArg<List<T>> = command.withListRequiredArg(name, description, argType)

    /**
     * Adds a list optional argument to the command
     *
     * @param name The argument name
     * @param description The argument description
     * @param argType The argument type
     *
     * @return The created argument
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.withListOptionalArg
     */
    fun <T> listOptionalArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): OptionalArg<List<T>> = command.withListOptionalArg(name, description, argType)

    /**
     * Requires a permission for the command
     *
     * @param permission The required permission
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.requirePermission
     */
    fun requirePermission(permission: String) {
        command.requirePermission(permission)
    }

    /**
     * Sets the command owner
     *
     * @param owner The command owner
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.setOwner
     */
    fun owner(owner: CommandOwner) {
        command.setOwner(owner)
    }

    /**
     * Adds a subcommand
     *
     * @param name The subcommand name
     * @param description The subcommand description
     * @param block The subcommand builder configuration
     *
     * @return The created subcommand
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.addSubCommand
     */
    fun subcommand(
        name: String,
        description: String,
        block: @KytaleCommandDsl KytaleCommandBuilder.() -> Unit,
    ): KytaleCommand = KytaleCommandBuilder(KytaleCommand(name, description)).apply(block).command.also(command::addSubCommand)

    /**
     * Adds a usage variant
     *
     * @param description The variant description
     * @param block The variant builder configuration
     *
     * @return The created variant
     *
     * @see com.hypixel.hytale.server.core.command.system.AbstractCommand.addUsageVariant
     */
    @KytaleCommandDsl
    fun variant(
        description: String,
        block: @KytaleCommandDsl KytaleCommandBuilder.() -> Unit,
    ): KytaleCommand = KytaleCommandBuilder(KytaleCommand(null, description)).apply(block).command.also(command::addUsageVariant)

    /**
     * Sets a synchronous executor for the command
     *
     * @param block The executor block
     *
     * @see executorAsync
     */
    @KytaleCommandDsl
    inline fun executorSync(crossinline block: @KytaleCommandDsl KytaleCommandContext.(CommandContext) -> Unit) {
        executorAsync { context ->
            this.block(context)
            null
        }
    }

    /**
     * Sets an asynchronous executor for the command
     *
     * @param block The executor block returning a CompletableFuture
     *
     * @see executorSync
     */
    @KytaleCommandDsl
    fun executorAsync(block: @KytaleCommandDsl KytaleCommandContext.(CommandContext) -> CompletableFuture<Void?>?) {
        command.defaultExecutor = block
    }
}
