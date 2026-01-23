package io.github.hytalekt.kytale.example.interaction.simpleinstant

import com.hypixel.hytale.component.AddReason
import com.hypixel.hytale.math.shape.Box
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.BlockEntity
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation
import com.hypixel.hytale.server.core.modules.entity.component.Intangible
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.modules.physics.component.Velocity
import com.hypixel.hytale.server.core.modules.projectile.config.StandardPhysicsConfig
import com.hypixel.hytale.server.core.modules.projectile.config.StandardPhysicsProvider
import com.hypixel.hytale.server.core.modules.time.TimeResource
import io.github.hytalekt.kytale.interaction.simpleInstantInteraction

val SpawnEntityInteraction = simpleInstantInteraction(
    interactionId = "SpawnEntity",
) {
    firstRun { type, context, cooldownHandler ->
        val commandBuffer =
            context.commandBuffer ?: return@firstRun
        val world = commandBuffer.externalData.world
        val playerRef = context.entity
        val entityStore = world.entityStore.store
        val player =
            entityStore.getComponent(playerRef, Player.getComponentType())
                ?: return@firstRun

        val modelName = "TestEntity"
        val blockAssetName = "Soil_Grass"
        val block = BlockType.getAssetMap().getAsset(blockAssetName)
        val modelOrBlockModel = block?.customModel ?: modelName
        player.sendMessage(Message.raw("Spawning entity with model: $modelOrBlockModel"))
        world.execute {
            val playerTransform = entityStore.getComponent(
                playerRef, TransformComponent.getComponentType()
            ) ?: return@execute

            val entityTransform = playerTransform.clone().also {
                it.position.add(5.0, 0.0, 0.0)
            }

            val timeResource: TimeResource =
                entityStore.getResource<TimeResource?>(TimeResource.getResourceType())
            val be = BlockEntity.assembleDefaultBlockEntity(
                timeResource, blockAssetName, entityTransform.position
            )
            val bb = BoundingBox(Box.horizontallyCentered(1.0, 1.0, 1.0))
            be.addComponent(BoundingBox.getComponentType(), BoundingBox())
            be.addComponent(
                StandardPhysicsProvider.getComponentType(),
                StandardPhysicsProvider(
                    bb, player.uuid, StandardPhysicsConfig(
                    ), Vector3d(0.0, 0.0, 0.0), false
                )
            );
            be.addComponent(
                Nameplate.getComponentType(), Nameplate(modelOrBlockModel)
            )
            be.ensureComponent(UUIDComponent.getComponentType())
            be.ensureComponent(TransformComponent.getComponentType())
            be.ensureComponent(HeadRotation.getComponentType())
            be.ensureComponent(Velocity.getComponentType())
            be.ensureComponent(BoundingBox.getComponentType())
            be.tryRemoveComponent(Intangible.getComponentType())
            val beRef =
                entityStore.addEntity(be, AddReason.SPAWN) ?: return@execute
            val be2 =
                entityStore.getComponent(beRef, BlockEntity.getComponentType())
                    ?: return@execute
            be2.updateHitbox(beRef, commandBuffer)
            be2.simplePhysicsProvider.setGravity(10.0, bb)
            be2.simplePhysicsProvider.setBounciness(2.0)
        }
    }
}
