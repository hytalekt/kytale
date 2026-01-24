package io.github.hytalekt.kytale.serialization

import com.hypixel.hytale.common.semver.Semver
import com.hypixel.hytale.math.Mat4f
import com.hypixel.hytale.math.Quatf
import com.hypixel.hytale.math.Vec2f
import com.hypixel.hytale.math.Vec3f
import com.hypixel.hytale.math.Vec4f
import com.hypixel.hytale.math.range.FloatRange
import com.hypixel.hytale.math.range.IntRange
import com.hypixel.hytale.math.shape.Box
import com.hypixel.hytale.math.shape.Box2D
import com.hypixel.hytale.math.shape.Cylinder
import com.hypixel.hytale.math.shape.Ellipsoid
import com.hypixel.hytale.math.vector.Location
import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.math.vector.Vector2d
import com.hypixel.hytale.math.vector.Vector2i
import com.hypixel.hytale.math.vector.Vector2l
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.math.vector.Vector3l
import com.hypixel.hytale.math.vector.Vector4d
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerRespawnPointData
import com.hypixel.hytale.server.core.modules.entity.component.WorldGenId
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId
import io.github.hytalekt.kytale.serialization.common.SemverSerializer
import io.github.hytalekt.kytale.serialization.component.NetworkIdSerializer
import io.github.hytalekt.kytale.serialization.component.UUIDComponentSerializer
import io.github.hytalekt.kytale.serialization.component.WorldGenIdSerializer
import io.github.hytalekt.kytale.serialization.math.Mat4fSerializer
import io.github.hytalekt.kytale.serialization.math.QuatfSerializer
import io.github.hytalekt.kytale.serialization.math.Vec2fSerializer
import io.github.hytalekt.kytale.serialization.math.Vec3fSerializer
import io.github.hytalekt.kytale.serialization.math.Vec4fSerializer
import io.github.hytalekt.kytale.serialization.math.range.FloatRangeSerializer
import io.github.hytalekt.kytale.serialization.math.range.IntRangeSerializer
import io.github.hytalekt.kytale.serialization.math.shape.Box2DSerializer
import io.github.hytalekt.kytale.serialization.math.shape.BoxSerializer
import io.github.hytalekt.kytale.serialization.math.shape.CylinderSerializer
import io.github.hytalekt.kytale.serialization.math.shape.EllipsoidSerializer
import io.github.hytalekt.kytale.serialization.math.vector.LocationSerializer
import io.github.hytalekt.kytale.serialization.math.vector.TransformSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector2dSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector2iSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector2lSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector3dSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector3fSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector3iSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector3lSerializer
import io.github.hytalekt.kytale.serialization.math.vector.Vector4dSerializer
import io.github.hytalekt.kytale.serialization.player.PlayerDeathPositionDataSerializer
import io.github.hytalekt.kytale.serialization.player.PlayerRespawnPointDataSerializer
import kotlinx.serialization.modules.SerializersModule

/**
 * Default SerializersModule for Kytale.
 *
 * This module provides kotlinx.serialization support for 30+ Hytale domain types including:
 * - Vector types (Vector3d, Vector3i, Vector3f, Vector2d, Vector2i, Vector3l, Vector2l, Vector4d, Vec2f, Vec3f, Vec4f)
 * - Transform and Location types
 * - Math types (Quatf, Mat4f)
 * - Range types (IntRange, FloatRange)
 * - Shape types (Box, Box2D, Ellipsoid, Cylinder)
 * - Component types (MetaKey, WorldGenId, NetworkId, UUIDComponent)
 * - Player data types (PlayerDeathPositionData, PlayerRespawnPointData)
 * - Command types (RelativeFloat, RelativeInteger)
 * - Version types (Semver)
 *
 * Example usage:
 * ```kotlin
 * val json = Json {
 *     serializersModule = KytaleSerializersModule
 * }
 * val vector = Vector3d(1.0, 2.0, 3.0)
 * val jsonString = json.encodeToString(Vector3dSerializer, vector)
 * ```
 */
val KytaleSerializersModule =
    SerializersModule {
        // Vector types
        contextual(Vector3d::class, Vector3dSerializer)
        contextual(Vector3i::class, Vector3iSerializer)
        contextual(Vector3f::class, Vector3fSerializer)
        contextual(Vector2d::class, Vector2dSerializer)
        contextual(Vector2i::class, Vector2iSerializer)
        contextual(Vector3l::class, Vector3lSerializer)
        contextual(Vector2l::class, Vector2lSerializer)
        contextual(Vector4d::class, Vector4dSerializer)
        contextual(Vec2f::class, Vec2fSerializer)
        contextual(Vec3f::class, Vec3fSerializer)
        contextual(Vec4f::class, Vec4fSerializer)
        contextual(Transform::class, TransformSerializer)
        contextual(Location::class, LocationSerializer)

        // Math types
        contextual(Quatf::class, QuatfSerializer)
        contextual(Mat4f::class, Mat4fSerializer)

        // Range types
        contextual(IntRange::class, IntRangeSerializer)
        contextual(FloatRange::class, FloatRangeSerializer)

        // Shape types
        contextual(Box::class, BoxSerializer)
        contextual(Box2D::class, Box2DSerializer)
        contextual(Ellipsoid::class, EllipsoidSerializer)
        contextual(Cylinder::class, CylinderSerializer)

        // Component types
        contextual(WorldGenId::class, WorldGenIdSerializer)
        contextual(NetworkId::class, NetworkIdSerializer)
        contextual(UUIDComponent::class, UUIDComponentSerializer)

        // Player data types
        contextual(PlayerDeathPositionData::class, PlayerDeathPositionDataSerializer)
        contextual(PlayerRespawnPointData::class, PlayerRespawnPointDataSerializer)

        // Version types
        contextual(Semver::class, SemverSerializer)
    }
