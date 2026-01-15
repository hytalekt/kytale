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
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Dynamic tick system created from a TickSystemDefinition.
 *
 * This allows users to define tick systems through the DSL without
 * creating custom EntityTickingSystem implementations.
 */
internal class DynamicTickSystem(
    private val plugin: JavaPlugin,
    private val definition: TickSystemDefinition
) : EntityTickingSystem<EntityStore>() {

    override fun getQuery(): Query<EntityStore> = definition.query

    override fun getDependencies(): Set<Dependency<EntityStore?>?> {
        @Suppress("UNCHECKED_CAST")
        return definition.dependencies as Set<Dependency<EntityStore?>?>
    }

    override fun tick(
        deltaTime: Float,
        tickIndex: Int,
        archetypeChunk: ArchetypeChunk<EntityStore>,
        store: Store<EntityStore>,
        commandBuffer: CommandBuffer<EntityStore>
    ) {
        if (definition.tickInterval > 1 && tickIndex % definition.tickInterval != 0) {
            return
        }

        val chunkSize = archetypeChunk.size()
        for (index in 0 until chunkSize) {
            try {
                val context = TickContext(
                    deltaTime = deltaTime,
                    tickIndex = tickIndex,
                    index = index,
                    chunk = archetypeChunk,
                    store = store,
                    commandBuffer = commandBuffer
                )
                definition.handler(context)
            } catch (e: Exception) {
                plugin.logger.atSevere()
                    .withCause(e)
                    .log("Error in tick system '${definition.id}'")
            }
        }
    }
}

/**
 * Dynamic damage system created from a DamageSystemDefinition.
 *
 * This allows users to define damage systems through the DSL without
 * creating custom DamageEventSystem implementations.
 */
internal class DynamicDamageSystem(
    private val plugin: JavaPlugin,
    private val scope: HexweaveScope,
    private val definition: DamageSystemDefinition
) : DamageEventSystem() {

    private val defaultDeps = setOf(
        SystemDependency(Order.BEFORE, DamageSystems.ApplyDamage::class.java)
    )

    override fun getQuery(): Query<EntityStore> =
        definition.query ?: AllLegacyLivingEntityTypesQuery.INSTANCE

    override fun getDependencies(): Set<Dependency<EntityStore?>?> {
        if (definition.dependencies.isEmpty()) {
            return defaultDeps
        }
        @Suppress("UNCHECKED_CAST")
        return definition.dependencies as Set<Dependency<EntityStore?>?>
    }

    override fun handle(
        index: Int,
        archetypeChunk: ArchetypeChunk<EntityStore>,
        store: Store<EntityStore>,
        commandBuffer: CommandBuffer<EntityStore>,
        damage: Damage
    ) {
        if (!definition.filter(damage)) {
            return
        }

        try {
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

            definition.handler(context)
        } catch (e: Exception) {
            plugin.logger.atSevere()
                .withCause(e)
                .log("Error in damage system '${definition.id}'")
        }
    }
}
