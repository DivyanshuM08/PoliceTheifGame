package com.example.policetheifgame.game.model

import kotlin.math.hypot
import kotlin.math.sqrt

/**
 * Represents a 2D point or vector in world coordinates (meters).
 */
data class Point2D(
    val x: Float,
    val y: Float
) {
    fun distanceTo(other: Point2D): Float {
        return hypot(other.x - x, other.y - y)
    }

    fun distanceSquaredTo(other: Point2D): Float {
        val dx = other.x - x
        val dy = other.y - y
        return dx * dx + dy * dy
    }

    fun length(): Float = hypot(x, y)

    fun lengthSquared(): Float = x * x + y * y

    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)

    operator fun minus(other: Point2D) = Point2D(x - other.x, y - other.y)

    operator fun times(scalar: Float) = Point2D(x * scalar, y * scalar)

    operator fun div(scalar: Float) = Point2D(x / scalar, y / scalar)

    fun dot(other: Point2D): Float = x * other.x + y * other.y

    fun normalized(): Point2D {
        val len = length()
        return if (len > 1e-6f) Point2D(x / len, y / len) else Point2D(0f, 0f)
    }

    fun lerp(other: Point2D, t: Float): Point2D {
        val clampedT = t.coerceIn(0f, 1f)
        return Point2D(
            x = x + (other.x - x) * clampedT,
            y = y + (other.y - y) * clampedT
        )
    }
}
