package io.github.hytalekt.kytale.ui

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.server.core.universe.PlayerRef

class MyUI(
    playerRef: PlayerRef,
) : KytaleUI<Unit>(
        playerRef,
        CustomPageLifetime.CanDismiss,
        BuilderCodec.builder(Unit::class.java) { Unit }.build(),
    ) {
    override fun UIBuilder.buildUI() {
        append("Pages/MyPage.ui")

        "#Title" set "My Page"
        "#Counter" set 42
        "#Visible" set true

        dropdown("#SavedConfigs") {
            entry("None", "")
            entry("Config 1", "config1")
            default("")
        }

        onActivate("#SubmitButton")
        onValueChange("#Input")
    }
}
