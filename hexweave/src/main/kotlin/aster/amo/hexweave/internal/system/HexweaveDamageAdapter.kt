package aster.amo.hexweave.internal.system

import aster.amo.hexweave.dsl.mechanics.HexweaveDamageContext
import aster.amo.hexweave.internal.HexweaveScope
import aster.amo.kytale.extension.get
import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.dependency.Dependency
import com.hypixel.hytale.component.dependency.Order
import com.hypixel.hytale.component.dependency.SystemDependency
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.concurrent.atomic.AtomicReference

/**
 * Adapter that wraps DamageEventSystem and fans out to multiple registered handlers.
 *
 * This allows multiple damage handlers to be registered through the DSL without
 * needing to create separate DamageEventSystem implementations.
 */
internal class HexweaveDamageAdapter(
    private val plugin: JavaPlugin,
    private val scope: HexweaveScope,
    initialHandlers: List<DamageHandler>
) : DamageEventSystem() {

    private val handlers = AtomicReference(initialHandlers)

    private val dependency = setOf(
        SystemDependency(Order.BEFORE, DamageSystems.ApplyDamage::class.java)
    )

    override fun getQuery(): Query<EntityStore> = AllLegacyLivingEntityTypesQuery.INSTANCE

    override fun getDependencies(): Set<Dependency<EntityStore?>?> = dependency

    /**
     * Refreshes the list of handlers (thread-safe).
     */
    fun refresh(newHandlers: List<DamageHandler>) {
        handlers.set(newHandlers)
    }

    override fun handle(
        index: Int,
        archetypeChunk: ArchetypeChunk<EntityStore>,
        store: Store<EntityStore>,
        commandBuffer: CommandBuffer<EntityStore>,
        damage: Damage
    ) {
        val currentHandlers = handlers.get()
        if (currentHandlers.isEmpty()) return

        val uuidComponent = commandBuffer.get<UUIDComponent>(archetypeChunk, index) ?: return
        val playerRef = commandBuffer.get<PlayerRef>(archetypeChunk, index)

        val context = HexweaveDamageContext(
            plugin = plugin,
            scope = scope,
            commandBuffer = commandBuffer,
            damage = damage,
            playerRef = playerRef,
            entityUuid = uuidComponent.uuid
        )

        for (handler in currentHandlers) {
            try {
                if (handler.filter(damage)) {
                    handler.handler(context)
                }
            } catch (e: Exception) {
                plugin.logger.atSevere()
                    .withCause(e)
                    .log("Error in damage handler '${handler.id}'")
            }
        }
    }
}
