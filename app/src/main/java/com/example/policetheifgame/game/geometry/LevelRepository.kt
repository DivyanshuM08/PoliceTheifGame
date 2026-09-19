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

        // Level 11: The Clover Labyrinth (12 corridors, 5.5m width, 33.0 m/s)
        LevelData(
            levelId = "level_11",
            title = "The Clover Labyrinth",
            roadLengthMeters = 120.0f,
            roadWidthMeters = 5.5f,
            thiefSpeedMps = 33.0f,
            initialGapMeters = 22.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 120.0f,
            roadPath = listOf(Point2D(80.0f, 30.0f), Point2D(80.0f, 55.0f), Point2D(80.0f, 80.0f), Point2D(50.0f, 80.0f), Point2D(50.0f, 115.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c11_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 30.0f)), 5.5f),
                RoadCorridor("c11_r1_w", listOf(Point2D(20.0f, 30.0f), Point2D(50.0f, 30.0f)), 5.5f),
                RoadCorridor("c11_r1_e", listOf(Point2D(50.0f, 30.0f), Point2D(80.0f, 30.0f)), 5.5f),
                RoadCorridor("c11_col1_w", listOf(Point2D(20.0f, 30.0f), Point2D(20.0f, 55.0f)), 5.5f),
                RoadCorridor("c11_col1_e", listOf(Point2D(80.0f, 30.0f), Point2D(80.0f, 55.0f)), 5.5f),
                RoadCorridor("c11_r_mid", listOf(Point2D(20.0f, 55.0f), Point2D(80.0f, 55.0f)), 5.5f),
                RoadCorridor("c11_col2_w", listOf(Point2D(20.0f, 55.0f), Point2D(20.0f, 80.0f)), 5.5f),
                RoadCorridor("c11_col2_e", listOf(Point2D(80.0f, 55.0f), Point2D(80.0f, 80.0f)), 5.5f),
                RoadCorridor("c11_r2_w", listOf(Point2D(20.0f, 80.0f), Point2D(50.0f, 80.0f)), 5.5f),
                RoadCorridor("c11_r2_e", listOf(Point2D(50.0f, 80.0f), Point2D(80.0f, 80.0f)), 5.5f),
                RoadCorridor("c11_exit", listOf(Point2D(50.0f, 80.0f), Point2D(50.0f, 115.0f)), 5.5f),
                RoadCorridor("c11_trap1", listOf(Point2D(20.0f, 30.0f), Point2D(8.0f, 18.0f)), 5.5f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(80.0f, 30.0f),
            destinationPosition = Point2D(50.0f, 115.0f),
            thiefRoute = listOf(Point2D(80.0f, 30.0f), Point2D(80.0f, 55.0f), Point2D(80.0f, 80.0f), Point2D(50.0f, 80.0f), Point2D(50.0f, 115.0f))
        ),

        // Level 12: Gridiron Peril (14 corridors, 5.2m width, 35.0 m/s)
        LevelData(
            levelId = "level_12",
            title = "Gridiron Peril",
            roadLengthMeters = 125.0f,
            roadWidthMeters = 5.2f,
            thiefSpeedMps = 35.0f,
            initialGapMeters = 23.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 125.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f), Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f), Point2D(25.0f, 85.0f), Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c12_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 5.2f),
                RoadCorridor("c12_r1_w", listOf(Point2D(25.0f, 25.0f), Point2D(50.0f, 25.0f)), 5.2f),
                RoadCorridor("c12_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 5.2f),
                RoadCorridor("c12_col1_w", listOf(Point2D(25.0f, 25.0f), Point2D(25.0f, 55.0f)), 5.2f),
                RoadCorridor("c12_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f)), 5.2f),
                RoadCorridor("c12_r2_w", listOf(Point2D(25.0f, 55.0f), Point2D(50.0f, 55.0f)), 5.2f),
                RoadCorridor("c12_r2_e", listOf(Point2D(50.0f, 55.0f), Point2D(75.0f, 55.0f)), 5.2f),
                RoadCorridor("c12_col2_m", listOf(Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f)), 5.2f),
                RoadCorridor("c12_r3_w", listOf(Point2D(25.0f, 85.0f), Point2D(50.0f, 85.0f)), 5.2f),
                RoadCorridor("c12_r3_e", listOf(Point2D(50.0f, 85.0f), Point2D(75.0f, 85.0f)), 5.2f),
                RoadCorridor("c12_col3_w", listOf(Point2D(25.0f, 85.0f), Point2D(25.0f, 110.0f)), 5.2f),
                RoadCorridor("c12_r4_w", listOf(Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f)), 5.2f),
                RoadCorridor("c12_exit", listOf(Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f)), 5.2f),
                RoadCorridor("c12_trap1", listOf(Point2D(75.0f, 85.0f), Point2D(88.0f, 85.0f)), 5.2f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 125.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f), Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f), Point2D(25.0f, 85.0f), Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f))
        ),

        // Level 13: Triple Ring Gauntlet (16 corridors, 5.0m width, 37.0 m/s)
        LevelData(
            levelId = "level_13",
            title = "Triple Ring Gauntlet",
            roadLengthMeters = 130.0f,
            roadWidthMeters = 5.0f,
            thiefSpeedMps = 37.0f,
            initialGapMeters = 24.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 130.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f), Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f), Point2D(25.0f, 85.0f), Point2D(25.0f, 115.0f), Point2D(50.0f, 115.0f), Point2D(50.0f, 130.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c13_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 5.0f),
                RoadCorridor("c13_r1_w", listOf(Point2D(25.0f, 25.0f), Point2D(50.0f, 25.0f)), 5.0f),
                RoadCorridor("c13_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 5.0f),
                RoadCorridor("c13_col1_w", listOf(Point2D(25.0f, 25.0f), Point2D(25.0f, 55.0f)), 5.0f),
                RoadCorridor("c13_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f)), 5.0f),
                RoadCorridor("c13_r2_w", listOf(Point2D(25.0f, 55.0f), Point2D(50.0f, 55.0f)), 5.0f),
                RoadCorridor("c13_r2_e", listOf(Point2D(50.0f, 55.0f), Point2D(75.0f, 55.0f)), 5.0f),
                RoadCorridor("c13_col2_m", listOf(Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f)), 5.0f),
                RoadCorridor("c13_r3_w", listOf(Point2D(25.0f, 85.0f), Point2D(50.0f, 85.0f)), 5.0f),
                RoadCorridor("c13_r3_e", listOf(Point2D(50.0f, 85.0f), Point2D(75.0f, 85.0f)), 5.0f),
                RoadCorridor("c13_col3_w", listOf(Point2D(25.0f, 85.0f), Point2D(25.0f, 115.0f)), 5.0f),
                RoadCorridor("c13_col3_e", listOf(Point2D(75.0f, 85.0f), Point2D(75.0f, 115.0f)), 5.0f),
                RoadCorridor("c13_r4_w", listOf(Point2D(25.0f, 115.0f), Point2D(50.0f, 115.0f)), 5.0f),
                RoadCorridor("c13_r4_e", listOf(Point2D(50.0f, 115.0f), Point2D(75.0f, 115.0f)), 5.0f),
                RoadCorridor("c13_exit", listOf(Point2D(50.0f, 115.0f), Point2D(50.0f, 130.0f)), 5.0f),
                RoadCorridor("c13_trap1", listOf(Point2D(25.0f, 55.0f), Point2D(12.0f, 55.0f)), 5.0f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 130.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 55.0f), Point2D(50.0f, 55.0f), Point2D(50.0f, 85.0f), Point2D(25.0f, 85.0f), Point2D(25.0f, 115.0f), Point2D(50.0f, 115.0f), Point2D(50.0f, 130.0f))
        ),

        // Level 14: Serpent's Knot (18 corridors, 4.8m width, 39.5 m/s)
        LevelData(
            levelId = "level_14",
            title = "Serpent's Knot",
            roadLengthMeters = 135.0f,
            roadWidthMeters = 4.8f,
            thiefSpeedMps = 39.5f,
            initialGapMeters = 25.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 135.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 52.0f), Point2D(50.0f, 52.0f), Point2D(50.0f, 80.0f), Point2D(25.0f, 80.0f), Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f), Point2D(50.0f, 135.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c14_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 4.8f),
                RoadCorridor("c14_r1_w", listOf(Point2D(25.0f, 25.0f), Point2D(50.0f, 25.0f)), 4.8f),
                RoadCorridor("c14_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 4.8f),
                RoadCorridor("c14_col1_w", listOf(Point2D(25.0f, 25.0f), Point2D(25.0f, 52.0f)), 4.8f),
                RoadCorridor("c14_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 52.0f)), 4.8f),
                RoadCorridor("c14_r2_w", listOf(Point2D(25.0f, 52.0f), Point2D(50.0f, 52.0f)), 4.8f),
                RoadCorridor("c14_r2_e", listOf(Point2D(50.0f, 52.0f), Point2D(75.0f, 52.0f)), 4.8f),
                RoadCorridor("c14_col2_m", listOf(Point2D(50.0f, 52.0f), Point2D(50.0f, 80.0f)), 4.8f),
                RoadCorridor("c14_r3_w", listOf(Point2D(25.0f, 80.0f), Point2D(50.0f, 80.0f)), 4.8f),
                RoadCorridor("c14_r3_e", listOf(Point2D(50.0f, 80.0f), Point2D(75.0f, 80.0f)), 4.8f),
                RoadCorridor("c14_col3_w", listOf(Point2D(25.0f, 80.0f), Point2D(25.0f, 110.0f)), 4.8f),
                RoadCorridor("c14_col3_e", listOf(Point2D(75.0f, 80.0f), Point2D(75.0f, 110.0f)), 4.8f),
                RoadCorridor("c14_r4_w", listOf(Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f)), 4.8f),
                RoadCorridor("c14_r4_e", listOf(Point2D(50.0f, 110.0f), Point2D(75.0f, 110.0f)), 4.8f),
                RoadCorridor("c14_col4_m", listOf(Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f)), 4.8f),
                RoadCorridor("c14_exit", listOf(Point2D(50.0f, 125.0f), Point2D(50.0f, 135.0f)), 4.8f),
                RoadCorridor("c14_trap1", listOf(Point2D(75.0f, 52.0f), Point2D(88.0f, 52.0f)), 4.8f, isDeadEnd = true),
                RoadCorridor("c14_trap2", listOf(Point2D(25.0f, 110.0f), Point2D(12.0f, 110.0f)), 4.8f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 135.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 52.0f), Point2D(50.0f, 52.0f), Point2D(50.0f, 80.0f), Point2D(25.0f, 80.0f), Point2D(25.0f, 110.0f), Point2D(50.0f, 110.0f), Point2D(50.0f, 125.0f), Point2D(50.0f, 135.0f))
        ),

        // Level 15: The Quad Crossroad (22 corridors, 4.6m width, 42.0 m/s)
        LevelData(
            levelId = "level_15",
            title = "The Quad Crossroad",
            roadLengthMeters = 140.0f,
            roadWidthMeters = 4.6f,
            thiefSpeedMps = 42.0f,
            initialGapMeters = 26.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 140.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 50.0f), Point2D(50.0f, 50.0f), Point2D(50.0f, 75.0f), Point2D(25.0f, 75.0f), Point2D(25.0f, 100.0f), Point2D(50.0f, 100.0f), Point2D(50.0f, 122.0f), Point2D(35.0f, 122.0f), Point2D(35.0f, 132.0f), Point2D(50.0f, 132.0f), Point2D(50.0f, 140.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c15_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 4.6f),
                RoadCorridor("c15_r1_w", listOf(Point2D(25.0f, 25.0f), Point2D(50.0f, 25.0f)), 4.6f),
                RoadCorridor("c15_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 4.6f),
                RoadCorridor("c15_col1_w", listOf(Point2D(25.0f, 25.0f), Point2D(25.0f, 50.0f)), 4.6f),
                RoadCorridor("c15_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 50.0f)), 4.6f),
                RoadCorridor("c15_r2_w", listOf(Point2D(25.0f, 50.0f), Point2D(50.0f, 50.0f)), 4.6f),
                RoadCorridor("c15_r2_e", listOf(Point2D(50.0f, 50.0f), Point2D(75.0f, 50.0f)), 4.6f),
                RoadCorridor("c15_col2_m", listOf(Point2D(50.0f, 50.0f), Point2D(50.0f, 75.0f)), 4.6f),
                RoadCorridor("c15_r3_w", listOf(Point2D(25.0f, 75.0f), Point2D(50.0f, 75.0f)), 4.6f),
                RoadCorridor("c15_r3_e", listOf(Point2D(50.0f, 75.0f), Point2D(75.0f, 75.0f)), 4.6f),
                RoadCorridor("c15_col3_w", listOf(Point2D(25.0f, 75.0f), Point2D(25.0f, 100.0f)), 4.6f),
                RoadCorridor("c15_col3_e", listOf(Point2D(75.0f, 75.0f), Point2D(75.0f, 100.0f)), 4.6f),
                RoadCorridor("c15_r4_w", listOf(Point2D(25.0f, 100.0f), Point2D(50.0f, 100.0f)), 4.6f),
                RoadCorridor("c15_r4_e", listOf(Point2D(50.0f, 100.0f), Point2D(75.0f, 100.0f)), 4.6f),
                RoadCorridor("c15_col4_m", listOf(Point2D(50.0f, 100.0f), Point2D(50.0f, 122.0f)), 4.6f),
                RoadCorridor("c15_r5_w", listOf(Point2D(35.0f, 122.0f), Point2D(50.0f, 122.0f)), 4.6f),
                RoadCorridor("c15_r5_e", listOf(Point2D(50.0f, 122.0f), Point2D(65.0f, 122.0f)), 4.6f),
                RoadCorridor("c15_col5_w", listOf(Point2D(35.0f, 122.0f), Point2D(35.0f, 132.0f)), 4.6f),
                RoadCorridor("c15_r6_w", listOf(Point2D(35.0f, 132.0f), Point2D(50.0f, 132.0f)), 4.6f),
                RoadCorridor("c15_exit", listOf(Point2D(50.0f, 132.0f), Point2D(50.0f, 140.0f)), 4.6f),
                RoadCorridor("c15_trap1", listOf(Point2D(25.0f, 25.0f), Point2D(12.0f, 25.0f)), 4.6f, isDeadEnd = true),
                RoadCorridor("c15_trap2", listOf(Point2D(75.0f, 75.0f), Point2D(88.0f, 75.0f)), 4.6f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 140.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 50.0f), Point2D(50.0f, 50.0f), Point2D(50.0f, 75.0f), Point2D(25.0f, 75.0f), Point2D(25.0f, 100.0f), Point2D(50.0f, 100.0f), Point2D(50.0f, 122.0f), Point2D(35.0f, 122.0f), Point2D(35.0f, 132.0f), Point2D(50.0f, 132.0f), Point2D(50.0f, 140.0f))
        ),

        // Level 16: The Spiral Web (25 corridors, 4.4m width, 44.5 m/s)
        LevelData(
            levelId = "level_16",
            title = "The Spiral Web",
            roadLengthMeters = 145.0f,
            roadWidthMeters = 4.4f,
            thiefSpeedMps = 44.5f,
            initialGapMeters = 27.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 145.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 48.0f), Point2D(50.0f, 48.0f), Point2D(50.0f, 72.0f), Point2D(75.0f, 72.0f), Point2D(75.0f, 96.0f), Point2D(50.0f, 96.0f), Point2D(50.0f, 120.0f), Point2D(20.0f, 120.0f), Point2D(20.0f, 135.0f), Point2D(50.0f, 135.0f), Point2D(50.0f, 145.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c16_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 4.4f),
                RoadCorridor("c16_r1_w", listOf(Point2D(20.0f, 25.0f), Point2D(50.0f, 25.0f)), 4.4f),
                RoadCorridor("c16_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 4.4f),
                RoadCorridor("c16_col1_w", listOf(Point2D(20.0f, 25.0f), Point2D(20.0f, 48.0f)), 4.4f),
                RoadCorridor("c16_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 48.0f)), 4.4f),
                RoadCorridor("c16_r2_w", listOf(Point2D(20.0f, 48.0f), Point2D(50.0f, 48.0f)), 4.4f),
                RoadCorridor("c16_r2_e", listOf(Point2D(50.0f, 48.0f), Point2D(75.0f, 48.0f)), 4.4f),
                RoadCorridor("c16_col2_m", listOf(Point2D(50.0f, 48.0f), Point2D(50.0f, 72.0f)), 4.4f),
                RoadCorridor("c16_r3_w", listOf(Point2D(20.0f, 72.0f), Point2D(50.0f, 72.0f)), 4.4f),
                RoadCorridor("c16_r3_e", listOf(Point2D(50.0f, 72.0f), Point2D(75.0f, 72.0f)), 4.4f),
                RoadCorridor("c16_col3_w", listOf(Point2D(20.0f, 72.0f), Point2D(20.0f, 96.0f)), 4.4f),
                RoadCorridor("c16_col3_e", listOf(Point2D(75.0f, 72.0f), Point2D(75.0f, 96.0f)), 4.4f),
                RoadCorridor("c16_r4_w", listOf(Point2D(20.0f, 96.0f), Point2D(50.0f, 96.0f)), 4.4f),
                RoadCorridor("c16_r4_e", listOf(Point2D(50.0f, 96.0f), Point2D(75.0f, 96.0f)), 4.4f),
                RoadCorridor("c16_col4_m", listOf(Point2D(50.0f, 96.0f), Point2D(50.0f, 120.0f)), 4.4f),
                RoadCorridor("c16_r5_w", listOf(Point2D(20.0f, 120.0f), Point2D(50.0f, 120.0f)), 4.4f),
                RoadCorridor("c16_r5_e", listOf(Point2D(50.0f, 120.0f), Point2D(75.0f, 120.0f)), 4.4f),
                RoadCorridor("c16_col5_w", listOf(Point2D(20.0f, 120.0f), Point2D(20.0f, 135.0f)), 4.4f),
                RoadCorridor("c16_r6_w", listOf(Point2D(20.0f, 135.0f), Point2D(50.0f, 135.0f)), 4.4f),
                RoadCorridor("c16_exit", listOf(Point2D(50.0f, 135.0f), Point2D(50.0f, 145.0f)), 4.4f),
                RoadCorridor("c16_trap1", listOf(Point2D(20.0f, 48.0f), Point2D(8.0f, 48.0f)), 4.4f, isDeadEnd = true),
                RoadCorridor("c16_trap2", listOf(Point2D(75.0f, 72.0f), Point2D(88.0f, 72.0f)), 4.4f, isDeadEnd = true),
                RoadCorridor("c16_trap3", listOf(Point2D(20.0f, 96.0f), Point2D(8.0f, 96.0f)), 4.4f, isDeadEnd = true),
                RoadCorridor("c16_trap4", listOf(Point2D(75.0f, 120.0f), Point2D(88.0f, 120.0f)), 4.4f, isDeadEnd = true),
                RoadCorridor("c16_trap5", listOf(Point2D(50.0f, 135.0f), Point2D(65.0f, 135.0f)), 4.4f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 145.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 48.0f), Point2D(50.0f, 48.0f), Point2D(50.0f, 72.0f), Point2D(75.0f, 72.0f), Point2D(75.0f, 96.0f), Point2D(50.0f, 96.0f), Point2D(50.0f, 120.0f), Point2D(20.0f, 120.0f), Point2D(20.0f, 135.0f), Point2D(50.0f, 135.0f), Point2D(50.0f, 145.0f))
        ),

        // Level 17: The Switchback Maze (28 corridors, 4.2m width, 47.0 m/s)
        LevelData(
            levelId = "level_17",
            title = "The Switchback Maze",
            roadLengthMeters = 150.0f,
            roadWidthMeters = 4.2f,
            thiefSpeedMps = 47.0f,
            initialGapMeters = 28.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 150.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 46.0f), Point2D(50.0f, 46.0f), Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f), Point2D(50.0f, 90.0f), Point2D(50.0f, 114.0f), Point2D(75.0f, 114.0f), Point2D(75.0f, 136.0f), Point2D(50.0f, 136.0f), Point2D(50.0f, 150.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c17_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 4.2f),
                RoadCorridor("c17_r1_w", listOf(Point2D(20.0f, 25.0f), Point2D(50.0f, 25.0f)), 4.2f),
                RoadCorridor("c17_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 4.2f),
                RoadCorridor("c17_col1_w", listOf(Point2D(20.0f, 25.0f), Point2D(20.0f, 46.0f)), 4.2f),
                RoadCorridor("c17_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 46.0f)), 4.2f),
                RoadCorridor("c17_r2_w", listOf(Point2D(20.0f, 46.0f), Point2D(50.0f, 46.0f)), 4.2f),
                RoadCorridor("c17_r2_e", listOf(Point2D(50.0f, 46.0f), Point2D(75.0f, 46.0f)), 4.2f),
                RoadCorridor("c17_col2_m", listOf(Point2D(50.0f, 46.0f), Point2D(50.0f, 68.0f)), 4.2f),
                RoadCorridor("c17_r3_w", listOf(Point2D(20.0f, 68.0f), Point2D(50.0f, 68.0f)), 4.2f),
                RoadCorridor("c17_r3_e", listOf(Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f)), 4.2f),
                RoadCorridor("c17_col3_w", listOf(Point2D(20.0f, 68.0f), Point2D(20.0f, 90.0f)), 4.2f),
                RoadCorridor("c17_col3_e", listOf(Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f)), 4.2f),
                RoadCorridor("c17_r4_w", listOf(Point2D(20.0f, 90.0f), Point2D(50.0f, 90.0f)), 4.2f),
                RoadCorridor("c17_r4_e", listOf(Point2D(50.0f, 90.0f), Point2D(75.0f, 90.0f)), 4.2f),
                RoadCorridor("c17_col4_m", listOf(Point2D(50.0f, 90.0f), Point2D(50.0f, 114.0f)), 4.2f),
                RoadCorridor("c17_r5_w", listOf(Point2D(20.0f, 114.0f), Point2D(50.0f, 114.0f)), 4.2f),
                RoadCorridor("c17_r5_e", listOf(Point2D(50.0f, 114.0f), Point2D(75.0f, 114.0f)), 4.2f),
                RoadCorridor("c17_col5_w", listOf(Point2D(20.0f, 114.0f), Point2D(20.0f, 136.0f)), 4.2f),
                RoadCorridor("c17_col5_e", listOf(Point2D(75.0f, 114.0f), Point2D(75.0f, 136.0f)), 4.2f),
                RoadCorridor("c17_r6_w", listOf(Point2D(20.0f, 136.0f), Point2D(50.0f, 136.0f)), 4.2f),
                RoadCorridor("c17_r6_e", listOf(Point2D(50.0f, 136.0f), Point2D(75.0f, 136.0f)), 4.2f),
                RoadCorridor("c17_exit", listOf(Point2D(50.0f, 136.0f), Point2D(50.0f, 150.0f)), 4.2f),
                RoadCorridor("c17_trap1", listOf(Point2D(20.0f, 46.0f), Point2D(8.0f, 46.0f)), 4.2f, isDeadEnd = true),
                RoadCorridor("c17_trap2", listOf(Point2D(75.0f, 46.0f), Point2D(88.0f, 46.0f)), 4.2f, isDeadEnd = true),
                RoadCorridor("c17_trap3", listOf(Point2D(20.0f, 90.0f), Point2D(8.0f, 90.0f)), 4.2f, isDeadEnd = true),
                RoadCorridor("c17_trap4", listOf(Point2D(75.0f, 90.0f), Point2D(88.0f, 90.0f)), 4.2f, isDeadEnd = true),
                RoadCorridor("c17_trap5", listOf(Point2D(20.0f, 136.0f), Point2D(8.0f, 136.0f)), 4.2f, isDeadEnd = true),
                RoadCorridor("c17_trap6", listOf(Point2D(75.0f, 136.0f), Point2D(88.0f, 136.0f)), 4.2f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 150.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 46.0f), Point2D(50.0f, 46.0f), Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f), Point2D(50.0f, 90.0f), Point2D(50.0f, 114.0f), Point2D(75.0f, 114.0f), Point2D(75.0f, 136.0f), Point2D(50.0f, 136.0f), Point2D(50.0f, 150.0f))
        ),

        // Level 18: Octagon Citadel (32 corridors, 4.0m width, 50.0 m/s)
        LevelData(
            levelId = "level_18",
            title = "Octagon Citadel",
            roadLengthMeters = 155.0f,
            roadWidthMeters = 4.0f,
            thiefSpeedMps = 50.0f,
            initialGapMeters = 29.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 155.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 45.0f), Point2D(50.0f, 45.0f), Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f), Point2D(50.0f, 90.0f), Point2D(50.0f, 112.0f), Point2D(75.0f, 112.0f), Point2D(75.0f, 134.0f), Point2D(50.0f, 134.0f), Point2D(50.0f, 146.0f), Point2D(50.0f, 155.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c18_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 4.0f),
                RoadCorridor("c18_r1_w", listOf(Point2D(20.0f, 25.0f), Point2D(50.0f, 25.0f)), 4.0f),
                RoadCorridor("c18_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 4.0f),
                RoadCorridor("c18_col1_w", listOf(Point2D(20.0f, 25.0f), Point2D(20.0f, 45.0f)), 4.0f),
                RoadCorridor("c18_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 45.0f)), 4.0f),
                RoadCorridor("c18_r2_w", listOf(Point2D(20.0f, 45.0f), Point2D(50.0f, 45.0f)), 4.0f),
                RoadCorridor("c18_r2_e", listOf(Point2D(50.0f, 45.0f), Point2D(75.0f, 45.0f)), 4.0f),
                RoadCorridor("c18_col2_m", listOf(Point2D(50.0f, 45.0f), Point2D(50.0f, 68.0f)), 4.0f),
                RoadCorridor("c18_r3_w", listOf(Point2D(20.0f, 68.0f), Point2D(50.0f, 68.0f)), 4.0f),
                RoadCorridor("c18_r3_e", listOf(Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f)), 4.0f),
                RoadCorridor("c18_col3_w", listOf(Point2D(20.0f, 68.0f), Point2D(20.0f, 90.0f)), 4.0f),
                RoadCorridor("c18_col3_e", listOf(Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f)), 4.0f),
                RoadCorridor("c18_r4_w", listOf(Point2D(20.0f, 90.0f), Point2D(50.0f, 90.0f)), 4.0f),
                RoadCorridor("c18_r4_e", listOf(Point2D(50.0f, 90.0f), Point2D(75.0f, 90.0f)), 4.0f),
                RoadCorridor("c18_col4_m", listOf(Point2D(50.0f, 90.0f), Point2D(50.0f, 112.0f)), 4.0f),
                RoadCorridor("c18_r5_w", listOf(Point2D(20.0f, 112.0f), Point2D(50.0f, 112.0f)), 4.0f),
                RoadCorridor("c18_r5_e", listOf(Point2D(50.0f, 112.0f), Point2D(75.0f, 112.0f)), 4.0f),
                RoadCorridor("c18_col5_w", listOf(Point2D(20.0f, 112.0f), Point2D(20.0f, 134.0f)), 4.0f),
                RoadCorridor("c18_col5_e", listOf(Point2D(75.0f, 112.0f), Point2D(75.0f, 134.0f)), 4.0f),
                RoadCorridor("c18_r6_w", listOf(Point2D(20.0f, 134.0f), Point2D(50.0f, 134.0f)), 4.0f),
                RoadCorridor("c18_r6_e", listOf(Point2D(50.0f, 134.0f), Point2D(75.0f, 134.0f)), 4.0f),
                RoadCorridor("c18_col6_m", listOf(Point2D(50.0f, 134.0f), Point2D(50.0f, 146.0f)), 4.0f),
                RoadCorridor("c18_exit", listOf(Point2D(50.0f, 146.0f), Point2D(50.0f, 155.0f)), 4.0f),
                RoadCorridor("c18_trap1", listOf(Point2D(20.0f, 25.0f), Point2D(8.0f, 25.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap2", listOf(Point2D(75.0f, 25.0f), Point2D(88.0f, 25.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap3", listOf(Point2D(20.0f, 45.0f), Point2D(8.0f, 45.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap4", listOf(Point2D(75.0f, 45.0f), Point2D(88.0f, 45.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap5", listOf(Point2D(20.0f, 68.0f), Point2D(8.0f, 68.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap6", listOf(Point2D(75.0f, 68.0f), Point2D(88.0f, 68.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap7", listOf(Point2D(20.0f, 112.0f), Point2D(8.0f, 112.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap8", listOf(Point2D(75.0f, 112.0f), Point2D(88.0f, 112.0f)), 4.0f, isDeadEnd = true),
                RoadCorridor("c18_trap9", listOf(Point2D(20.0f, 134.0f), Point2D(8.0f, 134.0f)), 4.0f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 155.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 45.0f), Point2D(50.0f, 45.0f), Point2D(50.0f, 68.0f), Point2D(75.0f, 68.0f), Point2D(75.0f, 90.0f), Point2D(50.0f, 90.0f), Point2D(50.0f, 112.0f), Point2D(75.0f, 112.0f), Point2D(75.0f, 134.0f), Point2D(50.0f, 134.0f), Point2D(50.0f, 146.0f), Point2D(50.0f, 155.0f))
        ),

        // Level 19: The Minotaur's Web (36 corridors, 3.9m width, 53.0 m/s)
        LevelData(
            levelId = "level_19",
            title = "The Minotaur's Web",
            roadLengthMeters = 160.0f,
            roadWidthMeters = 3.9f,
            thiefSpeedMps = 53.0f,
            initialGapMeters = 30.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 160.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f), Point2D(50.0f, 44.0f), Point2D(50.0f, 65.0f), Point2D(75.0f, 65.0f), Point2D(75.0f, 86.0f), Point2D(50.0f, 86.0f), Point2D(50.0f, 108.0f), Point2D(75.0f, 108.0f), Point2D(75.0f, 128.0f), Point2D(50.0f, 128.0f), Point2D(50.0f, 146.0f), Point2D(50.0f, 160.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c19_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 3.9f),
                RoadCorridor("c19_r1_w", listOf(Point2D(20.0f, 25.0f), Point2D(50.0f, 25.0f)), 3.9f),
                RoadCorridor("c19_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 3.9f),
                RoadCorridor("c19_col1_w", listOf(Point2D(20.0f, 25.0f), Point2D(20.0f, 44.0f)), 3.9f),
                RoadCorridor("c19_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f)), 3.9f),
                RoadCorridor("c19_r2_w", listOf(Point2D(20.0f, 44.0f), Point2D(50.0f, 44.0f)), 3.9f),
                RoadCorridor("c19_r2_e", listOf(Point2D(50.0f, 44.0f), Point2D(75.0f, 44.0f)), 3.9f),
                RoadCorridor("c19_col2_m", listOf(Point2D(50.0f, 44.0f), Point2D(50.0f, 65.0f)), 3.9f),
                RoadCorridor("c19_r3_w", listOf(Point2D(20.0f, 65.0f), Point2D(50.0f, 65.0f)), 3.9f),
                RoadCorridor("c19_r3_e", listOf(Point2D(50.0f, 65.0f), Point2D(75.0f, 65.0f)), 3.9f),
                RoadCorridor("c19_col3_w", listOf(Point2D(20.0f, 65.0f), Point2D(20.0f, 86.0f)), 3.9f),
                RoadCorridor("c19_col3_e", listOf(Point2D(75.0f, 65.0f), Point2D(75.0f, 86.0f)), 3.9f),
                RoadCorridor("c19_r4_w", listOf(Point2D(20.0f, 86.0f), Point2D(50.0f, 86.0f)), 3.9f),
                RoadCorridor("c19_r4_e", listOf(Point2D(50.0f, 86.0f), Point2D(75.0f, 86.0f)), 3.9f),
                RoadCorridor("c19_col4_m", listOf(Point2D(50.0f, 86.0f), Point2D(50.0f, 108.0f)), 3.9f),
                RoadCorridor("c19_r5_w", listOf(Point2D(20.0f, 108.0f), Point2D(50.0f, 108.0f)), 3.9f),
                RoadCorridor("c19_r5_e", listOf(Point2D(50.0f, 108.0f), Point2D(75.0f, 108.0f)), 3.9f),
                RoadCorridor("c19_col5_w", listOf(Point2D(20.0f, 108.0f), Point2D(20.0f, 128.0f)), 3.9f),
                RoadCorridor("c19_col5_e", listOf(Point2D(75.0f, 108.0f), Point2D(75.0f, 128.0f)), 3.9f),
                RoadCorridor("c19_r6_w", listOf(Point2D(20.0f, 128.0f), Point2D(50.0f, 128.0f)), 3.9f),
                RoadCorridor("c19_r6_e", listOf(Point2D(50.0f, 128.0f), Point2D(75.0f, 128.0f)), 3.9f),
                RoadCorridor("c19_col6_m", listOf(Point2D(50.0f, 128.0f), Point2D(50.0f, 146.0f)), 3.9f),
                RoadCorridor("c19_r7_w", listOf(Point2D(20.0f, 146.0f), Point2D(50.0f, 146.0f)), 3.9f),
                RoadCorridor("c19_r7_e", listOf(Point2D(50.0f, 146.0f), Point2D(75.0f, 146.0f)), 3.9f),
                RoadCorridor("c19_exit", listOf(Point2D(50.0f, 146.0f), Point2D(50.0f, 160.0f)), 3.9f),
                RoadCorridor("c19_trap1", listOf(Point2D(20.0f, 25.0f), Point2D(8.0f, 25.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap2", listOf(Point2D(75.0f, 25.0f), Point2D(88.0f, 25.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap3", listOf(Point2D(20.0f, 44.0f), Point2D(8.0f, 44.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap4", listOf(Point2D(75.0f, 44.0f), Point2D(88.0f, 44.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap5", listOf(Point2D(20.0f, 65.0f), Point2D(8.0f, 65.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap6", listOf(Point2D(75.0f, 65.0f), Point2D(88.0f, 65.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap7", listOf(Point2D(20.0f, 86.0f), Point2D(8.0f, 86.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap8", listOf(Point2D(75.0f, 86.0f), Point2D(88.0f, 86.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap9", listOf(Point2D(20.0f, 108.0f), Point2D(8.0f, 108.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap10", listOf(Point2D(75.0f, 108.0f), Point2D(88.0f, 108.0f)), 3.9f, isDeadEnd = true),
                RoadCorridor("c19_trap11", listOf(Point2D(20.0f, 146.0f), Point2D(8.0f, 146.0f)), 3.9f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 160.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f), Point2D(50.0f, 44.0f), Point2D(50.0f, 65.0f), Point2D(75.0f, 65.0f), Point2D(75.0f, 86.0f), Point2D(50.0f, 86.0f), Point2D(50.0f, 108.0f), Point2D(75.0f, 108.0f), Point2D(75.0f, 128.0f), Point2D(50.0f, 128.0f), Point2D(50.0f, 146.0f), Point2D(50.0f, 160.0f))
        ),

        // Level 20: Mastermind's Escape (40 corridors, 3.8m width, 56.0 m/s)
        LevelData(
            levelId = "level_20",
            title = "Mastermind's Escape",
            roadLengthMeters = 165.0f,
            roadWidthMeters = 3.8f,
            thiefSpeedMps = 56.0f,
            initialGapMeters = 32.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 165.0f,
            roadPath = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f), Point2D(50.0f, 44.0f), Point2D(50.0f, 64.0f), Point2D(75.0f, 64.0f), Point2D(75.0f, 84.0f), Point2D(50.0f, 84.0f), Point2D(50.0f, 104.0f), Point2D(75.0f, 104.0f), Point2D(75.0f, 124.0f), Point2D(50.0f, 124.0f), Point2D(50.0f, 144.0f), Point2D(75.0f, 144.0f), Point2D(75.0f, 155.0f), Point2D(50.0f, 155.0f), Point2D(50.0f, 165.0f)),
            boundaries = emptyList(),
            isPuzzle = true,
            corridors = listOf(
                RoadCorridor("c20_entry", listOf(Point2D(50.0f, 10.0f), Point2D(50.0f, 25.0f)), 3.8f),
                RoadCorridor("c20_r1_w", listOf(Point2D(20.0f, 25.0f), Point2D(50.0f, 25.0f)), 3.8f),
                RoadCorridor("c20_r1_e", listOf(Point2D(50.0f, 25.0f), Point2D(75.0f, 25.0f)), 3.8f),
                RoadCorridor("c20_col1_w", listOf(Point2D(20.0f, 25.0f), Point2D(20.0f, 44.0f)), 3.8f),
                RoadCorridor("c20_col1_e", listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f)), 3.8f),
                RoadCorridor("c20_r2_w", listOf(Point2D(20.0f, 44.0f), Point2D(50.0f, 44.0f)), 3.8f),
                RoadCorridor("c20_r2_e", listOf(Point2D(50.0f, 44.0f), Point2D(75.0f, 44.0f)), 3.8f),
                RoadCorridor("c20_col2_m", listOf(Point2D(50.0f, 44.0f), Point2D(50.0f, 64.0f)), 3.8f),
                RoadCorridor("c20_r3_w", listOf(Point2D(20.0f, 64.0f), Point2D(50.0f, 64.0f)), 3.8f),
                RoadCorridor("c20_r3_e", listOf(Point2D(50.0f, 64.0f), Point2D(75.0f, 64.0f)), 3.8f),
                RoadCorridor("c20_col3_w", listOf(Point2D(20.0f, 64.0f), Point2D(20.0f, 84.0f)), 3.8f),
                RoadCorridor("c20_col3_e", listOf(Point2D(75.0f, 64.0f), Point2D(75.0f, 84.0f)), 3.8f),
                RoadCorridor("c20_r4_w", listOf(Point2D(20.0f, 84.0f), Point2D(50.0f, 84.0f)), 3.8f),
                RoadCorridor("c20_r4_e", listOf(Point2D(50.0f, 84.0f), Point2D(75.0f, 84.0f)), 3.8f),
                RoadCorridor("c20_col4_m", listOf(Point2D(50.0f, 84.0f), Point2D(50.0f, 104.0f)), 3.8f),
                RoadCorridor("c20_r5_w", listOf(Point2D(20.0f, 104.0f), Point2D(50.0f, 104.0f)), 3.8f),
                RoadCorridor("c20_r5_e", listOf(Point2D(50.0f, 104.0f), Point2D(75.0f, 104.0f)), 3.8f),
                RoadCorridor("c20_col5_w", listOf(Point2D(20.0f, 104.0f), Point2D(20.0f, 124.0f)), 3.8f),
                RoadCorridor("c20_col5_e", listOf(Point2D(75.0f, 104.0f), Point2D(75.0f, 124.0f)), 3.8f),
                RoadCorridor("c20_r6_w", listOf(Point2D(20.0f, 124.0f), Point2D(50.0f, 124.0f)), 3.8f),
                RoadCorridor("c20_r6_e", listOf(Point2D(50.0f, 124.0f), Point2D(75.0f, 124.0f)), 3.8f),
                RoadCorridor("c20_col6_m", listOf(Point2D(50.0f, 124.0f), Point2D(50.0f, 144.0f)), 3.8f),
                RoadCorridor("c20_r7_w", listOf(Point2D(20.0f, 144.0f), Point2D(50.0f, 144.0f)), 3.8f),
                RoadCorridor("c20_r7_e", listOf(Point2D(50.0f, 144.0f), Point2D(75.0f, 144.0f)), 3.8f),
                RoadCorridor("c20_col7_w", listOf(Point2D(20.0f, 144.0f), Point2D(20.0f, 155.0f)), 3.8f),
                RoadCorridor("c20_col7_e", listOf(Point2D(75.0f, 144.0f), Point2D(75.0f, 155.0f)), 3.8f),
                RoadCorridor("c20_r8_w", listOf(Point2D(20.0f, 155.0f), Point2D(50.0f, 155.0f)), 3.8f),
                RoadCorridor("c20_r8_e", listOf(Point2D(50.0f, 155.0f), Point2D(75.0f, 155.0f)), 3.8f),
                RoadCorridor("c20_exit", listOf(Point2D(50.0f, 155.0f), Point2D(50.0f, 165.0f)), 3.8f),
                RoadCorridor("c20_trap1", listOf(Point2D(20.0f, 25.0f), Point2D(8.0f, 25.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap2", listOf(Point2D(75.0f, 25.0f), Point2D(88.0f, 25.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap3", listOf(Point2D(20.0f, 44.0f), Point2D(8.0f, 44.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap4", listOf(Point2D(75.0f, 44.0f), Point2D(88.0f, 44.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap5", listOf(Point2D(20.0f, 64.0f), Point2D(8.0f, 64.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap6", listOf(Point2D(75.0f, 64.0f), Point2D(88.0f, 64.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap7", listOf(Point2D(20.0f, 84.0f), Point2D(8.0f, 84.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap8", listOf(Point2D(75.0f, 84.0f), Point2D(88.0f, 84.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap9", listOf(Point2D(20.0f, 104.0f), Point2D(8.0f, 104.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap10", listOf(Point2D(75.0f, 104.0f), Point2D(88.0f, 104.0f)), 3.8f, isDeadEnd = true),
                RoadCorridor("c20_trap11", listOf(Point2D(20.0f, 155.0f), Point2D(8.0f, 155.0f)), 3.8f, isDeadEnd = true)
            ),
            policeStartPosition = Point2D(50.0f, 10.0f),
            thiefStartPosition = Point2D(75.0f, 25.0f),
            destinationPosition = Point2D(50.0f, 165.0f),
            thiefRoute = listOf(Point2D(75.0f, 25.0f), Point2D(75.0f, 44.0f), Point2D(50.0f, 44.0f), Point2D(50.0f, 64.0f), Point2D(75.0f, 64.0f), Point2D(75.0f, 84.0f), Point2D(50.0f, 84.0f), Point2D(50.0f, 104.0f), Point2D(75.0f, 104.0f), Point2D(75.0f, 124.0f), Point2D(50.0f, 124.0f), Point2D(50.0f, 144.0f), Point2D(75.0f, 144.0f), Point2D(75.0f, 155.0f), Point2D(50.0f, 155.0f), Point2D(50.0f, 165.0f))
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
