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

@JvmInline
@KytaleCommandDsl
value class KytaleCommandBuilder(
    val inner: KytaleCommand,
) {
    @KytaleCommandDsl
    fun alias(vararg aliases: String) {
        inner.addAliases(*aliases)
    }

    @KytaleCommandDsl
    fun <T> defaultArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
        defaultValue: T,
        defaultValueDescription: String,
    ): DefaultArg<T> = inner.withDefaultArg(name, description, argType, defaultValue, defaultValueDescription)

    @KytaleCommandDsl
    fun <W : WrappedArg<T>, T> defaultArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
        defaultValue: T,
        defaultValueDescription: String,
    ): W = inner.withDefaultArg(name, description, wrapper, defaultValue, defaultValueDescription)

    @KytaleCommandDsl
    fun <T> optionalArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): OptionalArg<T> = inner.withOptionalArg(name, description, argType)

    @KytaleCommandDsl
    fun <W : WrappedArg<T>, T> optionalArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
    ): W = inner.withOptionalArg(name, description, wrapper)

    @KytaleCommandDsl
    fun <T> requiredArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): RequiredArg<T> = inner.withRequiredArg(name, description, argType)

    @KytaleCommandDsl
    fun <W : WrappedArg<T>, T> requiredArg(
        name: String,
        description: String,
        wrapper: ArgWrapper<W, T>,
    ): W = inner.withRequiredArg(name, description, wrapper)

    @KytaleCommandDsl
    fun flagArg(
        name: String,
        description: String,
    ): FlagArg = inner.withFlagArg(name, description)

    @KytaleCommandDsl
    fun <T> listDefaultArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
        defaultValue: List<T>,
        defaultValueDescription: String,
    ): DefaultArg<List<T>> = inner.withListDefaultArg(name, description, argType, defaultValue, defaultValueDescription)

    @KytaleCommandDsl
    fun <T> listRequiredArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): RequiredArg<List<T>> = inner.withListRequiredArg(name, description, argType)

    @KytaleCommandDsl
    fun <T> listOptionalArg(
        name: String,
        description: String,
        argType: ArgumentType<T>,
    ): OptionalArg<List<T>> = inner.withListOptionalArg(name, description, argType)

    @KytaleCommandDsl
    fun requirePermission(permission: String) {
        inner.requirePermission(permission)
    }

    @KytaleCommandDsl
    fun owner(owner: CommandOwner) {
        inner.setOwner(owner)
    }

    @KytaleCommandDsl
    fun subcommand(
        name: String,
        description: String,
        block: KytaleCommandBuilder.() -> Unit,
    ): KytaleCommand = KytaleCommandBuilder(KytaleCommand(name, description)).apply(block).inner.also(inner::addSubCommand)

    @KytaleCommandDsl
    fun variant(
        description: String,
        block: KytaleCommandBuilder.() -> Unit,
    ): KytaleCommand = KytaleCommandBuilder(KytaleCommand(null, description)).apply(block).inner.also(inner::addUsageVariant)

    @KytaleCommandDsl
    inline fun executorSync(crossinline block: KytaleCommandContext.(CommandContext) -> Unit) {
        executorAsync { context ->
            this.block(context)
            null
        }
    }

    @KytaleCommandDsl
    fun executorAsync(block: KytaleCommandContext.(CommandContext) -> CompletableFuture<Void?>?) {
        inner.defaultExecutor = block
    }
}
