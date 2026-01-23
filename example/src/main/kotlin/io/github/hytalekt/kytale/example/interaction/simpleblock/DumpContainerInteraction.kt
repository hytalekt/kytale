package io.github.hytalekt.kytale.example.interaction.simpleblock

import com.hypixel.hytale.component.AddReason
import com.hypixel.hytale.math.shape.Box
import com.hypixel.hytale.math.util.ChunkUtil
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.protocol.BlockRotation
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.BlockEntity
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate
import com.hypixel.hytale.server.core.inventory.Inventory
import com.hypixel.hytale.server.core.inventory.ItemStack
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation
import com.hypixel.hytale.server.core.modules.entity.component.Intangible
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.modules.physics.component.Velocity
import com.hypixel.hytale.server.core.modules.projectile.config.StandardPhysicsConfig
import com.hypixel.hytale.server.core.modules.projectile.config.StandardPhysicsProvider
import com.hypixel.hytale.server.core.modules.time.TimeResource
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState
import io.github.hytalekt.kytale.interaction.simpleBlockInteraction
import io.github.hytalekt.kytale.message.text

val DumpContainerInteraction = simpleBlockInteraction("Marucs_DumpContainer") {
    interactWithBlock { world, commandBuffer, type, context, stack, pos, handler ->
        val ref = context.entity
        val player = commandBuffer.getComponent(ref, Player.getComponentType()) ?: return@interactWithBlock

        val containerState = world.getState(pos.x, pos.y, pos.z, true) as? ItemContainerState ?: run {
            Message.translation("server.interaction.invalidBlockState").param(
                    "blockState", "${
                        world.getState(
                            pos.x, pos.y, pos.z, true
                        )?.javaClass?.simpleName
                    }"
                ).let { player.sendMessage(it) }
            return@interactWithBlock
        }

        containerState.itemContainer.forEach { sh, stack ->
            player.sendMessage(text("Item in slot $sh: $stack"))
        }
    }
}