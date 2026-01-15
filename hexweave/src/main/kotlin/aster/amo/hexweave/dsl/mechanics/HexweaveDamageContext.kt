package aster.amo.hexweave.dsl.mechanics

import aster.amo.hexweave.internal.HexweaveScope
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.UUID

/**
 * Context passed to Hexweave damage handlers.
 *
 * Provides access to the damage event and entity information.
 */
class HexweaveDamageContext internal constructor(
    val plugin: JavaPlugin,
    private val scope: HexweaveScope,
    val commandBuffer: CommandBuffer<EntityStore>,
    val damage: Damage,
    val playerRef: PlayerRef?,
    private val entityUuid: UUID?
) {
    val logger get() = plugin.logger

    /** The world the damage occurred in, if available. */
    val world: World?
        get() = playerRef?.worldUuid?.let { Universe.get().getWorld(it) }

    /** Cancel this damage event. */
    fun cancelDamage() {
        damage.isCancelled = true
    }
}
