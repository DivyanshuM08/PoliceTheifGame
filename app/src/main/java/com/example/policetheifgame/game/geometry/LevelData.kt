package com.example.policetheifgame.game.geometry

import com.example.policetheifgame.game.model.Point2D
import org.json.JSONArray
import org.json.JSONObject

/**
 * Boundary pair representing the left and right road edge at a particular cross-section.
 */
data class RoadBoundary(
    val left: Point2D,
    val right: Point2D
)

/**
 * Raw data model for a level's road geometry and parameters.
 */
data class LevelData(
    val levelId: String,
    val title: String = "Level",
    val roadLengthMeters: Float,
    val roadWidthMeters: Float,
    val thiefSpeedMps: Float = 10.0f,
    val initialGapMeters: Float = 20.0f,
    val startPositionMeters: Float,
    val finishPositionMeters: Float,
    val roadPath: List<Point2D>,
    val boundaries: List<RoadBoundary>
) {
    companion object {
        /**
         * Default Level 1 geometry matching the reference specification.
         */
        fun createDefaultLevel1(): LevelData {
            val path = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(50.0f, 10.0f),
                Point2D(52.0f, 20.0f),
                Point2D(56.0f, 30.0f),
                Point2D(62.0f, 40.0f),
                Point2D(68.0f, 50.0f),
                Point2D(74.0f, 60.0f),
                Point2D(78.0f, 70.0f),
                Point2D(76.0f, 80.0f),
                Point2D(70.0f, 90.0f),
                Point2D(60.0f, 100.0f)
            )

            val bounds = listOf(
                RoadBoundary(Point2D(44.0f, 0.0f), Point2D(56.0f, 0.0f)),
                RoadBoundary(Point2D(44.0f, 10.0f), Point2D(56.0f, 10.0f)),
                RoadBoundary(Point2D(46.0f, 20.0f), Point2D(58.0f, 20.0f)),
                RoadBoundary(Point2D(50.0f, 30.0f), Point2D(62.0f, 30.0f)),
                RoadBoundary(Point2D(56.0f, 40.0f), Point2D(68.0f, 40.0f)),
                RoadBoundary(Point2D(62.0f, 50.0f), Point2D(74.0f, 50.0f)),
                RoadBoundary(Point2D(68.0f, 60.0f), Point2D(80.0f, 60.0f)),
                RoadBoundary(Point2D(72.0f, 70.0f), Point2D(84.0f, 70.0f)),
                RoadBoundary(Point2D(70.0f, 80.0f), Point2D(82.0f, 80.0f)),
                RoadBoundary(Point2D(64.0f, 90.0f), Point2D(76.0f, 90.0f)),
                RoadBoundary(Point2D(54.0f, 100.0f), Point2D(66.0f, 100.0f))
            )

            return LevelData(
                levelId = "level_1",
                title = "Sunny Highway",
                roadLengthMeters = 100.0f,
                roadWidthMeters = 12.0f,
                thiefSpeedMps = 9.0f,
                initialGapMeters = 16.0f,
                startPositionMeters = 0.0f,
                finishPositionMeters = 100.0f,
                roadPath = path,
                boundaries = bounds
            )
        }

        /**
         * Parses LevelData from a JSON string.
         * Supports both object-based points {"x": 50, "y": 0} and array-based points [50, 0].
         */
        fun fromJson(jsonStr: String): LevelData {
            val root = JSONObject(jsonStr)
            val levelId = root.optString("levelId", "custom_level")
            val title = root.optString("title", "Level $levelId")
            val roadLength = (root.optDouble("roadLengthMeters", root.optDouble("roadLength", 100.0))).toFloat()
            val roadWidth = (root.optDouble("roadWidthMeters", root.optDouble("roadWidth", 8.0))).toFloat()
            val thiefSpeed = root.optDouble("thiefSpeedMps", 10.0).toFloat()
            val initialGap = root.optDouble("initialGapMeters", 20.0).toFloat()
            val startPos = (root.optDouble("startPositionMeters", root.optDouble("startPosition", 0.0))).toFloat()
            val finishPos = (root.optDouble("finishPositionMeters", root.optDouble("finishPosition", roadLength.toDouble()))).toFloat()

            val pathPoints = mutableListOf<Point2D>()
            val pathArray = root.optJSONArray("roadPath") ?: JSONArray()
            for (i in 0 until pathArray.length()) {
                val item = pathArray.get(i)
                parsePoint(item)?.let { pathPoints.add(it) }
            }

            val boundaries = mutableListOf<RoadBoundary>()
            val boundsArray = root.optJSONArray("boundaries") ?: JSONArray()
            for (i in 0 until boundsArray.length()) {
                val obj = boundsArray.optJSONObject(i)
                if (obj != null) {
                    val leftPt = parsePoint(obj.opt("left"))
                    val rightPt = parsePoint(obj.opt("right"))
                    if (leftPt != null && rightPt != null) {
                        boundaries.add(RoadBoundary(leftPt, rightPt))
                    }
                }
            }

            return LevelData(
                levelId = levelId,
                title = title,
                roadLengthMeters = roadLength,
                roadWidthMeters = roadWidth,
                thiefSpeedMps = thiefSpeed,
                initialGapMeters = initialGap,
                startPositionMeters = startPos,
                finishPositionMeters = finishPos,
                roadPath = pathPoints,
                boundaries = boundaries
            )
        }

        private fun parsePoint(obj: Any?): Point2D? {
            return when (obj) {
                is JSONObject -> {
                    val x = obj.optDouble("x", 0.0).toFloat()
                    val y = obj.optDouble("y", 0.0).toFloat()
                    Point2D(x, y)
                }
                is JSONArray -> {
                    if (obj.length() >= 2) {
                        val x = obj.getDouble(0).toFloat()
                        val y = obj.getDouble(1).toFloat()
                        Point2D(x, y)
                    } else null
                }
                else -> null
            }
        }
    }
}
