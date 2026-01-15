package aster.amo.hexweave.internal.system

import aster.amo.hexweave.dsl.mechanics.HexweaveDamageContext
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Definition for a damage event system (extends DamageEventSystem).
 *
 * Created via the DSL:
 * ```kotlin
 * systems {
 *     damageSystem("my-damage") {
 *         filter { it.cause == DamageCause.FALL }
 *         dependencies { before<DamageSystems.ApplyDamage>() }
 *         onDamage { /* HexweaveDamageContext available */ }
 *     }
 * }
 * ```
 */
data class DamageSystemDefinition(
    val id: String,
    val priority: Int = 0,
    val query: Query<EntityStore>? = null, // null = use default AllLegacyLivingEntityTypesQuery
    val dependencies: Set<Dependency<EntityStore?>> = emptySet(),
    val filter: (Damage) -> Boolean = { true },
    val handler: HexweaveDamageContext.() -> Unit
)

/**
 * Handler definition for damage events (legacy).
 * @deprecated Use DamageSystemDefinition instead
 */
data class DamageHandler(
    val id: String,
    val priority: Int = 0,
    val filter: (Damage) -> Boolean,
    val handler: HexweaveDamageContext.() -> Unit
)
