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
         * onto the screen at once, taking top HUD and bottom insets into account.
         */
        fun createOverview(
            screenSize: Size,
            roadGeometry: RoadGeometry
        ): GameViewport {
            if (screenSize.width <= 0f || screenSize.height <= 0f) {
                return GameViewport(screenSize = screenSize)
            }
            val roadHeight = roadGeometry.finishPositionMeters - roadGeometry.startPositionMeters
            val roadWidth = (roadGeometry.maxX - roadGeometry.minX).coerceAtLeast(20f)

            // Leave space for HUD at top (approx 20m) and start padding at bottom (approx 6m)
            val topPadding = 20f
            val bottomPadding = 6f
            val sidePadding = 4f

            val totalHeightNeeded = roadHeight + topPadding + bottomPadding
            val totalWidthNeeded = roadWidth + (sidePadding * 2f)

            val scaleY = screenSize.height / totalHeightNeeded
            val scaleX = screenSize.width / totalWidthNeeded

            val finalScale = minOf(scaleX, scaleY)
            val visibleHeight = screenSize.height / finalScale

            val centerY = (roadGeometry.startPositionMeters - bottomPadding + roadGeometry.finishPositionMeters + topPadding) / 2f
            val centerX = roadGeometry.centerX

            return GameViewport(
                screenSize = screenSize,
                cameraCenterWorld = Point2D(centerX, centerY),
                visibleHeightMeters = visibleHeight
            )
        }
    }
}

