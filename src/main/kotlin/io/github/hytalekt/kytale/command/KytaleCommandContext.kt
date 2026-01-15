package io.github.hytalekt.kytale.command

import com.hypixel.hytale.component.ComponentAccessor
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.Argument
import com.hypixel.hytale.server.core.command.system.arguments.system.WrappedArg
import com.hypixel.hytale.server.core.command.system.arguments.types.EntityWrappedArg
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

@JvmInline
value class KytaleCommandContext(
    val context: CommandContext,
) {
    @KytaleCommandDsl
    operator fun <A : Argument<A, T>, T> Argument<A, T>.invoke(): T = context.get(this)

    @KytaleCommandDsl
    operator fun <T> WrappedArg<T>.invoke(): T = this.arg()

    @KytaleCommandDsl
    operator fun EntityWrappedArg.invoke(componentAccessor: ComponentAccessor<EntityStore>): Ref<EntityStore>? =
        get(componentAccessor, context)

    @KytaleCommandDsl
    fun EntityWrappedArg.getEntityDirectly(world: World): Ref<EntityStore>? = getEntityDirectly(context, world)
}
