package aster.amo.kytale.extension

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.protocol.SoundCategory
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.modules.physics.component.Velocity
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.ParticleUtil
import com.hypixel.hytale.server.core.universe.world.SoundUtil
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Extension functions to simplify the verbose Hytale ECS API.
 */

/**
 * Gets the ComponentType for a reified component class.
 * Uses reflection to call the static getComponentType() method.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Component<EntityStore>> componentType(): ComponentType<EntityStore, T> {
    val method = T::class.java.getMethod("getComponentType")
    return method.invoke(null) as ComponentType<EntityStore, T>
}

/**
 * Gets a component from an entity at the given index in the archetype chunk.
 *
 * Before: commandBuffer.getComponent(archetypeChunk.getReferenceTo(index), UUIDComponent.getComponentType())
 * After:  commandBuffer.get<UUIDComponent>(chunk, index)
 */
inline fun <reified T : Component<EntityStore>> CommandBuffer<EntityStore>.get(
    chunk: ArchetypeChunk<EntityStore>,
    index: Int
): T? = getComponent(chunk.getReferenceTo(index), componentType<T>())

/**
 * Gets a component from a PlayerRef using reified generics.
 *
 * Before: playerRef.getComponent(Velocity.getComponentType())
 * After:  playerRef.get<Velocity>()
 */
inline fun <reified T : Component<EntityStore>> PlayerRef.get(): T? =
    try { getComponent(componentType<T>()) } catch (e: Exception) { null }

/** Shortcut for velocity component */
val PlayerRef.velocity: Velocity?
    get() = try { getComponent(Velocity.getComponentType()) } catch (e: Exception) { null }

/** Shortcut for movement states */
val PlayerRef.movementStates: MovementStatesComponent?
    get() = try { getComponent(MovementStatesComponent.getComponentType()) } catch (e: Exception) { null }

/** Shortcut for transform component */
val PlayerRef.transform: TransformComponent?
    get() = try { getComponent(TransformComponent.getComponentType()) } catch (e: Exception) { null }

/**
 * Plays a sound at a position.
 *
 * Before: SoundUtil.playSoundEvent3d(SoundEvent.getAssetMap().getIndex("SFX_Gem_Break"), SoundCategory.SFX, position, commandBuffer)
 * After:  playSound("SFX_Gem_Break", position, commandBuffer)
 */
fun playSound(
    sound: String,
    position: Vector3d,
    commandBuffer: CommandBuffer<EntityStore>,
    category: SoundCategory = SoundCategory.SFX
) {
    SoundUtil.playSoundEvent3d(
        SoundEvent.getAssetMap().getIndex(sound),
        category,
        position,
        commandBuffer
    )
}

/**
 * Spawns a particle effect at a position.
 */
fun spawnParticle(
    effect: String,
    position: Vector3d,
    commandBuffer: CommandBuffer<EntityStore>
) {
    ParticleUtil.spawnParticleEffect(effect, position, commandBuffer)
}
