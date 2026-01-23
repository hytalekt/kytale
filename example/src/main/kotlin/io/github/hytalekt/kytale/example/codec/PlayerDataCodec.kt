package io.github.hytalekt.kytale.example.codec

import com.hypixel.hytale.codec.Codec
import io.github.hytalekt.kytale.codec.buildCodec

data class PlayerData(
    var username: String = "",
    var level: Int = 1,
    var experience: Double = 0.0,
    var isOnline: Boolean = false,
)

val PlayerDataCodec =
    buildCodec(::PlayerData) {
        documentation = "Stores basic player information"

        addField("Username", Codec.STRING) {
            documentation = "The player's display name"
            setter { username = it }
            getter { _ -> username }
        }

        addField("Level", Codec.INTEGER) {
            documentation = "The player's current level"
            setter { level = it }
            getter { _ -> level }
        }

        addField("Experience", Codec.DOUBLE) {
            documentation = "Total experience points"
            setter { experience = it }
            getter { _ -> experience }
        }

        addField("Online", Codec.BOOLEAN) {
            documentation = "Whether the player is currently online"
            setter { isOnline = it }
            getter { _ -> isOnline }
        }
    }
