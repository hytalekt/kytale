package io.github.hytalekt.kytale.ui

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceBasePage.ChoicePageEventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

class ExampleSimplePage(
    playerRef: PlayerRef,
) : BasicCustomUIPage(playerRef, CustomPageLifetime.CanDismiss) {
    override fun build(commandBuilder: UICommandBuilder) {
        commandBuilder.clear("#MainContainer")
        commandBuilder.append("Mod/ExampleUI.ui")
        commandBuilder.set("#Title.Text", "Hello Hytale!")
        commandBuilder.set("#Description.Text", "This is a simple UI created via Kotlin.")
        commandBuilder.set("#Logo.Visible", true)
    }
}

data class ExampleEventData(
    var action: String? = null,
    var value: Int = 0,
) {
    companion object {
        val CODEC_2 =
            BuilderCodec
                .builder<ExampleEventData?>(ExampleEventData::class.java) { ExampleEventData() }
                .append(
                    KeyedCodec("Value", Codec.INTEGER),
                    BiConsumer { data: ExampleEventData, s: Int ->
                        data.value = s.toInt()
                    },
                    Function { data: ExampleEventData -> data.value },
                ).add()
                .build()

        val CODEC: BuilderCodec<ExampleEventData> =
            BuilderCodec
                .builder(ExampleEventData::class.java) { ExampleEventData() }
                // TODO: addField expects KeyedCodec, needs proper wrapping
                // .addField("action", { p, v -> p.action = v as? String }, { it.action })
                // .addField("value", { p, v -> p.value = v as Int }, { it.value })
                .build()
    }
}

class ExampleInteractivePage(
    playerRef: PlayerRef,
) : InteractiveCustomUIPage<ExampleEventData>(playerRef, CustomPageLifetime.CanDismiss, ExampleEventData.CODEC) {
    override fun build(
        ref: Ref<EntityStore>,
        commandBuilder: UICommandBuilder,
        eventBuilder: UIEventBuilder,
        store: Store<EntityStore>,
    ) {
        commandBuilder.append("Mod/InteractiveMenu.ui")
        commandBuilder.set("#Status.Text", "Waiting for input...")

        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ConfirmButton")
    }

    override fun handleDataEvent(
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        data: ExampleEventData,
    ) {
        when (data.action) {
            "confirm" -> {
                val update = UICommandBuilder()
                update.set("#Status.Text", "Action Confirmed!")
                sendUpdate(update)
            }

            "close" -> {
                close()
            }
        }
    }
}
