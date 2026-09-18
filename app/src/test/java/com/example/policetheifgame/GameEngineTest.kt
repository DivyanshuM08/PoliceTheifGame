package com.example.policetheifgame

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.policetheifgame.game.engine.GameEngine
import com.example.policetheifgame.game.geometry.GameViewport
import com.example.policetheifgame.game.geometry.LevelData
import com.example.policetheifgame.game.geometry.RoadBoundary
import com.example.policetheifgame.game.geometry.RoadGeometry
import com.example.policetheifgame.game.model.GameOverReason
import com.example.policetheifgame.game.model.GameStatus
import com.example.policetheifgame.game.model.Point2D
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var defaultLevel: LevelData
    private lateinit var roadGeometry: RoadGeometry
    private lateinit var gameEngine: GameEngine

    @Before
    fun setUp() {
        defaultLevel = LevelData.createDefaultLevel1()
        roadGeometry = RoadGeometry(defaultLevel)
        gameEngine = GameEngine(
            roadGeometry = roadGeometry,
            thiefSpeedMps = 10.0f,
            initialThiefDistanceMeters = 20.0f,
            initialPoliceDistanceMeters = 0.0f,
            catchDistanceMeters = 2.5f,
            roadBoundaryToleranceMeters = 0.6f
        )
    }

    /**
     * Requirement 1: Thief moves exactly 10m in approximately 1 second.
     */
    @Test
    fun test1_thiefMovesExactly10MetersIn1Second() {
        gameEngine.start()
        val initialDist = gameEngine.thiefDistanceMeters
        assertEquals(20.0f, initialDist, 0.001f)

        // Advance by 1.0 second
        gameEngine.tick(1.0f)

        val newDist = gameEngine.thiefDistanceMeters
        assertEquals(initialDist + 10.0f, newDist, 0.001f)
    }

    /**
     * Requirement 2: Thief follows road distance rather than screen pixels.
     */
    @Test
    fun test2_thiefFollowsRoadDistanceRatherThanScreenPixels() {
        gameEngine.start()

        // Thief starts at 20m along the road
        val startThiefPos = gameEngine.thiefPosition
        val expectedAt20m = roadGeometry.getRoadCenterAtDistance(20.0f)
        assertEquals(expectedAt20m.x, startThiefPos.x, 0.1f)
        assertEquals(expectedAt20m.y, startThiefPos.y, 0.1f)

        // Advance thief to 50m (in 3.0s: 20m + 30m = 50m)
        gameEngine.tick(3.0f)
        assertEquals(50.0f, gameEngine.thiefDistanceMeters, 0.001f)

        val expectedAt50m = roadGeometry.getRoadCenterAtDistance(50.0f)
        assertEquals(expectedAt50m.x, gameEngine.thiefPosition.x, 0.1f)
        assertEquals(expectedAt50m.y, gameEngine.thiefPosition.y, 0.1f)

        // The path curves in X, verifying position is dictated by road curve
        assertTrue("X coordinate must shift due to road curvature", gameEngine.thiefPosition.x > startThiefPos.x)
    }

    /**
     * Requirement 3: Police can move within road boundaries.
     */
    @Test
    fun test3_policeCanMoveWithinRoadBoundaries() {
        gameEngine.start()

        // At Y = 10m, road center is at X = 50m, boundaries are [44m, 56m]
        val validPolicePoint = Point2D(48.0f, 10.0f)
        gameEngine.onPoliceDragged(validPolicePoint)

        assertEquals(GameStatus.PLAYING, gameEngine.status)
        assertEquals(GameOverReason.NONE, gameEngine.reason)
        assertEquals(validPolicePoint.x, gameEngine.policePosition.x, 0.01f)
        assertEquals(validPolicePoint.y, gameEngine.policePosition.y, 0.01f)
        assertTrue(gameEngine.policeDistanceMeters > 0f)
    }

    /**
     * Requirement 4: Police outside the road triggers loss.
     */
    @Test
    fun test4_policeOutsideRoadTriggersLoss() {
        gameEngine.start()

        // At Y = 10m, boundaries are [44m, 56m]. Point (15, 10) is far in the grass.
        val offRoadPoint = Point2D(15.0f, 10.0f)
        gameEngine.onPoliceDragged(offRoadPoint)

        assertEquals(GameStatus.THIEF_WON, gameEngine.status)
        assertEquals(GameOverReason.OFF_ROAD, gameEngine.reason)
    }

    /**
     * Requirement 5: Police catching thief triggers win.
     */
    @Test
    fun test5_policeCatchingThiefTriggersWin() {
        gameEngine.start()

        // Thief is at 20m (around Point2D(52f, 20f))
        val thiefPos = gameEngine.thiefPosition
        // Drag police to within 1m of thief
        val nearThiefPoint = Point2D(thiefPos.x, thiefPos.y - 1.0f)
        gameEngine.onPoliceDragged(nearThiefPoint)

        assertEquals(GameStatus.POLICE_WON, gameEngine.status)
        assertEquals(GameOverReason.CAUGHT_THIEF, gameEngine.reason)
    }

    /**
     * Requirement 6: Thief reaching finish triggers loss if not caught.
     */
    @Test
    fun test6_thiefReachingFinishTriggersLossIfNotCaught() {
        gameEngine.start()

        val finishDist = roadGeometry.finishPositionMeters
        val remainingDist = finishDist - gameEngine.thiefDistanceMeters
        val timeUntilJustBeforeFinish = (remainingDist - 2.0f) / gameEngine.thiefSpeedMps

        // Advance until just before finish line
        gameEngine.tick(timeUntilJustBeforeFinish)
        assertEquals(GameStatus.PLAYING, gameEngine.status)

        // Advance past finish line at the end of the road
        gameEngine.tick(1.0f)
        assertEquals(GameStatus.THIEF_WON, gameEngine.status)
        assertEquals(GameOverReason.THIEF_ESCAPED, gameEngine.reason)
    }

    /**
     * Requirement 7: Different road geometry can be loaded without changing game logic.
     */
    @Test
    fun test7_differentRoadGeometryCanBeLoadedWithoutChangingGameLogic() {
        // Create a custom straight vertical road
        val customLevel = LevelData(
            levelId = "level_straight",
            roadLengthMeters = 80.0f,
            roadWidthMeters = 10.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 80.0f,
            roadPath = listOf(
                Point2D(0.0f, 0.0f),
                Point2D(0.0f, 40.0f),
                Point2D(0.0f, 80.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(-5.0f, 0.0f), Point2D(5.0f, 0.0f)),
                RoadBoundary(Point2D(-5.0f, 40.0f), Point2D(5.0f, 40.0f)),
                RoadBoundary(Point2D(-5.0f, 80.0f), Point2D(5.0f, 80.0f))
            )
        )

        val customEngine = GameEngine(
            roadGeometry = RoadGeometry(customLevel),
            thiefSpeedMps = 10.0f,
            initialThiefDistanceMeters = 15.0f
        )
        customEngine.start()

        // Thief moves along straight road
        customEngine.tick(1.0f)
        assertEquals(25.0f, customEngine.thiefDistanceMeters, 0.001f)
        assertEquals(0.0f, customEngine.thiefPosition.x, 0.01f)
        assertEquals(25.0f, customEngine.thiefPosition.y, 0.01f)

        // Dragging outside (-10, 20) triggers off-road loss
        customEngine.onPoliceDragged(Point2D(-10.0f, 20.0f))
        assertEquals(GameStatus.THIEF_WON, customEngine.status)
        assertEquals(GameOverReason.OFF_ROAD, customEngine.reason)
    }

    /**
     * Requirement 8: Screen/world coordinate conversion works correctly.
     */
    @Test
    fun test8_screenWorldCoordinateConversionWorksCorrectly() {
        val viewport = GameViewport(
            screenSize = Size(1080f, 2400f),
            cameraCenterWorld = Point2D(60.0f, 40.0f),
            visibleHeightMeters = 32.0f
        )

        val testPoints = listOf(
            Point2D(60.0f, 40.0f), // camera center
            Point2D(50.0f, 20.0f),
            Point2D(75.0f, 55.0f),
            Point2D(46.0f, 0.0f)
        )

        for (pt in testPoints) {
            val screenOffset = viewport.worldToScreen(pt)
            val recoveredPt = viewport.screenToWorld(screenOffset)

            assertEquals("X conversion mismatch", pt.x, recoveredPt.x, 0.001f)
            assertEquals("Y conversion mismatch", pt.y, recoveredPt.y, 0.001f)
        }
    }

    /**
     * Requirement 9: Game restart resets both cars and the initial gap.
     */
    @Test
    fun test9_gameRestartResetsBothCarsAndInitialGap() {
        gameEngine.start()

        // Simulate game progress
        gameEngine.tick(2.5f)
        gameEngine.onPoliceDragged(Point2D(52.0f, 15.0f))

        assertTrue(gameEngine.policeDistanceMeters > 0f)
        assertTrue(gameEngine.thiefDistanceMeters > 20f)

        // Trigger restart
        gameEngine.restart()

        assertEquals(GameStatus.PLAYING, gameEngine.status)
        assertEquals(GameOverReason.NONE, gameEngine.reason)
        assertEquals(0.0f, gameEngine.policeDistanceMeters, 0.001f)
        assertEquals(20.0f, gameEngine.thiefDistanceMeters, 0.001f)

        val expectedGap = 20.0f
        val actualGap = gameEngine.thiefDistanceMeters - gameEngine.policeDistanceMeters
        assertEquals(expectedGap, actualGap, 0.001f)
    }

    /**
     * Requirement 10: Static overview viewport fits the entire road with padding so police car is not cut off.
     */
    @Test
    fun test10_staticOverviewFitsEntireRoadFromStartToFinish() {
        val screenSize = Size(1080f, 2400f)
        val viewport = GameViewport.createOverview(screenSize, roadGeometry)

        // Verify Start line (Y=0m) leaves margin above screen bottom so police car is fully visible
        val startScreen = viewport.worldToScreen(Point2D(roadGeometry.centerX, 0f))
        assertTrue("Start line must be near bottom of screen", startScreen.y in (screenSize.height * 0.9f)..screenSize.height)

        // Verify Finish line (Y=100m) leaves margin below screen top so finish banner is fully visible
        val finishScreen = viewport.worldToScreen(Point2D(roadGeometry.centerX, 100f))
        assertTrue("Finish line must be near top of screen", finishScreen.y in 0f..(screenSize.height * 0.1f))

        // Verify road boundaries are within screen bounds
        val leftScreen = viewport.worldToScreen(Point2D(roadGeometry.minX, 50f))
        val rightScreen = viewport.worldToScreen(Point2D(roadGeometry.maxX, 50f))
        assertTrue("Left road edge must be on screen", leftScreen.x >= -10f)
        assertTrue("Right road edge must be on screen", rightScreen.x <= screenSize.width + 10f)
    }

    /**
     * Requirement 11: LevelRepository loads all 10 levels with progressively increasing challenge.
     */
    @Test
    fun test11_levelRepositoryLoadsAll10LevelsWithIncreasingDifficulty() {
        val totalLevels = com.example.policetheifgame.game.geometry.LevelRepository.totalLevels
        assertEquals(10, totalLevels)

        var previousSpeed = 0f
        var previousWidth = 100f

        for (i in 0 until totalLevels) {
            val level = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(i)
            assertTrue("Level title must not be blank", level.title.isNotBlank())
            assertTrue("Road path must have at least 7 waypoints", level.roadPath.size >= 7)
            assertTrue("Boundaries must match path size", level.boundaries.size == level.roadPath.size)
            assertEquals(100.0f, level.roadLengthMeters, 0.001f)

            // Speed must increase with level
            assertTrue("Thief speed should increase with level: ${level.thiefSpeedMps} > $previousSpeed", level.thiefSpeedMps > previousSpeed)
            previousSpeed = level.thiefSpeedMps

            // Road width should decrease or stay challenging
            assertTrue("Road width should narrow or remain tight: ${level.roadWidthMeters} <= $previousWidth", level.roadWidthMeters <= previousWidth)
            previousWidth = level.roadWidthMeters
        }
    }

    /**
     * Requirement 12: Engine dynamically reconfigures speed and boundaries for higher levels.
     */
    @Test
    fun test12_engineReconfiguresPerLevel() {
        val level5 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(4)
        assertEquals("Coastal Serpent", level5.title)
        assertEquals(15.0f, level5.thiefSpeedMps, 0.001f)
        assertEquals(16.0f, level5.initialGapMeters, 0.001f)

        gameEngine.loadLevel(level5, 4)
        assertEquals(16.0f, gameEngine.thiefDistanceMeters, 0.001f)
        assertEquals(0.0f, gameEngine.policeDistanceMeters, 0.001f)

        gameEngine.start()
        gameEngine.tick(1.0f)

        // Moves at 15.0 m/s from 16.0m = 31.0m
        assertEquals(31.0f, gameEngine.thiefDistanceMeters, 0.001f)
    }

    /**
     * Requirement 13: Game can be paused and resumed; physics pause during PAUSED state.
     */
    @Test
    fun test13_pauseAndResumeGameStopsPhysicsSimulation() {
        gameEngine.start()
        assertEquals(GameStatus.PLAYING, gameEngine.status)

        // Advance 1 second: thief goes from 20m to 30m
        gameEngine.tick(1.0f)
        assertEquals(30.0f, gameEngine.thiefDistanceMeters, 0.001f)

        // Pause game
        gameEngine.pause()
        assertEquals(GameStatus.PAUSED, gameEngine.status)

        // Tick while paused should not advance thief or simulation
        gameEngine.tick(2.0f)
        assertEquals(30.0f, gameEngine.thiefDistanceMeters, 0.001f)

        // Dragging while paused should not move car or alter status
        val initialPolicePos = gameEngine.policePosition
        gameEngine.onPoliceDragged(Point2D(50f, 15f))
        assertEquals(GameStatus.PAUSED, gameEngine.status)

        // Resume game
        gameEngine.resume()
        assertEquals(GameStatus.PLAYING, gameEngine.status)

        // Tick resumes physics
        gameEngine.tick(1.0f)
        assertEquals(40.0f, gameEngine.thiefDistanceMeters, 0.001f)
    }

    /**
     * Requirement 14: Thief speed scales noticeably from level 1 (9.0 m/s) to level 10 (22.5 m/s).
     */
    @Test
    fun test14_thiefSpeedScalesNoticeablyAcrossAllLevels() {
        val level1 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(0)
        assertEquals(9.0f, level1.thiefSpeedMps, 0.001f)

        val level10 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(9)
        assertEquals(22.5f, level10.thiefSpeedMps, 0.001f)

        // Substantial increase: level 10 is more than double level 1 speed
        assertTrue("Level 10 thief must be much faster than Level 1", level10.thiefSpeedMps > level1.thiefSpeedMps * 2f)
    }

    /**
     * Requirement 15: ViewModel handles level selection, mute toggling, and tutorial state.
     */
    @Test
    fun test15_viewModelLevelAndMuteControls() {
        val viewModel = com.example.policetheifgame.ui.GameViewModel(initialLevelIndex = 3)
        assertEquals(3, viewModel.currentLevelIndex)
        assertEquals("Mountain Pass", viewModel.uiState.value.levelTitle)
        assertEquals(10, viewModel.uiState.value.totalLevels)

        // Toggle mute
        val initialMuted = viewModel.isSirenMuted.value
        viewModel.toggleMute()
        assertEquals(!initialMuted, viewModel.isSirenMuted.value)
        assertEquals(!initialMuted, viewModel.uiState.value.isSirenMuted)

        // Tutorial flow
        viewModel.openTutorial()
        assertTrue(viewModel.showTutorial.value)
        viewModel.dismissTutorial()
        assertFalse(viewModel.showTutorial.value)

        // Pause and resume
        viewModel.startGame()
        assertEquals(GameStatus.PLAYING, viewModel.uiState.value.status)
        viewModel.pauseGame()
        assertEquals(GameStatus.PAUSED, viewModel.uiState.value.status)
        viewModel.resumeGame()
        assertEquals(GameStatus.PLAYING, viewModel.uiState.value.status)
    }

    /**
     * Requirement 16: In winding levels (like Level 8), the finish barrier is at the end of the road (Y=100m)
     * and the game does not end midway.
     */
    @Test
    fun test16_finishBarrierIsAtEndOfRoadInCurvedLevels() {
        val level8 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(7)
        assertEquals("Thunder Valley", level8.title)
        val geom = com.example.policetheifgame.game.geometry.RoadGeometry(level8)

        // Finish position must match the actual total path length to the end of the road
        assertEquals(geom.totalPathLengthMeters, geom.finishPositionMeters, 0.001f)

        // The finish boundary must be at Y = 100m (the very end of the road, not midway)
        val (finishLeft, finishRight) = geom.getRoadBoundariesAtDistance(geom.finishPositionMeters)
        assertEquals(100.0f, finishLeft.y, 0.1f)
        assertEquals(100.0f, finishRight.y, 0.1f)

        // Verify game engine does NOT end when thief reaches Y=50m or midway
        gameEngine.loadLevel(level8, 7)
        gameEngine.start()

        // Mid-way along the road: thief should still be playing
        val midDistance = geom.finishPositionMeters * 0.5f
        val timeToMid = (midDistance - gameEngine.thiefDistanceMeters) / gameEngine.thiefSpeedMps
        gameEngine.tick(timeToMid)
        assertEquals(GameStatus.PLAYING, gameEngine.status)
        assertEquals(GameOverReason.NONE, gameEngine.reason)

        // Crossing the finish barrier at the very end of the road triggers escape
        val timeToEnd = (geom.finishPositionMeters - gameEngine.thiefDistanceMeters + 1.0f) / gameEngine.thiefSpeedMps
        gameEngine.tick(timeToEnd)
        assertEquals(GameStatus.THIEF_WON, gameEngine.status)
        assertEquals(GameOverReason.THIEF_ESCAPED, gameEngine.reason)
    }
}


