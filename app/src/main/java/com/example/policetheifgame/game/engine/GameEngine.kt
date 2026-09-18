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

    init {
        reset()
    }

    /**
     * Resets the game to initial ready state.
     */
    fun reset() {
        status = GameStatus.READY
        reason = GameOverReason.NONE
        policeDistanceMeters = initialPoliceDistanceMeters
        thiefDistanceMeters = initialThiefDistanceMeters
        policeSpeedMps = 0f
        lastDragTimeNanos = 0L

        policePosition = roadGeometry.getRoadCenterAtDistance(policeDistanceMeters)
        policeHeadingDeg = roadGeometry.getRoadHeadingAtDistance(policeDistanceMeters)

        thiefPosition = roadGeometry.getRoadCenterAtDistance(thiefDistanceMeters)
        thiefHeadingDeg = roadGeometry.getRoadHeadingAtDistance(thiefDistanceMeters)
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

        // 1. Advance thief along the road
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

        // Project position onto the road
        val projection = roadGeometry.getNearestRoadPosition(targetWorldPoint)
        val newDistance = projection.distanceAlongRoadMeters.coerceAtLeast(0f)
        val deltaDist = newDistance - policeDistanceMeters

        // Calculate speed directly from user finger movement
        if (lastDragTimeNanos > 0L && currentTimeNanos > lastDragTimeNanos) {
            val dtSeconds = (currentTimeNanos - lastDragTimeNanos) / 1_000_000_000f
            if (dtSeconds > 0.001f) {
                val instantSpeed = deltaDist / dtSeconds
                // Smooth speed
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
        val caughtByDistance = policeDistanceMeters >= thiefDistanceMeters

        if (caughtByProximity || caughtByDistance) {
            status = GameStatus.POLICE_WON
            reason = GameOverReason.CAUGHT_THIEF
        }
    }

    /**
     * Produces a snapshot of current game state for the UI.
     */
    fun getSnapshot(): GameState {
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
            gapMeters = max(0f, thiefDistanceMeters - policeDistanceMeters),
            roadLengthMeters = roadGeometry.finishPositionMeters,
            cameraCenter = Point2D(roadGeometry.centerX, roadGeometry.centerY),
            policeSpeedMultiplier = policeSpeedMultiplier
        )
    }

}
