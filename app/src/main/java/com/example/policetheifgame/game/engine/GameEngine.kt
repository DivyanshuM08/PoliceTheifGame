package com.example.policetheifgame.game.engine

import com.example.policetheifgame.game.geometry.LevelData
import com.example.policetheifgame.game.geometry.RoadGeometry
import com.example.policetheifgame.game.model.GameOverReason
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import com.example.policetheifgame.game.model.Point2D
import kotlin.math.max
import kotlin.math.min

/**
 * Core game engine executing the simulation logic in world coordinates.
 * Completely decoupled from Android UI, Compose, and screen pixels.
 */
class GameEngine(
    var roadGeometry: RoadGeometry,
    var thiefSpeedMps: Float = 10.0f,
    var initialThiefDistanceMeters: Float = 20.0f,
    val initialPoliceDistanceMeters: Float = 0.0f,
    val catchDistanceMeters: Float = 2.5f,
    val roadBoundaryToleranceMeters: Float = 0.6f
) {

    var status: GameStatus = GameStatus.READY
        private set

    var reason: GameOverReason = GameOverReason.NONE
        private set

    val isGameOver: Boolean get() = status == GameStatus.POLICE_WON || status == GameStatus.THIEF_WON

    var policeDistanceMeters: Float = initialPoliceDistanceMeters
        private set

    var policePosition: Point2D = Point2D(0f, 0f)
        private set

    var policeHeadingDeg: Float = 0f
        private set

    var thiefDistanceMeters: Float = initialThiefDistanceMeters
        private set

    var thiefPosition: Point2D = Point2D(0f, 0f)
        private set

    var thiefHeadingDeg: Float = 0f
        private set

    var policeSpeedMps: Float = 0f
        private set

    var policeSpeedMultiplier: Float = 1.0f
        private set

    private var lastDragTimeNanos: Long = 0L
    private var thiefRouteCumulativeDistances: FloatArray = FloatArray(0)

    init {
        reset()
    }

    /**
     * Resets the game to initial ready state.
     */
    fun reset() {
        status = GameStatus.READY
        reason = GameOverReason.NONE
        policeSpeedMps = 0f
        lastDragTimeNanos = 0L

        if (roadGeometry.isPuzzle) {
            policeDistanceMeters = 0f
            thiefDistanceMeters = 0f
            policePosition = roadGeometry.policeStartPosition
            thiefPosition = roadGeometry.thiefStartPosition

            val route = roadGeometry.thiefRoute
            if (route.size >= 2) {
                thiefRouteCumulativeDistances = FloatArray(route.size)
                thiefRouteCumulativeDistances[0] = 0f
                for (i in 0 until route.size - 1) {
                    thiefRouteCumulativeDistances[i + 1] = thiefRouteCumulativeDistances[i] + route[i].distanceTo(route[i + 1])
                }
                val dx = route[1].x - route[0].x
                val dy = route[1].y - route[0].y
                thiefHeadingDeg = Math.toDegrees(kotlin.math.atan2(dx.toDouble(), dy.toDouble())).toFloat()
            } else {
                thiefRouteCumulativeDistances = FloatArray(0)
                thiefHeadingDeg = 0f
            }
            policeHeadingDeg = 0f
        } else {
            policeDistanceMeters = initialPoliceDistanceMeters
            thiefDistanceMeters = initialThiefDistanceMeters
            policePosition = roadGeometry.getRoadCenterAtDistance(policeDistanceMeters)
            policeHeadingDeg = roadGeometry.getRoadHeadingAtDistance(policeDistanceMeters)
            thiefPosition = roadGeometry.getRoadCenterAtDistance(thiefDistanceMeters)
            thiefHeadingDeg = roadGeometry.getRoadHeadingAtDistance(thiefDistanceMeters)
        }
    }

    /**
     * Starts or resumes the game loop.
     */
    fun start() {
        if (status == GameStatus.READY) {
            status = GameStatus.PLAYING
        }
    }

    /**
     * Pauses the active game.
     */
    fun pause() {
        if (status == GameStatus.PLAYING) {
            status = GameStatus.PAUSED
        }
    }

    /**
     * Resumes a paused game.
     */
    fun resume() {
        if (status == GameStatus.PAUSED) {
            status = GameStatus.PLAYING
            lastDragTimeNanos = 0L // prevent delta spike
        }
    }

    /**
     * Restarts the game from scratch.
     */
    fun restart() {
        reset()
        start()
    }

    /**
     * Loads a new level and resets the engine.
     */
    fun loadLevel(levelData: LevelData, levelIndex: Int = 0) {
        roadGeometry = RoadGeometry(levelData)
        thiefSpeedMps = levelData.thiefSpeedMps
        initialThiefDistanceMeters = levelData.initialGapMeters
        policeSpeedMultiplier = 1.0f
        reset()
    }

    /**
     * Advances the simulation by [deltaTimeSeconds].
     */
    fun tick(deltaTimeSeconds: Float) {
        if (status != GameStatus.PLAYING) return
        if (deltaTimeSeconds <= 0f) return

        if (roadGeometry.isPuzzle) {
            val route = roadGeometry.thiefRoute
            val totalRouteDist = if (thiefRouteCumulativeDistances.isNotEmpty()) thiefRouteCumulativeDistances.last() else 100f
            thiefDistanceMeters += thiefSpeedMps * deltaTimeSeconds

            if (thiefDistanceMeters >= totalRouteDist || thiefPosition.distanceTo(roadGeometry.destinationPosition) <= catchDistanceMeters) {
                thiefPosition = roadGeometry.destinationPosition
                status = GameStatus.THIEF_WON
                reason = GameOverReason.THIEF_ESCAPED
                return
            }

            // Interpolate position and heading along thiefRoute
            if (route.size >= 2 && thiefRouteCumulativeDistances.isNotEmpty()) {
                for (i in 0 until route.size - 1) {
                    val d0 = thiefRouteCumulativeDistances[i]
                    val d1 = thiefRouteCumulativeDistances[i + 1]
                    if (thiefDistanceMeters in d0..d1 || i == route.size - 2) {
                        val segLen = d1 - d0
                        val t = if (segLen > 1e-4f) (thiefDistanceMeters - d0) / segLen else 0f
                        thiefPosition = route[i].lerp(route[i + 1], t)
                        val dx = route[i + 1].x - route[i].x
                        val dy = route[i + 1].y - route[i].y
                        thiefHeadingDeg = Math.toDegrees(kotlin.math.atan2(dx.toDouble(), dy.toDouble())).toFloat()
                        break
                    }
                }
            }

            checkCatchCondition()
            return
        }

        // 1. Classic: Advance thief along the road
        thiefDistanceMeters += thiefSpeedMps * deltaTimeSeconds
        thiefPosition = roadGeometry.getRoadCenterAtDistance(thiefDistanceMeters)
        thiefHeadingDeg = roadGeometry.getRoadHeadingAtDistance(thiefDistanceMeters)

        // 2. Check if thief reached finish line
        if (thiefDistanceMeters >= roadGeometry.finishPositionMeters) {
            status = GameStatus.THIEF_WON
            reason = GameOverReason.THIEF_ESCAPED
            return
        }

        // 3. Check catch condition
        checkCatchCondition()
    }

    /**
     * Handles police dragging by the player in world coordinates.
     * Police car moves 1:1 with user's touch point within boundaries,
     * and police speed is calculated directly from finger drag velocity.
     */
    fun onPoliceDragged(targetWorldPoint: Point2D, currentTimeNanos: Long = System.nanoTime()) {
        if (status == GameStatus.READY) {
            start()
        }
        if (status != GameStatus.PLAYING) return

        // Validate if touch point is within road boundaries
        val onRoad = roadGeometry.isPositionOnRoad(targetWorldPoint, roadBoundaryToleranceMeters)
        if (!onRoad) {
            // Police went off-road -> Thief wins
            policePosition = targetWorldPoint
            status = GameStatus.THIEF_WON
            reason = GameOverReason.OFF_ROAD
            return
        }

        if (roadGeometry.isPuzzle) {
            val moveVec = targetWorldPoint - policePosition
            val distMoved = moveVec.length()
            policeDistanceMeters += distMoved

            // Speed calculation
            if (lastDragTimeNanos > 0L && currentTimeNanos > lastDragTimeNanos) {
                val dtSeconds = (currentTimeNanos - lastDragTimeNanos) / 1_000_000_000f
                if (dtSeconds > 0.001f) {
                    val instantSpeed = distMoved / dtSeconds
                    policeSpeedMps = 0.7f * policeSpeedMps + 0.3f * instantSpeed
                }
            }
            lastDragTimeNanos = currentTimeNanos

            // Dynamically rotate heading in direction of motion (supports 180° U-turns!)
            if (moveVec.lengthSquared() > 0.04f) {
                val angle = Math.toDegrees(kotlin.math.atan2(moveVec.x.toDouble(), moveVec.y.toDouble())).toFloat()
                policeHeadingDeg = angle
            }
            policePosition = targetWorldPoint
            checkCatchCondition()
            return
        }

        // Classic highway projection
        val projection = roadGeometry.getNearestRoadPosition(targetWorldPoint)
        val newDistance = projection.distanceAlongRoadMeters.coerceAtLeast(0f)
        val deltaDist = newDistance - policeDistanceMeters

        // Calculate speed directly from user finger movement
        if (lastDragTimeNanos > 0L && currentTimeNanos > lastDragTimeNanos) {
            val dtSeconds = (currentTimeNanos - lastDragTimeNanos) / 1_000_000_000f
            if (dtSeconds > 0.001f) {
                val instantSpeed = deltaDist / dtSeconds
                policeSpeedMps = 0.7f * policeSpeedMps + 0.3f * instantSpeed
            }
        }
        lastDragTimeNanos = currentTimeNanos

        policeDistanceMeters = newDistance
        policePosition = targetWorldPoint
        policeHeadingDeg = roadGeometry.getRoadHeadingAtDistance(policeDistanceMeters)

        // Check if drag resulted in a catch
        checkCatchCondition()
    }

    private fun checkCatchCondition() {
        val distanceBetweenCars = policePosition.distanceTo(thiefPosition)
        val caughtByProximity = distanceBetweenCars <= catchDistanceMeters
        val caughtByDistance = !roadGeometry.isPuzzle && policeDistanceMeters >= thiefDistanceMeters

        if (caughtByProximity || caughtByDistance) {
            status = GameStatus.POLICE_WON
            reason = GameOverReason.CAUGHT_THIEF
        }
    }

    /**
     * Produces a snapshot of current game state for the UI.
     */
    fun getSnapshot(): GameState {
        val totalLen = if (roadGeometry.isPuzzle) {
            if (thiefRouteCumulativeDistances.isNotEmpty()) thiefRouteCumulativeDistances.last() else roadGeometry.roadLengthMeters
        } else {
            roadGeometry.finishPositionMeters
        }
        val gap = if (roadGeometry.isPuzzle) {
            policePosition.distanceTo(thiefPosition)
        } else {
            max(0f, thiefDistanceMeters - policeDistanceMeters)
        }
        return GameState(
            status = status,
            reason = reason,
            policeDistanceMeters = policeDistanceMeters,
            policePosition = policePosition,
            policeHeadingDeg = policeHeadingDeg,
            thiefDistanceMeters = thiefDistanceMeters,
            thiefPosition = thiefPosition,
            thiefHeadingDeg = thiefHeadingDeg,
            policeSpeedMps = max(0f, policeSpeedMps),
            thiefSpeedMps = thiefSpeedMps,
            gapMeters = gap,
            roadLengthMeters = totalLen,
            cameraCenter = Point2D(roadGeometry.centerX, roadGeometry.centerY),
            policeSpeedMultiplier = policeSpeedMultiplier,
            isPuzzle = roadGeometry.isPuzzle,
            destinationPosition = if (roadGeometry.isPuzzle) roadGeometry.destinationPosition else null
        )
    }

}
