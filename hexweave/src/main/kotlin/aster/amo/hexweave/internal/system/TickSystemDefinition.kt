package aster.amo.hexweave.internal.system

import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Definition for a tick-based system (extends EntityTickingSystem).
 *
 * Created via the DSL:
 * ```kotlin
 * systems {
 *     tickSystem("my-system") {
 *         query { ... }
 *         every = 20
 *         dependencies { before<OtherSystem>() }
 *         onTick { /* TickContext available */ }
 *     }
 * }
 * ```
 */
data class TickSystemDefinition(
    val id: String,
    val priority: Int = 0,
    val query: Query<EntityStore>,
    val dependencies: Set<Dependency<EntityStore?>> = emptySet(),
    val tickInterval: Int = 1,
    val handler: TickContext.() -> Unit
)

/**
 * Handler definition for entity tick events (legacy).
 * @deprecated Use TickSystemDefinition instead
 */
data class TickHandler(
    val id: String,
    val priority: Int = 0,
    val query: Query<EntityStore>,
    val tickInterval: Int = 1,
    val handler: TickContext.() -> Unit
)
