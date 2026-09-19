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
 * Represents an individual road corridor or lane in a multi-pathway maze puzzle.
 */
data class RoadCorridor(
    val id: String = "",
    val path: List<Point2D>,
    val widthMeters: Float = 8.0f,
    val isDeadEnd: Boolean = false
)

/**
 * Raw data model for a level's road geometry and parameters.
 * Supports both classic single-track highway courses (Levels 1-10) and
 * Pac-Man style multi-corridor puzzle mazes (Levels 11-20).
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
    val boundaries: List<RoadBoundary>,
    val isPuzzle: Boolean = false,
    val corridors: List<RoadCorridor> = emptyList(),
    val policeStartPosition: Point2D? = null,
    val thiefStartPosition: Point2D? = null,
    val destinationPosition: Point2D? = null,
    val thiefRoute: List<Point2D> = emptyList()
) {
    companion object {
        /**
         * Default Level 1 geometry matching the reference specification.
         */
        fun createDefaultLevel1(): LevelData {
            val path = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(50.0f, 15.0f),
                Point2D(56.0f, 30.0f),
                Point2D(62.0f, 45.0f),
                Point2D(58.0f, 60.0f),
                Point2D(52.0f, 70.0f),
                Point2D(50.0f, 80.0f)
            )

            val bounds = listOf(
                RoadBoundary(Point2D(44.0f, 0.0f), Point2D(56.0f, 0.0f)),
                RoadBoundary(Point2D(44.0f, 15.0f), Point2D(56.0f, 15.0f)),
                RoadBoundary(Point2D(50.0f, 30.0f), Point2D(62.0f, 30.0f)),
                RoadBoundary(Point2D(56.0f, 45.0f), Point2D(68.0f, 45.0f)),
                RoadBoundary(Point2D(52.0f, 60.0f), Point2D(64.0f, 60.0f)),
                RoadBoundary(Point2D(46.0f, 70.0f), Point2D(58.0f, 70.0f)),
                RoadBoundary(Point2D(44.0f, 80.0f), Point2D(56.0f, 80.0f))
            )

            return LevelData(
                levelId = "level_1",
                title = "Sunny Highway",
                roadLengthMeters = 80.0f,
                roadWidthMeters = 12.0f,
                thiefSpeedMps = 8.0f,
                initialGapMeters = 14.0f,
                startPositionMeters = 0.0f,
                finishPositionMeters = 80.0f,
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

            val isPuzzle = root.optBoolean("isPuzzle", false)
            val corridors = mutableListOf<RoadCorridor>()
            val corridorsArray = root.optJSONArray("corridors")
            if (corridorsArray != null) {
                for (i in 0 until corridorsArray.length()) {
                    val cObj = corridorsArray.optJSONObject(i)
                    if (cObj != null) {
                        val cid = cObj.optString("id", "c_$i")
                        val cWidth = cObj.optDouble("widthMeters", roadWidth.toDouble()).toFloat()
                        val isDeadEnd = cObj.optBoolean("isDeadEnd", false)
                        val cPath = mutableListOf<Point2D>()
                        val cPathArr = cObj.optJSONArray("path") ?: JSONArray()
                        for (j in 0 until cPathArr.length()) {
                            parsePoint(cPathArr.get(j))?.let { cPath.add(it) }
                        }
                        if (cPath.size >= 2) {
                            corridors.add(RoadCorridor(cid, cPath, cWidth, isDeadEnd))
                        }
                    }
                }
            }

            val policeStart = parsePoint(root.opt("policeStart"))
            val thiefStart = parsePoint(root.opt("thiefStart"))
            val destination = parsePoint(root.opt("destination"))

            val thiefRoute = mutableListOf<Point2D>()
            val thiefRouteArr = root.optJSONArray("thiefRoute")
            if (thiefRouteArr != null) {
                for (i in 0 until thiefRouteArr.length()) {
                    parsePoint(thiefRouteArr.get(i))?.let { thiefRoute.add(it) }
                }
            }

            // Fallback for roadPath in puzzle levels: ensure roadPath has at least 2 points
            val finalRoadPath = if (pathPoints.size >= 2) {
                pathPoints
            } else if (thiefRoute.size >= 2) {
                thiefRoute
            } else if (corridors.isNotEmpty() && corridors.first().path.size >= 2) {
                corridors.first().path
            } else {
                listOf(Point2D(50f, 0f), Point2D(50f, 10f))
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
                roadPath = finalRoadPath,
                boundaries = boundaries,
                isPuzzle = isPuzzle,
                corridors = corridors,
                policeStartPosition = policeStart,
                thiefStartPosition = thiefStart,
                destinationPosition = destination,
                thiefRoute = thiefRoute
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
