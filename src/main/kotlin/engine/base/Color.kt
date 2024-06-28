package engine.base

import java.nio.ByteBuffer
import kotlin.random.Random

data class Color(
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float = 1.0f
) {
    companion object {
        val WHITE = Color(1.0f, 1.0f, 1.0f)
        val BLACK = Color(0.0f, 0.0f, 0.0f)
        val RED = Color(1.0f, 0.0f, 0.0f)
        val GREEN = Color(0.0f, 1.0f, 0.0f)
        val BLUE = Color(0.0f, 0.0f, 1.0f)
        val MAGENTA = Color(1.0f, 0.0f, 1.0f)

        fun random(): Color {
            return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
        }
    }
    
    fun put(buffer: ByteBuffer): ByteBuffer {
        return buffer
            .put((r * 255.0f).toInt().toByte())
            .put((g * 255.0f).toInt().toByte())
            .put((b * 255.0f).toInt().toByte())
            .put((a * 255.0f).toInt().toByte())
    }

    operator fun times(other: Color) = Color(r * other.r, g * other.g, b * other.b, a * other.a)
    operator fun times(scalar: Float) = Color(r * scalar, g * scalar, b * scalar, a * scalar)
}
