package io.github.hytalekt.kytale.example.codec

import com.hypixel.hytale.codec.Codec
import io.github.hytalekt.kytale.codec.buildCodec

data class GameConfig(
    var maxPlayers: Int = 10,
    var gameMode: String = "default",
    var spawnProtection: Boolean = true,
    var customRules: String = "",
)

val GameConfigCodec =
    buildCodec(::GameConfig) {
        documentation = "Game configuration with inheritance support"

        addField("MaxPlayers", Codec.INTEGER) {
            documentation = "Maximum number of players allowed"
            setter { maxPlayers = it }
            getter { _ -> maxPlayers }
            inherit { parent, _ ->
                maxPlayers = parent.maxPlayers
            }
        }

        addField("GameMode", Codec.STRING) {
            documentation = "The game mode identifier"
            setter { gameMode = it }
            getter { _ -> gameMode }
            inherit { parent, _ ->
                gameMode = parent.gameMode
            }
        }

        addField("SpawnProtection", Codec.BOOLEAN) {
            documentation = "Whether spawn protection is enabled"
            setter { spawnProtection = it }
            getter { _ -> spawnProtection }
            inherit { parent, _ ->
                spawnProtection = parent.spawnProtection
            }
        }

        addField("CustomRules", Codec.STRING) {
            documentation = "Custom game rules (not inherited)"
            setter { customRules = it }
            getter { _ -> customRules }
        }

        afterDecode {
            require(maxPlayers > 0) { "MaxPlayers must be positive" }
        }
    }
