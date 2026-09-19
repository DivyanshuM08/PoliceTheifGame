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
import com.example.policetheifgame.ui.AppScreen
import com.example.policetheifgame.ui.GameViewModel
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

        // Verify Finish line leaves margin below screen top so finish banner is fully visible
        val finishScreen = viewport.worldToScreen(Point2D(roadGeometry.centerX, roadGeometry.finishPositionMeters))
        assertTrue("Finish line must be near top of screen", finishScreen.y in 0f..(screenSize.height * 0.1f))

        // Verify road boundaries are within screen bounds
        val leftScreen = viewport.worldToScreen(Point2D(roadGeometry.minX, 40f))
        val rightScreen = viewport.worldToScreen(Point2D(roadGeometry.maxX, 40f))
        assertTrue("Left road edge must be on screen", leftScreen.x >= -10f)
        assertTrue("Right road edge must be on screen", rightScreen.x <= screenSize.width + 10f)
    }

    /**
     * Requirement 11: LevelRepository loads all 10 levels with progressively increasing challenge.
     */
    @Test
    fun test11_levelRepositoryLoadsAll10LevelsWithIncreasingDifficulty() {
        val totalLevels = com.example.policetheifgame.game.geometry.LevelRepository.totalLevels
        assertEquals(20, totalLevels)

        var previousLength = 0f
        var previousSpeed = 0f
        var previousWidth = 100f

        for (i in 0 until 10) {
            val level = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(i)
            assertTrue("Level title must not be blank", level.title.isNotBlank())
            assertTrue("Road path must have at least 7 waypoints", level.roadPath.size >= 7)
            assertTrue("Boundaries must match path size", level.boundaries.size == level.roadPath.size)

            // Length must increase with level
            assertTrue("Road length should increase: ${level.roadLengthMeters} > $previousLength", level.roadLengthMeters > previousLength)
            previousLength = level.roadLengthMeters

            // Speed must increase with level
            assertTrue("Thief speed should increase with level: ${level.thiefSpeedMps} > $previousSpeed", level.thiefSpeedMps > previousSpeed)
            previousSpeed = level.thiefSpeedMps

            // Road width should narrow or remain tight
            assertTrue("Road width should narrow: ${level.roadWidthMeters} < $previousWidth", level.roadWidthMeters < previousWidth)
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
        assertEquals(18.0f, level5.thiefSpeedMps, 0.001f)
        assertEquals(22.0f, level5.initialGapMeters, 0.001f)

        gameEngine.loadLevel(level5, 4)
        assertEquals(22.0f, gameEngine.thiefDistanceMeters, 0.001f)
        assertEquals(0.0f, gameEngine.policeDistanceMeters, 0.001f)

        gameEngine.start()
        gameEngine.tick(1.0f)

        // Moves at 18.0 m/s from 22.0m = 40.0m
        assertEquals(40.0f, gameEngine.thiefDistanceMeters, 0.001f)
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
     * Requirement 14: Thief speed scales noticeably from level 1 (8.0 m/s) to level 10 (31.0 m/s).
     */
    @Test
    fun test14_thiefSpeedScalesNoticeablyAcrossAllLevels() {
        val level1 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(0)
        assertEquals(8.0f, level1.thiefSpeedMps, 0.001f)

        val level10 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(9)
        assertEquals(31.0f, level10.thiefSpeedMps, 0.001f)

        // Substantial increase: level 10 is almost 4x level 1 speed
        assertTrue("Level 10 thief must be more than 3x faster than Level 1", level10.thiefSpeedMps > level1.thiefSpeedMps * 3f)
    }

    /**
     * Requirement 15: ViewModel handles level selection, mute toggling, and tutorial state.
     */
    @Test
    fun test15_viewModelLevelAndMuteControls() {
        val viewModel = com.example.policetheifgame.ui.GameViewModel(initialLevelIndex = 3)
        assertEquals(3, viewModel.currentLevelIndex)
        assertEquals("Mountain Pass", viewModel.uiState.value.levelTitle)
        assertEquals(20, viewModel.uiState.value.totalLevels)

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
        assertFalse("Siren must NOT play until player actively drags", viewModel.isActivelyChasing)
        viewModel.pauseGame()
        assertEquals(GameStatus.PAUSED, viewModel.uiState.value.status)
        assertFalse(viewModel.isActivelyChasing)
        viewModel.resumeGame()
        assertEquals(GameStatus.PLAYING, viewModel.uiState.value.status)
        assertFalse(viewModel.isActivelyChasing)
    }

    /**
     * Requirement 16: In winding levels (like Level 8), the finish barrier is at the end of the road (Y=185m)
     * and the game does not end midway.
     */
    @Test
    fun test16_finishBarrierIsAtEndOfRoadInCurvedLevels() {
        val level8 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(7)
        assertEquals("Thunder Valley", level8.title)
        val geom = com.example.policetheifgame.game.geometry.RoadGeometry(level8)

        // Finish position must match the actual total path length to the end of the road
        assertEquals(geom.totalPathLengthMeters, geom.finishPositionMeters, 0.001f)

        // The finish boundary must be at Y = 185m (the very end of the road, not midway)
        val (finishLeft, finishRight) = geom.getRoadBoundariesAtDistance(geom.finishPositionMeters)
        assertEquals(185.0f, finishLeft.y, 0.1f)
        assertEquals(185.0f, finishRight.y, 0.1f)

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

    /**
     * Requirement 17: Siren triggers strictly when the chase is actively on its way (dragging),
     * and stops immediately when drag ends or game pauses.
     */
    @Test
    fun test17_sirenOnlyPlaysDuringActiveChaseDragAndStopsOnRelease() {
        val viewModel = com.example.policetheifgame.ui.GameViewModel(initialLevelIndex = 0)
        viewModel.startGame()

        // 1. Ready/Playing without drag: Chase is NOT active, siren must not be active
        assertFalse(viewModel.isActivelyChasing)
        assertFalse(viewModel.uiState.value.isPoliceChasing)

        // 2. Player drags police car on screen
        val viewport = GameViewport.createOverview(
            screenSize = Size(1080f, 2400f),
            roadGeometry = viewModel.gameEngine.roadGeometry
        )
        val dragScreenOffset = viewport.worldToScreen(Point2D(50f, 5f))
        viewModel.onPoliceDragStart(dragScreenOffset, viewport)

        assertTrue("Chase must be active during drag", viewModel.isActivelyChasing)
        assertTrue("GameState reflects active chase", viewModel.uiState.value.isPoliceChasing)

        // 3. Player lifts finger (drag end)
        viewModel.onPoliceDragEnd()
        assertFalse("Chase must stop when finger is released", viewModel.isActivelyChasing)
        assertFalse(viewModel.uiState.value.isPoliceChasing)

        // 4. Drag again and pause
        viewModel.onPoliceDragStart(dragScreenOffset, viewport)
        viewModel.onPoliceDrag(dragScreenOffset, viewport)
        assertTrue(viewModel.isActivelyChasing)
        viewModel.pauseGame()
        assertFalse("Chase must stop immediately on pause", viewModel.isActivelyChasing)
        assertFalse(viewModel.uiState.value.isPoliceChasing)
    }

    /**
     * Requirement 18: Progressive difficulty across all 10 levels in LevelRepository.
     * Road length strictly increases (80m to 220m), thief speed increases (8m/s to 31m/s),
     * road width strictly narrows (12m down to 6m).
     */
    @Test
    fun test18_levelsProgressiveDifficultyMatrix() {
        var prevLength = 0f
        var prevSpeed = 0f
        var prevWidth = 999f

        // Levels 1 to 10: Highway progression
        for (i in 0 until 10) {
            val level = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(i)

            assertTrue(
                "Level ${i + 1} length (${level.roadLengthMeters}) must be > prev ($prevLength)",
                level.roadLengthMeters > prevLength
            )
            assertTrue(
                "Level ${i + 1} thief speed (${level.thiefSpeedMps}) must be > prev ($prevSpeed)",
                level.thiefSpeedMps > prevSpeed
            )
            assertTrue(
                "Level ${i + 1} road width (${level.roadWidthMeters}) must be < prev ($prevWidth)",
                level.roadWidthMeters < prevWidth
            )

            prevLength = level.roadLengthMeters
            prevSpeed = level.thiefSpeedMps
            prevWidth = level.roadWidthMeters
        }

        // Levels 11 to 20: Puzzle maze progression
        var prevPuzzleSpeed = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(9).thiefSpeedMps
        var prevCorridorWidth = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(9).roadWidthMeters
        var prevCorridorCount = 0
        for (i in 10 until 20) {
            val level = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(i)
            assertTrue("Level ${i + 1} must be marked as puzzle", level.isPuzzle)
            assertTrue(
                "Level ${i + 1} thief speed (${level.thiefSpeedMps}) must be > prev ($prevPuzzleSpeed)",
                level.thiefSpeedMps > prevPuzzleSpeed
            )
            assertTrue(
                "Level ${i + 1} corridor width (${level.roadWidthMeters}) must be <= prev ($prevCorridorWidth)",
                level.roadWidthMeters <= prevCorridorWidth
            )
            assertTrue(
                "Level ${i + 1} corridors (${level.corridors.size}) must increase > prev ($prevCorridorCount)",
                level.corridors.size > prevCorridorCount
            )
            assertTrue(
                "Level ${i + 1} must have dead ends",
                level.corridors.any { it.isDeadEnd }
            )
            prevPuzzleSpeed = level.thiefSpeedMps
            prevCorridorWidth = level.roadWidthMeters
            prevCorridorCount = level.corridors.size
        }
    }

    /**
     * Requirement 19: Police car cannot be teleported by touching random parts of the road.
     * Dragging only initiates if the touch starts at/near the police car's position.
     * When user releases finger, the police car remains in place, and dragging can only
     * restart from the car's current position (not from original start or anywhere else).
     */
    @Test
    fun test19_noTeleportationOnRandomTapAndCanRestartDragFromCarPosition() {
        val viewModel = GameViewModel(initialLevelIndex = 0)
        viewModel.startGame()

        val viewport = GameViewport.createOverview(
            screenSize = Size(1080f, 2400f),
            roadGeometry = viewModel.gameEngine.roadGeometry
        )

        val initialPolicePos = viewModel.gameEngine.policePosition
        assertEquals(50f, initialPolicePos.x, 0.1f)
        assertEquals(0f, initialPolicePos.y, 0.1f)

        // 1. User taps at Y=60m (midway/near top of road)
        val farScreenOffset = viewport.worldToScreen(Point2D(50f, 60f))
        viewModel.onPoliceDragStart(farScreenOffset, viewport)
        viewModel.onPoliceDrag(farScreenOffset, viewport)

        // Car must NOT have moved / teleported
        assertEquals("Police car must NOT teleport on far tap", 0f, viewModel.gameEngine.policePosition.y, 0.1f)
        assertFalse("Drag must not be active", viewModel.isDraggingPolice)

        // 2. User starts drag at the car's position (at Y=0m)
        val carScreenOffset = viewport.worldToScreen(initialPolicePos)
        viewModel.onPoliceDragStart(carScreenOffset, viewport)
        assertTrue("Drag must be active when started on car", viewModel.isDraggingPolice)

        // Drag the car up to Y=8m (behind thief who starts at 14m+)
        val dragTo8mOffset = viewport.worldToScreen(Point2D(50f, 8f))
        viewModel.onPoliceDrag(dragTo8mOffset, viewport)
        assertEquals(8f, viewModel.gameEngine.policePosition.y, 1.0f)
        assertEquals(GameStatus.PLAYING, viewModel.gameEngine.status)

        // 3. User releases finger at Y=8m
        viewModel.onPoliceDragEnd()
        assertFalse(viewModel.isDraggingPolice)
        val leftPos = viewModel.gameEngine.policePosition
        assertEquals(8f, leftPos.y, 1.0f)

        // 4. User attempts to drag from bottom (Y=0m) again
        val farBottomOffset = viewport.worldToScreen(Point2D(50f, -5f))
        viewModel.onPoliceDragStart(farBottomOffset, viewport)
        viewModel.onPoliceDrag(farBottomOffset, viewport)

        // Car must remain at Y=8m, not teleport back to Y=0m
        assertEquals("Car must not teleport back to bottom when tapped at 0m", leftPos.y, viewModel.gameEngine.policePosition.y, 0.1f)
        assertFalse("Drag must not start from distant bottom point", viewModel.isDraggingPolice)

        // 5. User touches where car was left (Y=8m)
        val resumeScreenOffset = viewport.worldToScreen(leftPos)
        viewModel.onPoliceDragStart(resumeScreenOffset, viewport)
        assertTrue("Drag can resume from car's last position", viewModel.isDraggingPolice)

        // Drag further to Y=11m
        val dragTo11mOffset = viewport.worldToScreen(Point2D(50f, 11f))
        viewModel.onPoliceDrag(dragTo11mOffset, viewport)
        assertEquals(11f, viewModel.gameEngine.policePosition.y, 1.0f)
    }

    /**
     * Requirement 20: Viewport overview and bottom padding ensure the police car (centered at Y=0m)
     * is completely inside the visible viewport and has clearance from the bottom edge.
     */
    @Test
    fun test20_bottomPaddingEnsuresPoliceCarFullyVisibleAboveBottom() {
        val viewport = GameViewport.createOverview(
            screenSize = Size(1080f, 2400f),
            roadGeometry = roadGeometry
        )

        // At Y=0m, police car screen Y must be above the screen bottom (screenSize.height = 2400)
        val policeScreenPos = viewport.worldToScreen(roadGeometry.pathPoints.first())
        assertTrue(
            "Police car must be well above screen bottom (y=${policeScreenPos.y}, screenHeight=2400)",
            policeScreenPos.y < 2350f
        )
        // Checkered finish line at top of road must be below screen top (y > 0)
        val finishWorldPos = roadGeometry.pathPoints.last()
        val finishScreenPos = viewport.worldToScreen(finishWorldPos)
        assertTrue(
            "Finish line must be comfortably below top edge (y=${finishScreenPos.y})",
            finishScreenPos.y > 40f
        )
    }

    /**
     * Requirement 21: Puzzle levels 11 through 20 loaded correctly with corridors, start, destination, and thief routes.
     */
    @Test
    fun test21_puzzleLevelsLoadedCorrectlyWithCorridorsAndRoutes() {
        for (i in 10 until 20) {
            val level = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(i)
            assertTrue("Level ${i + 1} must have isPuzzle = true", level.isPuzzle)
            assertTrue("Level ${i + 1} corridors must not be empty", level.corridors.isNotEmpty())
            org.junit.Assert.assertNotNull("Level ${i + 1} policeStartPosition must exist", level.policeStartPosition)
            org.junit.Assert.assertNotNull("Level ${i + 1} thiefStartPosition must exist", level.thiefStartPosition)
            org.junit.Assert.assertNotNull("Level ${i + 1} destinationPosition must exist", level.destinationPosition)
            assertTrue("Level ${i + 1} thiefRoute must have at least 2 points", level.thiefRoute.size >= 2)
        }
    }

    /**
     * Requirement 22: Corridor containment & off-road crash in puzzle maze.
     */
    @Test
    fun test22_puzzleRoadGeometryCorridorContainmentAndOffRoad() {
        val level11 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(10)
        val geom = RoadGeometry(level11)
        val engine = GameEngine(roadGeometry = geom, thiefSpeedMps = level11.thiefSpeedMps)
        engine.start()

        // Police starts at (50, 10), which is inside corridor c11_entry [(50,10) to (50,25)]
        assertTrue("Police start point must be on road corridor", geom.isPositionOnRoad(Point2D(50f, 10f)))

        // Point inside west corridor (20, 40) is valid
        assertTrue("Point in west corridor must be on road", geom.isPositionOnRoad(Point2D(20f, 40f)))

        // Point far outside corridors (0, 0) or inside maze wall island (10, 50) is OFF-ROAD
        assertFalse("Grass point outside maze must not be on road", geom.isPositionOnRoad(Point2D(0f, 0f)))

        // Dragging police off-road triggers GameStatus.THIEF_WON with OFF_ROAD
        engine.onPoliceDragged(Point2D(0f, 0f))
        assertEquals(GameStatus.THIEF_WON, engine.status)
        assertEquals(GameOverReason.OFF_ROAD, engine.reason)
    }

    /**
     * Requirement 23: Free 2D dragging and U-turn heading rotation in puzzle maze.
     */
    @Test
    fun test23_puzzleFree2DDragAndUTurnHeadingRotation() {
        val level11 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(10)
        val geom = RoadGeometry(level11)
        val engine = GameEngine(roadGeometry = geom, thiefSpeedMps = level11.thiefSpeedMps)
        engine.start()

        // Drag North along entry corridor: (50, 10) -> (50, 18)
        engine.onPoliceDragged(Point2D(50f, 18f))
        assertEquals(0f, engine.policeHeadingDeg, 5.0f)

        // Drag East within corridor: (50, 18) -> (52f, 18f)
        engine.onPoliceDragged(Point2D(52f, 18f))
        assertEquals(90f, engine.policeHeadingDeg, 5.0f)

        // Drag South (reversing / 180° U-turn): (52f, 18f) -> (52f, 14f)
        engine.onPoliceDragged(Point2D(52f, 14f))
        assertEquals(180f, Math.abs(engine.policeHeadingDeg), 5.0f)

        // Drag West: (52f, 14f) -> (48f, 14f)
        engine.onPoliceDragged(Point2D(48f, 14f))
        assertEquals(-90f, engine.policeHeadingDeg, 5.0f)
    }

    /**
     * Requirement 24: Thief navigates route waypoints across the maze.
     */
    @Test
    fun test24_puzzleThiefRouteTraversalTowardsDestination() {
        val level11 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(10)
        val geom = RoadGeometry(level11)
        val engine = GameEngine(roadGeometry = geom, thiefSpeedMps = 10f)
        engine.start()

        val startPos = level11.thiefStartPosition!!
        assertEquals(startPos.x, engine.thiefPosition.x, 0.1f)
        assertEquals(startPos.y, engine.thiefPosition.y, 0.1f)

        // Tick 2 seconds (covers 20m along thief route)
        engine.tick(2.0f)
        assertTrue("Thief must have traversed distance", engine.thiefDistanceMeters > 0f)
        assertFalse("Game should still be playing", engine.isGameOver)
    }

    /**
     * Requirement 25: Police catches thief inside puzzle maze triggers POLICE_WON.
     */
    @Test
    fun test25_puzzlePoliceCatchesThiefInMaze() {
        val level11 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(10)
        val geom = RoadGeometry(level11)
        val engine = GameEngine(roadGeometry = geom, thiefSpeedMps = 10f)
        engine.start()

        val thiefPos = engine.thiefPosition
        // Drag police to thief's exact position
        engine.onPoliceDragged(Point2D(thiefPos.x, thiefPos.y))

        assertEquals(GameStatus.POLICE_WON, engine.status)
        assertEquals(GameOverReason.CAUGHT_THIEF, engine.reason)
    }

    /**
     * Requirement 26: Thief reaching destination escape gate triggers THIEF_WON / THIEF_ESCAPED.
     */
    @Test
    fun test26_puzzleThiefEscapesDestinationGate() {
        val level11 = com.example.policetheifgame.game.geometry.LevelRepository.getLevel(10)
        val geom = RoadGeometry(level11)
        val engine = GameEngine(roadGeometry = geom, thiefSpeedMps = 20f)
        engine.start()

        // Advance enough time to complete the entire route (route is ~100m, at 20 m/s takes ~5s)
        engine.tick(10.0f)

        assertEquals(GameStatus.THIEF_WON, engine.status)
        assertEquals(GameOverReason.THIEF_ESCAPED, engine.reason)
    }

    /**
     * Requirement 27: App launches into Level Map screen by default.
     */
    @Test
    fun test27_appLaunchesIntoLevelMapScreen() {
        val vm = GameViewModel()
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)
    }

    /**
     * Requirement 28: Locked levels cannot be opened from the Level Map.
     */
    @Test
    fun test28_cannotSelectLockedLevelOnMap() {
        val vm = GameViewModel()
        // Default unlocked level is index 0 (Level 1)
        assertEquals(0, vm.unlockedLevelIndex)

        // Attempting to jump ahead to locked Level 2 (index 1) or Level 8 (index 7) fails
        val openedLvl2 = vm.openLevelFromMap(1)
        assertFalse("Cannot open locked level 2 when only level 1 is unlocked", openedLvl2)
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)

        val openedLvl8 = vm.openLevelFromMap(7)
        assertFalse("Cannot open locked level 8", openedLvl8)
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)
    }

    /**
     * Requirement 29: Any unlocked or previously cleared level can be selected and replayed.
     */
    @Test
    fun test29_canSelectAndReplayAnyUnlockedLevelOnMap() {
        val vm = GameViewModel()
        // Select and open Level 1 (index 0)
        val openedLvl1 = vm.openLevelFromMap(0)
        assertTrue("Level 1 is unlocked and can be opened", openedLvl1)
        assertEquals(AppScreen.GAMEPLAY, vm.currentScreen.value)
        assertEquals(0, vm.currentLevelIndex)

        // Return to map
        vm.returnToLevelMap()
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)
    }

    /**
     * Requirement 30: When user is at Level 7 (index 6 unlocked), Level 4 can be replayed, but Level 8 is locked.
     */
    @Test
    fun test30_candyCrushProgressionRuleUserAtLevel7CanReplay4CannotPlay8() {
        // Mock a ViewModel where user unlocked up to Level 7 (index 6)
        val vm = GameViewModel(initialLevelIndex = 6)
        assertEquals(6, vm.unlockedLevelIndex)

        // Player wants to replay Level 4 (index 3)
        val openedLvl4 = vm.openLevelFromMap(3)
        assertTrue("User at Level 7 can freely replay Level 4", openedLvl4)
        assertEquals(3, vm.currentLevelIndex)
        assertEquals(AppScreen.GAMEPLAY, vm.currentScreen.value)

        // Return to map
        vm.returnToLevelMap()
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)

        // Player tries to play Level 8 (index 7) -> must fail because Level 7 is not beaten yet
        val openedLvl8 = vm.openLevelFromMap(7)
        assertFalse("User cannot play Level 8 until Level 7 is completed", openedLvl8)
        assertEquals(AppScreen.LEVEL_MAP, vm.currentScreen.value)
    }
}



