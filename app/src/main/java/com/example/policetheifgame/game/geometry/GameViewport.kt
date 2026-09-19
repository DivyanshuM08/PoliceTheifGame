package com.example.policetheifgame.game.geometry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.policetheifgame.game.model.Point2D

/**
 * Transforms between World Coordinates (in meters) and Screen Coordinates (in Canvas pixels).
 *
 * World coordinates:
 * - Y increases from 0 (Start at bottom) to 100+ (Finish at top).
 * - X spans across the road path.
 *
 * Screen coordinates:
 * - (0, 0) is top-left in pixels.
 * - Y increases downwards.
 */
data class GameViewport(
    val screenSize: Size = Size.Zero,
    val cameraCenterWorld: Point2D = Point2D(60f, 20f),
    val visibleHeightMeters: Float = 32f
) {
    /**
     * Pixels per world meter (uniform scale preserving aspect ratio).
     */
    val scale: Float
        get() = if (visibleHeightMeters > 0f && screenSize.height > 0f) {
            screenSize.height / visibleHeightMeters
        } else {
            1f
        }

    val visibleWidthMeters: Float
        get() = if (scale > 0f) screenSize.width / scale else 0f

    val minWorldX: Float
        get() = cameraCenterWorld.x - visibleWidthMeters / 2f

    val maxWorldX: Float
        get() = cameraCenterWorld.x + visibleWidthMeters / 2f

    val minWorldY: Float
        get() = cameraCenterWorld.y - visibleHeightMeters / 2f

    val maxWorldY: Float
        get() = cameraCenterWorld.y + visibleHeightMeters / 2f

    /**
     * Converts a World Point (meters) to Screen Offset (pixels).
     */
    fun worldToScreen(worldPoint: Point2D): Offset {
        val screenX = (worldPoint.x - minWorldX) * scale
        // Invert Y so higher world Y is towards the top of the screen (smaller screen Y)
        val screenY = (maxWorldY - worldPoint.y) * scale
        return Offset(screenX, screenY)
    }

    /**
     * Converts Screen Offset (pixels) to World Point (meters).
     */
    fun screenToWorld(screenOffset: Offset): Point2D {
        val worldX = minWorldX + screenOffset.x / scale
        val worldY = maxWorldY - screenOffset.y / scale
        return Point2D(worldX, worldY)
    }

    /**
     * Converts meters to pixels.
     */
    fun metersToPixels(meters: Float): Float = meters * scale

    /**
     * Converts pixels to meters.
     */
    fun pixelsToMeters(pixels: Float): Float = if (scale > 0f) pixels / scale else 0f

    companion object {
        /**
         * Creates a static overview viewport that fits the entire road (from start to finish)
         * onto the screen, with padding so the police car at Y=0m and finish line at Y=100m
         * are completely visible.
         */
        fun createOverview(
            screenSize: Size,
            roadGeometry: RoadGeometry
        ): GameViewport {
            if (screenSize.width <= 0f || screenSize.height <= 0f) {
                return GameViewport(screenSize = screenSize)
            }
            val minY = roadGeometry.minY
            val maxY = roadGeometry.maxY
            val roadYHeight = (maxY - minY).coerceAtLeast(10f)

            // Padding: 6m at bottom keeps police car (5.6m length, centered at Y=0) fully visible.
            // 5m at top keeps checkered finish line and cars fully visible below top edge.
            val bottomPadding = 6.0f
            val topPadding = 5.0f
            val totalHeightNeeded = roadYHeight + bottomPadding + topPadding

            val roadWidthNeeded = (roadGeometry.maxX - roadGeometry.minX + 6.0f).coerceAtLeast(20f)
            val scaleY = screenSize.height / totalHeightNeeded
            val scaleX = screenSize.width / roadWidthNeeded

            val finalScale = minOf(scaleX, scaleY)
            val visibleHeight = screenSize.height / finalScale

            val centerY = ((minY - bottomPadding) + (maxY + topPadding)) / 2f
            val centerX = roadGeometry.centerX

            return GameViewport(
                screenSize = screenSize,
                cameraCenterWorld = Point2D(centerX, centerY),
                visibleHeightMeters = visibleHeight
            )
        }
    }
}

