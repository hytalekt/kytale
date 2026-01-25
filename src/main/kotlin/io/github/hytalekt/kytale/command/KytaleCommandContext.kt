package io.github.hytalekt.kytale.command

import com.hypixel.hytale.component.ComponentAccessor
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.Argument
import com.hypixel.hytale.server.core.command.system.arguments.system.WrappedArg
import com.hypixel.hytale.server.core.command.system.arguments.types.EntityWrappedArg
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * A command context wrapper that provides convenient argument accessors
 *
 * @param context The underlying command context
 *
 * @see com.hypixel.hytale.server.core.command.system.CommandContext
 */
@JvmInline
@KytaleCommandDsl
value class KytaleCommandContext(
    private val context: CommandContext,
) {
    /**
     * Gets an argument value from the context
     *
     * @see com.hypixel.hytale.server.core.command.system.CommandContext.get
     */
    operator fun <A : Argument<A, T>, T> Argument<A, T>.invoke(): T = context.get(this)

    /**
     * Unwraps a wrapped argument
     *
     * @see com.hypixel.hytale.server.core.command.system.arguments.system.WrappedArg.arg
     */
    operator fun <T> WrappedArg<T>.invoke(): T = this.arg()

    /**
     * Gets an entity from a wrapped argument using a component accessor
     *
     * @param componentAccessor The component accessor for entity stores
     * @return The entity reference, or null if not found
     *
     * @see com.hypixel.hytale.server.core.command.system.arguments.types.EntityWrappedArg.get
     */
    operator fun EntityWrappedArg.invoke(componentAccessor: ComponentAccessor<EntityStore>): Ref<EntityStore>? =
        get(componentAccessor, context)

    /**
     * Gets an entity directly from a wrapped argument using a world
     *
     * @param world The world containing the entity
     * @return The entity reference, or null if not found
     *
     * @see com.hypixel.hytale.server.core.command.system.arguments.types.EntityWrappedArg.getEntityDirectly
     */
    fun EntityWrappedArg.getEntityDirectly(world: World): Ref<EntityStore>? = getEntityDirectly(context, world)
}
