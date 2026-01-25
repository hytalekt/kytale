package io.github.hytalekt.kytale.example.waypoint

import io.github.hytalekt.kytale.serialization.KytaleSerializersModule
import io.github.hytalekt.kytale.serialization.common.KSemver
import io.github.hytalekt.kytale.serialization.math.vector.KVector3d
import io.github.hytalekt.kytale.serialization.math.vector.KVector3f
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Waypoint(
    val name: String,
    val position: KVector3d,
    val rotation: KVector3f,
)

@Serializable
data class WaypointRegistry(
    val version: KSemver,
    val waypoints: List<Waypoint>,
)

val WaypointJson =
    Json {
        serializersModule = KytaleSerializersModule
        prettyPrint = true
        ignoreUnknownKeys = true
    }

fun loadWaypoints(): WaypointRegistry {
    val resource =
        WaypointRegistry::class.java.getResourceAsStream("/waypoints.json")
            ?: error("waypoints.json not found in resources")
    return WaypointJson.decodeFromString<WaypointRegistry>(resource.bufferedReader().readText())
}
