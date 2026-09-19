package com.example.policetheifgame.game.geometry

import android.content.Context
import com.example.policetheifgame.game.model.Point2D

/**
 * Repository providing the collection of playable game levels.
 * Speeds scale up clearly with each level, road widths cover the screen,
 * and the 100m chase distance is fully utilized.
 */
object LevelRepository {

    /**
     * Built-in definitions of all 10 levels, ensuring deterministic operation
     * in both Android runtime and JVM unit tests.
     */
    private val builtInLevels: List<LevelData> = listOf(
        // Level 1: Sunny Highway (9.0 m/s)
        LevelData.createDefaultLevel1(),

        // Level 2: Winding Canyon (95m, 11.2m width, 10.5 m/s)
        LevelData(
            levelId = "level_2",
            title = "Winding Canyon",
            roadLengthMeters = 95.0f,
            roadWidthMeters = 11.2f,
            thiefSpeedMps = 10.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 95.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(44.0f, 15.0f),
                Point2D(38.0f, 30.0f),
                Point2D(46.0f, 45.0f),
                Point2D(58.0f, 60.0f),
                Point2D(64.0f, 75.0f),
                Point2D(56.0f, 85.0f),
                Point2D(50.0f, 95.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.4f, 0.0f), Point2D(55.6f, 0.0f)),
                RoadBoundary(Point2D(38.4f, 15.0f), Point2D(49.6f, 15.0f)),
                RoadBoundary(Point2D(32.4f, 30.0f), Point2D(43.6f, 30.0f)),
                RoadBoundary(Point2D(40.4f, 45.0f), Point2D(51.6f, 45.0f)),
                RoadBoundary(Point2D(52.4f, 60.0f), Point2D(63.6f, 60.0f)),
                RoadBoundary(Point2D(58.4f, 75.0f), Point2D(69.6f, 75.0f)),
                RoadBoundary(Point2D(50.4f, 85.0f), Point2D(61.6f, 85.0f)),
                RoadBoundary(Point2D(44.4f, 95.0f), Point2D(55.6f, 95.0f))
            )
        ),

        // Level 3: Hairpin Ridge (110m, 10.4m width, 13.0 m/s)
        LevelData(
            levelId = "level_3",
            title = "Hairpin Ridge",
            roadLengthMeters = 110.0f,
            roadWidthMeters = 10.4f,
            thiefSpeedMps = 13.0f,
            initialGapMeters = 18.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 110.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(52.0f, 14.0f),
                Point2D(34.0f, 28.0f),
                Point2D(32.0f, 42.0f),
                Point2D(66.0f, 58.0f),
                Point2D(68.0f, 72.0f),
                Point2D(36.0f, 88.0f),
                Point2D(46.0f, 100.0f),
                Point2D(50.0f, 110.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.8f, 0.0f), Point2D(55.2f, 0.0f)),
                RoadBoundary(Point2D(46.8f, 14.0f), Point2D(57.2f, 14.0f)),
                RoadBoundary(Point2D(28.8f, 28.0f), Point2D(39.2f, 28.0f)),
                RoadBoundary(Point2D(26.8f, 42.0f), Point2D(37.2f, 42.0f)),
                RoadBoundary(Point2D(60.8f, 58.0f), Point2D(71.2f, 58.0f)),
                RoadBoundary(Point2D(62.8f, 72.0f), Point2D(73.2f, 72.0f)),
                RoadBoundary(Point2D(30.8f, 88.0f), Point2D(41.2f, 88.0f)),
                RoadBoundary(Point2D(40.8f, 100.0f), Point2D(51.2f, 100.0f)),
                RoadBoundary(Point2D(44.8f, 110.0f), Point2D(55.2f, 110.0f))
            )
        ),

        // Level 4: Mountain Pass (125m, 9.6m width, 15.5 m/s)
        LevelData(
            levelId = "level_4",
            title = "Mountain Pass",
            roadLengthMeters = 125.0f,
            roadWidthMeters = 9.6f,
            thiefSpeedMps = 15.5f,
            initialGapMeters = 20.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 125.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(62.0f, 15.0f),
                Point2D(70.0f, 32.0f),
                Point2D(50.0f, 50.0f),
                Point2D(32.0f, 68.0f),
                Point2D(40.0f, 84.0f),
                Point2D(66.0f, 100.0f),
                Point2D(58.0f, 114.0f),
                Point2D(50.0f, 125.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.2f, 0.0f), Point2D(54.8f, 0.0f)),
                RoadBoundary(Point2D(57.2f, 15.0f), Point2D(66.8f, 15.0f)),
                RoadBoundary(Point2D(65.2f, 32.0f), Point2D(74.8f, 32.0f)),
                RoadBoundary(Point2D(45.2f, 50.0f), Point2D(54.8f, 50.0f)),
                RoadBoundary(Point2D(27.2f, 68.0f), Point2D(36.8f, 68.0f)),
                RoadBoundary(Point2D(35.2f, 84.0f), Point2D(44.8f, 84.0f)),
                RoadBoundary(Point2D(61.2f, 100.0f), Point2D(70.8f, 100.0f)),
                RoadBoundary(Point2D(53.2f, 114.0f), Point2D(62.8f, 114.0f)),
                RoadBoundary(Point2D(45.2f, 125.0f), Point2D(54.8f, 125.0f))
            )
        ),

        // Level 5: Coastal Serpent (140m, 9.0m width, 18.0 m/s)
        LevelData(
            levelId = "level_5",
            title = "Coastal Serpent",
            roadLengthMeters = 140.0f,
            roadWidthMeters = 9.0f,
            thiefSpeedMps = 18.0f,
            initialGapMeters = 22.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 140.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(36.0f, 14.0f),
                Point2D(34.0f, 28.0f),
                Point2D(54.0f, 44.0f),
                Point2D(68.0f, 58.0f),
                Point2D(44.0f, 74.0f),
                Point2D(32.0f, 90.0f),
                Point2D(62.0f, 106.0f),
                Point2D(66.0f, 118.0f),
                Point2D(56.0f, 130.0f),
                Point2D(50.0f, 140.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.5f, 0.0f), Point2D(54.5f, 0.0f)),
                RoadBoundary(Point2D(31.5f, 14.0f), Point2D(40.5f, 14.0f)),
                RoadBoundary(Point2D(29.5f, 28.0f), Point2D(38.5f, 28.0f)),
                RoadBoundary(Point2D(49.5f, 44.0f), Point2D(58.5f, 44.0f)),
                RoadBoundary(Point2D(63.5f, 58.0f), Point2D(72.5f, 58.0f)),
                RoadBoundary(Point2D(39.5f, 74.0f), Point2D(48.5f, 74.0f)),
                RoadBoundary(Point2D(27.5f, 90.0f), Point2D(36.5f, 90.0f)),
                RoadBoundary(Point2D(57.5f, 106.0f), Point2D(66.5f, 106.0f)),
                RoadBoundary(Point2D(61.5f, 118.0f), Point2D(70.5f, 118.0f)),
                RoadBoundary(Point2D(51.5f, 130.0f), Point2D(60.5f, 130.0f)),
                RoadBoundary(Point2D(45.5f, 140.0f), Point2D(54.5f, 140.0f))
            )
        ),

        // Level 6: Midnight Expressway (155m, 8.4m width, 20.5 m/s)
        LevelData(
            levelId = "level_6",
            title = "Midnight Expressway",
            roadLengthMeters = 155.0f,
            roadWidthMeters = 8.4f,
            thiefSpeedMps = 20.5f,
            initialGapMeters = 24.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 155.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(65.0f, 14.0f),
                Point2D(68.0f, 28.0f),
                Point2D(48.0f, 42.0f),
                Point2D(30.0f, 56.0f),
                Point2D(36.0f, 70.0f),
                Point2D(64.0f, 84.0f),
                Point2D(66.0f, 98.0f),
                Point2D(38.0f, 114.0f),
                Point2D(32.0f, 128.0f),
                Point2D(58.0f, 142.0f),
                Point2D(50.0f, 155.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.8f, 0.0f), Point2D(54.2f, 0.0f)),
                RoadBoundary(Point2D(60.8f, 14.0f), Point2D(69.2f, 14.0f)),
                RoadBoundary(Point2D(63.8f, 28.0f), Point2D(72.2f, 28.0f)),
                RoadBoundary(Point2D(43.8f, 42.0f), Point2D(52.2f, 42.0f)),
                RoadBoundary(Point2D(25.8f, 56.0f), Point2D(34.2f, 56.0f)),
                RoadBoundary(Point2D(31.8f, 70.0f), Point2D(40.2f, 70.0f)),
                RoadBoundary(Point2D(59.8f, 84.0f), Point2D(68.2f, 84.0f)),
                RoadBoundary(Point2D(61.8f, 98.0f), Point2D(70.2f, 98.0f)),
                RoadBoundary(Point2D(33.8f, 114.0f), Point2D(42.2f, 114.0f)),
                RoadBoundary(Point2D(27.8f, 128.0f), Point2D(36.2f, 128.0f)),
                RoadBoundary(Point2D(53.8f, 142.0f), Point2D(62.2f, 142.0f)),
                RoadBoundary(Point2D(45.8f, 155.0f), Point2D(54.2f, 155.0f))
            )
        ),

        // Level 7: Neon Metropolis (170m, 7.8m width, 23.0 m/s)
        LevelData(
            levelId = "level_7",
            title = "Neon Metropolis",
            roadLengthMeters = 170.0f,
            roadWidthMeters = 7.8f,
            thiefSpeedMps = 23.0f,
            initialGapMeters = 26.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 170.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(38.0f, 14.0f),
                Point2D(32.0f, 28.0f),
                Point2D(56.0f, 44.0f),
                Point2D(70.0f, 60.0f),
                Point2D(42.0f, 76.0f),
                Point2D(30.0f, 92.0f),
                Point2D(60.0f, 108.0f),
                Point2D(70.0f, 124.0f),
                Point2D(35.0f, 140.0f),
                Point2D(40.0f, 152.0f),
                Point2D(56.0f, 162.0f),
                Point2D(50.0f, 170.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(46.1f, 0.0f), Point2D(53.9f, 0.0f)),
                RoadBoundary(Point2D(34.1f, 14.0f), Point2D(41.9f, 14.0f)),
                RoadBoundary(Point2D(28.1f, 28.0f), Point2D(35.9f, 28.0f)),
                RoadBoundary(Point2D(52.1f, 44.0f), Point2D(59.9f, 44.0f)),
                RoadBoundary(Point2D(66.1f, 60.0f), Point2D(73.9f, 60.0f)),
                RoadBoundary(Point2D(38.1f, 76.0f), Point2D(45.9f, 76.0f)),
                RoadBoundary(Point2D(26.1f, 92.0f), Point2D(33.9f, 92.0f)),
                RoadBoundary(Point2D(56.1f, 108.0f), Point2D(63.9f, 108.0f)),
                RoadBoundary(Point2D(66.1f, 124.0f), Point2D(73.9f, 124.0f)),
                RoadBoundary(Point2D(31.1f, 140.0f), Point2D(38.9f, 140.0f)),
                RoadBoundary(Point2D(36.1f, 152.0f), Point2D(43.9f, 152.0f)),
                RoadBoundary(Point2D(52.1f, 162.0f), Point2D(59.9f, 162.0f)),
                RoadBoundary(Point2D(46.1f, 170.0f), Point2D(53.9f, 170.0f))
            )
        ),

        // Level 8: Thunder Valley (185m, 7.2m width, 25.5 m/s)
        LevelData(
            levelId = "level_8",
            title = "Thunder Valley",
            roadLengthMeters = 185.0f,
            roadWidthMeters = 7.2f,
            thiefSpeedMps = 25.5f,
            initialGapMeters = 28.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 185.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(68.0f, 15.0f),
                Point2D(70.0f, 30.0f),
                Point2D(34.0f, 48.0f),
                Point2D(30.0f, 64.0f),
                Point2D(66.0f, 80.0f),
                Point2D(70.0f, 96.0f),
                Point2D(32.0f, 114.0f),
                Point2D(30.0f, 130.0f),
                Point2D(65.0f, 146.0f),
                Point2D(68.0f, 160.0f),
                Point2D(38.0f, 174.0f),
                Point2D(50.0f, 185.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(46.4f, 0.0f), Point2D(53.6f, 0.0f)),
                RoadBoundary(Point2D(64.4f, 15.0f), Point2D(71.6f, 15.0f)),
                RoadBoundary(Point2D(66.4f, 30.0f), Point2D(73.6f, 30.0f)),
                RoadBoundary(Point2D(30.4f, 48.0f), Point2D(37.6f, 48.0f)),
                RoadBoundary(Point2D(26.4f, 64.0f), Point2D(33.6f, 64.0f)),
                RoadBoundary(Point2D(62.4f, 80.0f), Point2D(69.6f, 80.0f)),
                RoadBoundary(Point2D(66.4f, 96.0f), Point2D(73.6f, 96.0f)),
                RoadBoundary(Point2D(28.4f, 114.0f), Point2D(35.6f, 114.0f)),
                RoadBoundary(Point2D(26.4f, 130.0f), Point2D(33.6f, 130.0f)),
                RoadBoundary(Point2D(61.4f, 146.0f), Point2D(68.6f, 146.0f)),
                RoadBoundary(Point2D(64.4f, 160.0f), Point2D(71.6f, 160.0f)),
                RoadBoundary(Point2D(34.4f, 174.0f), Point2D(41.6f, 174.0f)),
                RoadBoundary(Point2D(46.4f, 185.0f), Point2D(53.6f, 185.0f))
            )
        ),

        // Level 9: Inferno Ridge (200m, 6.6m width, 28.0 m/s)
        LevelData(
            levelId = "level_9",
            title = "Inferno Ridge",
            roadLengthMeters = 200.0f,
            roadWidthMeters = 6.6f,
            thiefSpeedMps = 28.0f,
            initialGapMeters = 30.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 200.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(30.0f, 14.0f),
                Point2D(28.0f, 28.0f),
                Point2D(70.0f, 44.0f),
                Point2D(72.0f, 58.0f),
                Point2D(26.0f, 74.0f),
                Point2D(24.0f, 88.0f),
                Point2D(72.0f, 104.0f),
                Point2D(74.0f, 118.0f),
                Point2D(30.0f, 134.0f),
                Point2D(28.0f, 148.0f),
                Point2D(68.0f, 164.0f),
                Point2D(66.0f, 178.0f),
                Point2D(38.0f, 190.0f),
                Point2D(50.0f, 200.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(46.7f, 0.0f), Point2D(53.3f, 0.0f)),
                RoadBoundary(Point2D(26.7f, 14.0f), Point2D(33.3f, 14.0f)),
                RoadBoundary(Point2D(24.7f, 28.0f), Point2D(31.3f, 28.0f)),
                RoadBoundary(Point2D(66.7f, 44.0f), Point2D(73.3f, 44.0f)),
                RoadBoundary(Point2D(68.7f, 58.0f), Point2D(75.3f, 58.0f)),
                RoadBoundary(Point2D(22.7f, 74.0f), Point2D(29.3f, 74.0f)),
                RoadBoundary(Point2D(20.7f, 88.0f), Point2D(27.3f, 88.0f)),
                RoadBoundary(Point2D(68.7f, 104.0f), Point2D(75.3f, 104.0f)),
                RoadBoundary(Point2D(70.7f, 118.0f), Point2D(77.3f, 118.0f)),
                RoadBoundary(Point2D(26.7f, 134.0f), Point2D(33.3f, 134.0f)),
                RoadBoundary(Point2D(24.7f, 148.0f), Point2D(31.3f, 148.0f)),
                RoadBoundary(Point2D(64.7f, 164.0f), Point2D(71.3f, 164.0f)),
                RoadBoundary(Point2D(62.7f, 178.0f), Point2D(69.3f, 178.0f)),
                RoadBoundary(Point2D(34.7f, 190.0f), Point2D(41.3f, 190.0f)),
                RoadBoundary(Point2D(46.7f, 200.0f), Point2D(53.3f, 200.0f))
            )
        ),

        // Level 10: The Final Gauntlet (220m, 6.0m width, 31.0 m/s)
        LevelData(
            levelId = "level_10",
            title = "The Final Gauntlet",
            roadLengthMeters = 220.0f,
            roadWidthMeters = 6.0f,
            thiefSpeedMps = 31.0f,
            initialGapMeters = 32.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 220.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(70.0f, 14.0f),
                Point2D(74.0f, 28.0f),
                Point2D(26.0f, 44.0f),
                Point2D(22.0f, 58.0f),
                Point2D(74.0f, 74.0f),
                Point2D(78.0f, 90.0f),
                Point2D(24.0f, 106.0f),
                Point2D(20.0f, 122.0f),
                Point2D(76.0f, 138.0f),
                Point2D(78.0f, 154.0f),
                Point2D(24.0f, 170.0f),
                Point2D(26.0f, 184.0f),
                Point2D(72.0f, 198.0f),
                Point2D(64.0f, 210.0f),
                Point2D(50.0f, 220.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(47.0f, 0.0f), Point2D(53.0f, 0.0f)),
                RoadBoundary(Point2D(67.0f, 14.0f), Point2D(73.0f, 14.0f)),
                RoadBoundary(Point2D(71.0f, 28.0f), Point2D(77.0f, 28.0f)),
                RoadBoundary(Point2D(23.0f, 44.0f), Point2D(29.0f, 44.0f)),
                RoadBoundary(Point2D(19.0f, 58.0f), Point2D(25.0f, 58.0f)),
                RoadBoundary(Point2D(71.0f, 74.0f), Point2D(77.0f, 74.0f)),
                RoadBoundary(Point2D(75.0f, 90.0f), Point2D(81.0f, 90.0f)),
                RoadBoundary(Point2D(21.0f, 106.0f), Point2D(27.0f, 106.0f)),
                RoadBoundary(Point2D(17.0f, 122.0f), Point2D(23.0f, 122.0f)),
                RoadBoundary(Point2D(73.0f, 138.0f), Point2D(79.0f, 138.0f)),
                RoadBoundary(Point2D(75.0f, 154.0f), Point2D(81.0f, 154.0f)),
                RoadBoundary(Point2D(21.0f, 170.0f), Point2D(27.0f, 170.0f)),
                RoadBoundary(Point2D(23.0f, 184.0f), Point2D(29.0f, 184.0f)),
                RoadBoundary(Point2D(69.0f, 198.0f), Point2D(75.0f, 198.0f)),
                RoadBoundary(Point2D(61.0f, 210.0f), Point2D(67.0f, 210.0f)),
                RoadBoundary(Point2D(47.0f, 220.0f), Point2D(53.0f, 220.0f))
            )
        ),

        // Level 11: The Clover Fork (Pac-Man Intro Maze, 10.0m width, 12.0 m/s)
        LevelData(
            levelId = "level_11",
            title = "The Clover Fork",
            roadLengthMeters = 95.0f,
            roadWidthMeters = 10.0f,
            thiefSpeedMps = 12.0f,
            initialGapMeters = 20.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 95.0f,
            roadPath = listOf(Point2D(50f, 10f), Point2D(50f, 35f), Point2D(70f, 35f), Point2D(70f, 75f), Point2D(50f, 75f), Point2D(50f, 95f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_south", listOf(Point2D(50f, 10f), Point2D(50f, 35f)), 10.0f),
                RoadCorridor("c_left_loop", listOf(Point2D(50f, 35f), Point2D(30f, 35f), Point2D(30f, 75f), Point2D(50f, 75f)), 10.0f),
                RoadCorridor("c_right_loop", listOf(Point2D(50f, 35f), Point2D(70f, 35f), Point2D(70f, 75f), Point2D(50f, 75f)), 10.0f),
                RoadCorridor("c_crosswalk", listOf(Point2D(30f, 55f), Point2D(70f, 55f)), 10.0f),
                RoadCorridor("c_north_exit", listOf(Point2D(50f, 75f), Point2D(50f, 95f)), 10.0f)
            ),
            policeStartPosition = Point2D(50f, 12f),
            thiefStartPosition = Point2D(50f, 35f),
            destinationPosition = Point2D(50f, 95f),
            thiefRoute = listOf(Point2D(50f, 35f), Point2D(70f, 35f), Point2D(70f, 75f), Point2D(50f, 75f), Point2D(50f, 95f))
        ),

        // Level 12: Cul-De-Sac Trap (Dead Ends & U-Turns, 9.5m width, 14.5 m/s)
        LevelData(
            levelId = "level_12",
            title = "Cul-De-Sac Trap",
            roadLengthMeters = 105.0f,
            roadWidthMeters = 9.5f,
            thiefSpeedMps = 14.5f,
            initialGapMeters = 20.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 105.0f,
            roadPath = listOf(Point2D(50f, 10f), Point2D(50f, 30f), Point2D(75f, 30f), Point2D(75f, 90f), Point2D(60f, 105f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_entry", listOf(Point2D(50f, 10f), Point2D(50f, 30f)), 9.5f),
                RoadCorridor("c_south_crossway", listOf(Point2D(25f, 30f), Point2D(75f, 30f)), 9.5f),
                RoadCorridor("c_dead_end", listOf(Point2D(25f, 30f), Point2D(25f, 75f)), 9.5f, isDeadEnd = true),
                RoadCorridor("c_center_avenue", listOf(Point2D(50f, 30f), Point2D(50f, 90f)), 9.5f),
                RoadCorridor("c_right_avenue", listOf(Point2D(75f, 30f), Point2D(75f, 90f)), 9.5f),
                RoadCorridor("c_north_exit", listOf(Point2D(50f, 90f), Point2D(75f, 90f), Point2D(60f, 105f)), 9.5f)
            ),
            policeStartPosition = Point2D(50f, 12f),
            thiefStartPosition = Point2D(50f, 30f),
            destinationPosition = Point2D(60f, 105f),
            thiefRoute = listOf(Point2D(50f, 30f), Point2D(75f, 30f), Point2D(75f, 90f), Point2D(60f, 105f))
        ),

        // Level 13: Twin Island Crossing (Ghost House Style, 9.0m width, 17.0 m/s)
        LevelData(
            levelId = "level_13",
            title = "Twin Island Crossing",
            roadLengthMeters = 110.0f,
            roadWidthMeters = 9.0f,
            thiefSpeedMps = 17.0f,
            initialGapMeters = 22.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 110.0f,
            roadPath = listOf(Point2D(50f, 15f), Point2D(50f, 55f), Point2D(25f, 55f), Point2D(25f, 95f), Point2D(75f, 95f), Point2D(85f, 105f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_south_perim", listOf(Point2D(25f, 15f), Point2D(75f, 15f)), 9.0f),
                RoadCorridor("c_west_perim", listOf(Point2D(25f, 15f), Point2D(25f, 95f)), 9.0f),
                RoadCorridor("c_east_perim", listOf(Point2D(75f, 15f), Point2D(75f, 95f)), 9.0f),
                RoadCorridor("c_north_perim", listOf(Point2D(25f, 95f), Point2D(75f, 95f)), 9.0f),
                RoadCorridor("c_center_vert", listOf(Point2D(50f, 15f), Point2D(50f, 95f)), 9.0f),
                RoadCorridor("c_center_horiz", listOf(Point2D(25f, 55f), Point2D(75f, 55f)), 9.0f),
                RoadCorridor("c_exit_spur", listOf(Point2D(75f, 95f), Point2D(85f, 105f)), 9.0f)
            ),
            policeStartPosition = Point2D(25f, 20f),
            thiefStartPosition = Point2D(50f, 35f),
            destinationPosition = Point2D(85f, 105f),
            thiefRoute = listOf(Point2D(50f, 35f), Point2D(50f, 55f), Point2D(25f, 55f), Point2D(25f, 95f), Point2D(75f, 95f), Point2D(85f, 105f))
        ),

        // Level 14: City Block Grid (2x2 Blocks, 8.5m width, 19.5 m/s)
        LevelData(
            levelId = "level_14",
            title = "City Block Grid",
            roadLengthMeters = 115.0f,
            roadWidthMeters = 8.5f,
            thiefSpeedMps = 19.5f,
            initialGapMeters = 24.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 115.0f,
            roadPath = listOf(Point2D(50f, 15f), Point2D(50f, 55f), Point2D(80f, 55f), Point2D(80f, 95f), Point2D(80f, 110f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_street_south", listOf(Point2D(20f, 15f), Point2D(80f, 15f)), 8.5f),
                RoadCorridor("c_street_mid", listOf(Point2D(20f, 55f), Point2D(80f, 55f)), 8.5f),
                RoadCorridor("c_street_north", listOf(Point2D(20f, 95f), Point2D(80f, 95f)), 8.5f),
                RoadCorridor("c_ave_west", listOf(Point2D(20f, 15f), Point2D(20f, 95f)), 8.5f),
                RoadCorridor("c_ave_center", listOf(Point2D(50f, 15f), Point2D(50f, 95f)), 8.5f),
                RoadCorridor("c_ave_east", listOf(Point2D(80f, 15f), Point2D(80f, 95f)), 8.5f),
                RoadCorridor("c_exit", listOf(Point2D(80f, 95f), Point2D(80f, 110f)), 8.5f)
            ),
            policeStartPosition = Point2D(20f, 20f),
            thiefStartPosition = Point2D(50f, 30f),
            destinationPosition = Point2D(80f, 110f),
            thiefRoute = listOf(Point2D(50f, 30f), Point2D(50f, 55f), Point2D(80f, 55f), Point2D(80f, 95f), Point2D(80f, 110f))
        ),

        // Level 15: Warehouse Labyrinth (5 Corridors, 2 Traps, 8.0m width, 22.0 m/s)
        LevelData(
            levelId = "level_15",
            title = "Warehouse Labyrinth",
            roadLengthMeters = 120.0f,
            roadWidthMeters = 8.0f,
            thiefSpeedMps = 22.0f,
            initialGapMeters = 26.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 120.0f,
            roadPath = listOf(Point2D(50f, 10f), Point2D(50f, 35f), Point2D(80f, 35f), Point2D(80f, 65f), Point2D(45f, 65f), Point2D(45f, 115f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_entry", listOf(Point2D(50f, 10f), Point2D(50f, 35f)), 8.0f),
                RoadCorridor("c_cross_1", listOf(Point2D(20f, 35f), Point2D(80f, 35f)), 8.0f),
                RoadCorridor("c_trap_west", listOf(Point2D(20f, 35f), Point2D(20f, 65f)), 8.0f, isDeadEnd = true),
                RoadCorridor("c_east_zigzag", listOf(Point2D(80f, 35f), Point2D(80f, 65f), Point2D(45f, 65f), Point2D(45f, 95f)), 8.0f),
                RoadCorridor("c_center_mid", listOf(Point2D(50f, 35f), Point2D(50f, 65f)), 8.0f),
                RoadCorridor("c_cross_2", listOf(Point2D(20f, 95f), Point2D(80f, 95f)), 8.0f),
                RoadCorridor("c_trap_northwest", listOf(Point2D(20f, 95f), Point2D(20f, 75f)), 8.0f, isDeadEnd = true),
                RoadCorridor("c_north_exit", listOf(Point2D(45f, 95f), Point2D(45f, 115f)), 8.0f)
            ),
            policeStartPosition = Point2D(50f, 12f),
            thiefStartPosition = Point2D(50f, 38f),
            destinationPosition = Point2D(45f, 115f),
            thiefRoute = listOf(Point2D(50f, 38f), Point2D(80f, 35f), Point2D(80f, 65f), Point2D(45f, 65f), Point2D(45f, 95f), Point2D(45f, 115f))
        ),

        // Level 16: Metro Underground (Dual Loops & Central Spine, 7.5m width, 24.5 m/s)
        LevelData(
            levelId = "level_16",
            title = "Metro Underground",
            roadLengthMeters = 125.0f,
            roadWidthMeters = 7.5f,
            thiefSpeedMps = 24.5f,
            initialGapMeters = 28.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 125.0f,
            roadPath = listOf(Point2D(75f, 25f), Point2D(75f, 50f), Point2D(25f, 50f), Point2D(25f, 95f), Point2D(50f, 95f), Point2D(50f, 115f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_south_bottom", listOf(Point2D(25f, 15f), Point2D(75f, 15f)), 7.5f),
                RoadCorridor("c_south_west", listOf(Point2D(25f, 15f), Point2D(25f, 50f)), 7.5f),
                RoadCorridor("c_south_east", listOf(Point2D(75f, 15f), Point2D(75f, 50f)), 7.5f),
                RoadCorridor("c_mid_concourse", listOf(Point2D(25f, 50f), Point2D(75f, 50f)), 7.5f),
                RoadCorridor("c_north_west", listOf(Point2D(25f, 50f), Point2D(25f, 95f), Point2D(50f, 95f)), 7.5f),
                RoadCorridor("c_north_east", listOf(Point2D(75f, 50f), Point2D(75f, 95f), Point2D(50f, 95f)), 7.5f),
                RoadCorridor("c_express_spine", listOf(Point2D(50f, 15f), Point2D(50f, 95f)), 7.5f),
                RoadCorridor("c_surface_exit", listOf(Point2D(50f, 95f), Point2D(50f, 115f)), 7.5f)
            ),
            policeStartPosition = Point2D(25f, 20f),
            thiefStartPosition = Point2D(75f, 25f),
            destinationPosition = Point2D(50f, 115f),
            thiefRoute = listOf(Point2D(75f, 25f), Point2D(75f, 50f), Point2D(25f, 50f), Point2D(25f, 95f), Point2D(50f, 95f), Point2D(50f, 115f))
        ),

        // Level 17: Suburban Switchbacks (3 Cul-De-Sacs, 7.0m width, 27.0 m/s)
        LevelData(
            levelId = "level_17",
            title = "Suburban Switchbacks",
            roadLengthMeters = 130.0f,
            roadWidthMeters = 7.0f,
            thiefSpeedMps = 27.0f,
            initialGapMeters = 28.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 130.0f,
            roadPath = listOf(Point2D(20f, 25f), Point2D(20f, 55f), Point2D(50f, 55f), Point2D(50f, 95f), Point2D(80f, 95f), Point2D(80f, 120f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_blvd_south", listOf(Point2D(20f, 15f), Point2D(80f, 15f)), 7.0f),
                RoadCorridor("c_cul_1", listOf(Point2D(80f, 15f), Point2D(80f, 45f)), 7.0f, isDeadEnd = true),
                RoadCorridor("c_west_spine", listOf(Point2D(20f, 15f), Point2D(20f, 95f)), 7.0f),
                RoadCorridor("c_mid_street", listOf(Point2D(20f, 55f), Point2D(65f, 55f)), 7.0f),
                RoadCorridor("c_cul_2", listOf(Point2D(65f, 55f), Point2D(85f, 55f)), 7.0f, isDeadEnd = true),
                RoadCorridor("c_north_ave", listOf(Point2D(20f, 95f), Point2D(80f, 95f)), 7.0f),
                RoadCorridor("c_cul_3", listOf(Point2D(20f, 95f), Point2D(20f, 115f)), 7.0f, isDeadEnd = true),
                RoadCorridor("c_north_spine", listOf(Point2D(50f, 55f), Point2D(50f, 95f)), 7.0f),
                RoadCorridor("c_hwy_exit", listOf(Point2D(80f, 95f), Point2D(80f, 120f)), 7.0f)
            ),
            policeStartPosition = Point2D(20f, 18f),
            thiefStartPosition = Point2D(20f, 35f),
            destinationPosition = Point2D(80f, 120f),
            thiefRoute = listOf(Point2D(20f, 35f), Point2D(20f, 55f), Point2D(50f, 55f), Point2D(50f, 95f), Point2D(80f, 95f), Point2D(80f, 120f))
        ),

        // Level 18: Old Town Alleyways (Tight 6.5m Corners, 29.0 m/s)
        LevelData(
            levelId = "level_18",
            title = "Old Town Alleyways",
            roadLengthMeters = 135.0f,
            roadWidthMeters = 6.5f,
            thiefSpeedMps = 29.0f,
            initialGapMeters = 30.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 135.0f,
            roadPath = listOf(Point2D(50f, 25f), Point2D(50f, 60f), Point2D(80f, 60f), Point2D(80f, 100f), Point2D(50f, 100f), Point2D(50f, 120f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_south_promenade", listOf(Point2D(20f, 15f), Point2D(80f, 15f)), 6.5f),
                RoadCorridor("c_east_alley", listOf(Point2D(80f, 15f), Point2D(80f, 60f), Point2D(55f, 60f)), 6.5f),
                RoadCorridor("c_west_alley", listOf(Point2D(20f, 15f), Point2D(20f, 60f), Point2D(45f, 60f)), 6.5f),
                RoadCorridor("c_plaza_conn", listOf(Point2D(45f, 60f), Point2D(55f, 60f)), 6.5f),
                RoadCorridor("c_nw_chicane", listOf(Point2D(20f, 60f), Point2D(20f, 100f), Point2D(50f, 100f)), 6.5f),
                RoadCorridor("c_ne_chicane", listOf(Point2D(80f, 60f), Point2D(80f, 100f), Point2D(50f, 100f)), 6.5f),
                RoadCorridor("c_center_spine", listOf(Point2D(50f, 15f), Point2D(50f, 100f)), 6.5f),
                RoadCorridor("c_gate_exit", listOf(Point2D(50f, 100f), Point2D(50f, 120f)), 6.5f)
            ),
            policeStartPosition = Point2D(20f, 18f),
            thiefStartPosition = Point2D(50f, 25f),
            destinationPosition = Point2D(50f, 120f),
            thiefRoute = listOf(Point2D(50f, 25f), Point2D(50f, 60f), Point2D(80f, 60f), Point2D(80f, 100f), Point2D(50f, 100f), Point2D(50f, 120f))
        ),

        // Level 19: Shipping Yard Matrix (10 Corridors, 4 Loading Docks, 6.0m width, 31.0 m/s)
        LevelData(
            levelId = "level_19",
            title = "Shipping Yard Matrix",
            roadLengthMeters = 140.0f,
            roadWidthMeters = 6.0f,
            thiefSpeedMps = 31.0f,
            initialGapMeters = 30.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 140.0f,
            roadPath = listOf(Point2D(65f, 25f), Point2D(65f, 45f), Point2D(35f, 45f), Point2D(35f, 75f), Point2D(50f, 75f), Point2D(50f, 120f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_entry_dock", listOf(Point2D(15f, 15f), Point2D(85f, 15f)), 6.0f),
                RoadCorridor("c_pier_1", listOf(Point2D(15f, 15f), Point2D(15f, 45f)), 6.0f, isDeadEnd = true),
                RoadCorridor("c_pier_2", listOf(Point2D(85f, 15f), Point2D(85f, 45f)), 6.0f, isDeadEnd = true),
                RoadCorridor("c_transit_1", listOf(Point2D(35f, 15f), Point2D(35f, 95f)), 6.0f),
                RoadCorridor("c_transit_2", listOf(Point2D(65f, 15f), Point2D(65f, 95f)), 6.0f),
                RoadCorridor("c_cross_alpha", listOf(Point2D(35f, 45f), Point2D(65f, 45f)), 6.0f),
                RoadCorridor("c_cross_beta", listOf(Point2D(15f, 75f), Point2D(85f, 75f)), 6.0f),
                RoadCorridor("c_stack_west", listOf(Point2D(15f, 75f), Point2D(15f, 100f)), 6.0f, isDeadEnd = true),
                RoadCorridor("c_stack_east", listOf(Point2D(85f, 75f), Point2D(85f, 100f)), 6.0f, isDeadEnd = true),
                RoadCorridor("c_north_hwy", listOf(Point2D(50f, 75f), Point2D(50f, 120f)), 6.0f)
            ),
            policeStartPosition = Point2D(35f, 20f),
            thiefStartPosition = Point2D(65f, 25f),
            destinationPosition = Point2D(50f, 120f),
            thiefRoute = listOf(Point2D(65f, 25f), Point2D(65f, 45f), Point2D(35f, 45f), Point2D(35f, 75f), Point2D(50f, 75f), Point2D(50f, 120f))
        ),

        // Level 20: The Master Labyrinth (Full Pac-Man Style Maze, 5.8m width, 33.0 m/s)
        LevelData(
            levelId = "level_20",
            title = "The Master Labyrinth",
            roadLengthMeters = 150.0f,
            roadWidthMeters = 5.8f,
            thiefSpeedMps = 33.0f,
            initialGapMeters = 32.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 150.0f,
            roadPath = listOf(Point2D(50f, 35f), Point2D(65f, 35f), Point2D(65f, 55f), Point2D(85f, 55f), Point2D(85f, 105f), Point2D(50f, 105f), Point2D(50f, 125f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c_outer_south", listOf(Point2D(15f, 10f), Point2D(85f, 10f)), 5.8f),
                RoadCorridor("c_outer_west", listOf(Point2D(15f, 10f), Point2D(15f, 105f)), 5.8f),
                RoadCorridor("c_outer_east", listOf(Point2D(85f, 10f), Point2D(85f, 105f)), 5.8f),
                RoadCorridor("c_outer_north", listOf(Point2D(15f, 105f), Point2D(85f, 105f)), 5.8f),
                RoadCorridor("c_inner_south", listOf(Point2D(35f, 35f), Point2D(65f, 35f)), 5.8f),
                RoadCorridor("c_inner_north", listOf(Point2D(35f, 75f), Point2D(65f, 75f)), 5.8f),
                RoadCorridor("c_inner_west", listOf(Point2D(35f, 35f), Point2D(35f, 75f)), 5.8f),
                RoadCorridor("c_inner_east", listOf(Point2D(65f, 35f), Point2D(65f, 75f)), 5.8f),
                RoadCorridor("c_conn_west", listOf(Point2D(15f, 55f), Point2D(35f, 55f)), 5.8f),
                RoadCorridor("c_conn_east", listOf(Point2D(65f, 55f), Point2D(85f, 55f)), 5.8f),
                RoadCorridor("c_center_trap", listOf(Point2D(50f, 55f), Point2D(50f, 40f)), 5.8f, isDeadEnd = true),
                RoadCorridor("c_escape_chute", listOf(Point2D(50f, 75f), Point2D(50f, 125f)), 5.8f)
            ),
            policeStartPosition = Point2D(15f, 15f),
            thiefStartPosition = Point2D(50f, 35f),
            destinationPosition = Point2D(50f, 125f),
            thiefRoute = listOf(Point2D(50f, 35f), Point2D(65f, 35f), Point2D(65f, 55f), Point2D(85f, 55f), Point2D(85f, 105f), Point2D(50f, 105f), Point2D(50f, 125f))
        )
    )

    val totalLevels: Int get() = builtInLevels.size

    fun getLevel(index: Int): LevelData {
        val safeIndex = index.coerceIn(0, totalLevels - 1)
        return builtInLevels[safeIndex]
    }

    /**
     * Loads a level JSON string from app assets if available, or falls back to built-in.
     */
    fun loadFromAssets(context: Context, levelNumber: Int): LevelData {
        return try {
            val jsonString = context.assets.open("levels/level_$levelNumber.json")
                .bufferedReader()
                .use { it.readText() }
            LevelData.fromJson(jsonString)
        } catch (e: Exception) {
            getLevel(levelNumber - 1)
        }
    }
}
