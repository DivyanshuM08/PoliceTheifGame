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

        // Thief starts at 20m. Finish is at 100m. (80m to go / 10 m/s = 8 seconds)
        // Advance 7 seconds (thief at 90m)
        gameEngine.tick(7.0f)
        assertEquals(GameStatus.PLAYING, gameEngine.status)

        // Advance another 1.5 seconds (thief at 105m >= 100m)
        gameEngine.tick(1.5f)
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
     * Requirement 10: Static overview viewport fits the entire road from start to finish.
     */
    @Test
    fun test10_staticOverviewFitsEntireRoadFromStartToFinish() {
        val screenSize = Size(1080f, 2400f)
        val viewport = GameViewport.createOverview(screenSize, roadGeometry)

        // Verify Start line (Y=0m) is within screen bounds near the bottom
        val startScreen = viewport.worldToScreen(Point2D(roadGeometry.centerX, 0f))
        assertTrue("Start line must be visible on screen", startScreen.y in 0f..screenSize.height)
        assertTrue("Start line should be near bottom of screen", startScreen.y > screenSize.height * 0.75f)

        // Verify Finish line (Y=100m) is within screen bounds below top margin
        val finishScreen = viewport.worldToScreen(Point2D(roadGeometry.centerX, 100f))
        assertTrue("Finish line must be visible on screen", finishScreen.y in 0f..screenSize.height)
        assertTrue("Finish line should be in upper portion of screen", finishScreen.y < screenSize.height * 0.35f)

        // Verify left-most and right-most road boundaries fit horizontally
        val leftScreen = viewport.worldToScreen(Point2D(roadGeometry.minX, 50f))
        val rightScreen = viewport.worldToScreen(Point2D(roadGeometry.maxX, 50f))
        assertTrue("Left road edge must be within screen width", leftScreen.x >= 0f)
        assertTrue("Right road edge must be within screen width", rightScreen.x <= screenSize.width)
    }
}

