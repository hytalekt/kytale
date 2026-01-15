package aster.amo.hexweave.dsl.mechanics

import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.hexweave.internal.HexweaveScope
import aster.amo.hexweave.internal.system.DamageHandler
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * Legacy DSL scope for registering damage handlers.
 *
 * @deprecated Use [aster.amo.hexweave.dsl.systems.HexweaveSystemsScope.damageSystem] instead.
 */
@HexweaveDsl
class HexweaveDamageScope internal constructor(
    private val plugin: JavaPlugin,
    private val scope: HexweaveScope
) {
    /** Registers a handler for fall damage events. */
    fun fall(priority: Int = 0, handler: HexweaveDamageContext.() -> Unit) {
        register(
            id = scope.systems.nextId("fall-damage"),
            priority = priority,
            filter = { it.cause == DamageCause.FALL },
            handler = handler
        )
    }

    /**
     * Registers a handler for damage events matching the filter.
     */
    fun onDamage(
        id: String? = null,
        priority: Int = 0,
        filter: (Damage) -> Boolean = { true },
        handler: HexweaveDamageContext.() -> Unit
    ) {
        register(
            id = id ?: scope.systems.nextId("damage"),
            priority = priority,
            filter = filter,
            handler = handler
        )
    }

    /**
     * Registers a handler for a specific damage cause.
     */
    fun cause(
        cause: DamageCause,
        priority: Int = 0,
        handler: HexweaveDamageContext.() -> Unit
    ) {
        register(
            id = scope.systems.nextId("damage-${cause.toString().lowercase()}"),
            priority = priority,
            filter = { it.cause == cause },
            handler = handler
        )
    }

    private fun register(
        id: String,
        priority: Int,
        filter: (Damage) -> Boolean,
        handler: HexweaveDamageContext.() -> Unit
    ) {
        scope.systems.addDamageHandler(
            DamageHandler(
                id = id,
                priority = priority,
                filter = filter,
                handler = handler
            )
        )
    }
}
