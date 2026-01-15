package io.github.hytalekt.kytale.example.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.message.text

val GiveCommand =
    command("give", "Give items to players") {
        requirePermission("example.give")

        val playerArg = requiredArg("player", "Player to give items to", ArgTypes.PLAYER_REF)
        val itemArg = requiredArg("item", "Item to give", ArgTypes.ITEM_ASSET)
        val amountArg = defaultArg("amount", "Amount to give", ArgTypes.INTEGER, 1, "1")
        val silentFlag = flagArg("silent", "Don't notify the player")

        executorSync { context ->
            val player = playerArg()
            val item = itemArg()
            val amount = amountArg()
            val silent = silentFlag()

            context.sendMessage(
                text("Giving ${amount}x ${item.id} to ${player.username}${if (silent) " (silently)" else ""}"),
            )
        }
    }
