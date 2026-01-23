package io.github.hytalekt.kytale.example.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.github.hytalekt.kytale.command.command
import io.github.hytalekt.kytale.message.text

val KitCommand =
    command("kit", "Give yourself a kit") {
        alias("k")
        requirePermission("example.kit")

        val kitArg = requiredArg("kitName", "Name of the kit", ArgTypes.STRING)
        val amountArg = defaultArg("amount", "Amount of items in kit", ArgTypes.INTEGER, 1, "1")
        val silentFlag = flagArg("silent", "Don't send a message")

        executorSync { context ->
            val kitName = kitArg()
            val amount = amountArg()
            val silent = silentFlag()

            context.sendMessage(
                text("Giving kit '$kitName' with $amount items${if (silent) " (silently)" else ""}"),
            )
        }
    }
