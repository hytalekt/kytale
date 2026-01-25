@file:OptIn(ExperimentalSerializationApi::class)

package io.github.hytalekt.kytale.serialization.math

import com.hypixel.hytale.math.Mat4f
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.FloatArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object Mat4fSerializer : KSerializer<Mat4f> {
    private val delegateSerializer = ArraySerializer(FloatArraySerializer())
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: Mat4f,
    ) {
        val matrix =
            arrayOf(
                floatArrayOf(value.m11, value.m12, value.m13, value.m14),
                floatArrayOf(value.m21, value.m22, value.m23, value.m24),
                floatArrayOf(value.m31, value.m32, value.m33, value.m34),
                floatArrayOf(value.m41, value.m42, value.m43, value.m44),
            )
        encoder.encodeSerializableValue(delegateSerializer, matrix)
    }

    override fun deserialize(decoder: Decoder): Mat4f {
        val matrix = decoder.decodeSerializableValue(delegateSerializer)
        require(matrix.size == 4) { "Mat4f requires exactly 4 rows, got ${matrix.size}" }
        matrix.forEachIndexed { index, row ->
            require(row.size == 4) { "Mat4f row $index requires exactly 4 columns, got ${row.size}" }
        }

        return Mat4f(
            matrix[0][0],
            matrix[0][1],
            matrix[0][2],
            matrix[0][3],
            matrix[1][0],
            matrix[1][1],
            matrix[1][2],
            matrix[1][3],
            matrix[2][0],
            matrix[2][1],
            matrix[2][2],
            matrix[2][3],
            matrix[3][0],
            matrix[3][1],
            matrix[3][2],
            matrix[3][3],
        )
    }
}

/**
 * Serializable typealias for [Mat4f].
 *
 * Serialized shape: `float[4][4]` (row-major)
 */
typealias KMat4f =
    @Serializable(with = Mat4fSerializer::class)
    Mat4f
