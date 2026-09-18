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

        // Level 2: Winding Canyon (10.5 m/s)
        LevelData(
            levelId = "level_2",
            title = "Winding Canyon",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 11.5f,
            thiefSpeedMps = 10.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(46.0f, 10.0f),
                Point2D(40.0f, 22.0f),
                Point2D(38.0f, 32.0f),
                Point2D(45.0f, 45.0f),
                Point2D(56.0f, 58.0f),
                Point2D(64.0f, 70.0f),
                Point2D(62.0f, 82.0f),
                Point2D(55.0f, 92.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.25f, 0.0f), Point2D(55.75f, 0.0f)),
                RoadBoundary(Point2D(40.25f, 10.0f), Point2D(51.75f, 10.0f)),
                RoadBoundary(Point2D(34.25f, 22.0f), Point2D(45.75f, 22.0f)),
                RoadBoundary(Point2D(32.25f, 32.0f), Point2D(43.75f, 32.0f)),
                RoadBoundary(Point2D(39.25f, 45.0f), Point2D(50.75f, 45.0f)),
                RoadBoundary(Point2D(50.25f, 58.0f), Point2D(61.75f, 58.0f)),
                RoadBoundary(Point2D(58.25f, 70.0f), Point2D(69.75f, 70.0f)),
                RoadBoundary(Point2D(56.25f, 82.0f), Point2D(67.75f, 82.0f)),
                RoadBoundary(Point2D(49.25f, 92.0f), Point2D(60.75f, 92.0f)),
                RoadBoundary(Point2D(44.25f, 100.0f), Point2D(55.75f, 100.0f))
            )
        ),

        // Level 3: Hairpin Ridge (12.0 m/s)
        LevelData(
            levelId = "level_3",
            title = "Hairpin Ridge",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 11.0f,
            thiefSpeedMps = 12.0f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(50.0f, 12.0f),
                Point2D(35.0f, 26.0f),
                Point2D(34.0f, 38.0f),
                Point2D(65.0f, 56.0f),
                Point2D(67.0f, 68.0f),
                Point2D(38.0f, 84.0f),
                Point2D(48.0f, 92.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.5f, 0.0f), Point2D(55.5f, 0.0f)),
                RoadBoundary(Point2D(44.5f, 12.0f), Point2D(55.5f, 12.0f)),
                RoadBoundary(Point2D(29.5f, 26.0f), Point2D(40.5f, 26.0f)),
                RoadBoundary(Point2D(28.5f, 38.0f), Point2D(39.5f, 38.0f)),
                RoadBoundary(Point2D(59.5f, 56.0f), Point2D(70.5f, 56.0f)),
                RoadBoundary(Point2D(61.5f, 68.0f), Point2D(72.5f, 68.0f)),
                RoadBoundary(Point2D(32.5f, 84.0f), Point2D(43.5f, 84.0f)),
                RoadBoundary(Point2D(42.5f, 92.0f), Point2D(53.5f, 92.0f)),
                RoadBoundary(Point2D(44.5f, 100.0f), Point2D(55.5f, 100.0f))
            )
        ),

        // Level 4: Mountain Pass (13.5 m/s)
        LevelData(
            levelId = "level_4",
            title = "Mountain Pass",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 11.0f,
            thiefSpeedMps = 13.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(58.0f, 14.0f),
                Point2D(68.0f, 28.0f),
                Point2D(72.0f, 42.0f),
                Point2D(64.0f, 58.0f),
                Point2D(42.0f, 74.0f),
                Point2D(38.0f, 86.0f),
                Point2D(48.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.5f, 0.0f), Point2D(55.5f, 0.0f)),
                RoadBoundary(Point2D(52.5f, 14.0f), Point2D(63.5f, 14.0f)),
                RoadBoundary(Point2D(62.5f, 28.0f), Point2D(73.5f, 28.0f)),
                RoadBoundary(Point2D(66.5f, 42.0f), Point2D(77.5f, 42.0f)),
                RoadBoundary(Point2D(58.5f, 58.0f), Point2D(69.5f, 58.0f)),
                RoadBoundary(Point2D(36.5f, 74.0f), Point2D(47.5f, 74.0f)),
                RoadBoundary(Point2D(32.5f, 86.0f), Point2D(43.5f, 86.0f)),
                RoadBoundary(Point2D(42.5f, 100.0f), Point2D(53.5f, 100.0f))
            )
        ),

        // Level 5: Coastal Serpent (15.0 m/s)
        LevelData(
            levelId = "level_5",
            title = "Coastal Serpent",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 10.5f,
            thiefSpeedMps = 15.0f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(38.0f, 14.0f),
                Point2D(42.0f, 26.0f),
                Point2D(62.0f, 40.0f),
                Point2D(66.0f, 52.0f),
                Point2D(42.0f, 66.0f),
                Point2D(36.0f, 78.0f),
                Point2D(56.0f, 90.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.75f, 0.0f), Point2D(55.25f, 0.0f)),
                RoadBoundary(Point2D(32.75f, 14.0f), Point2D(43.25f, 14.0f)),
                RoadBoundary(Point2D(36.75f, 26.0f), Point2D(47.25f, 26.0f)),
                RoadBoundary(Point2D(56.75f, 40.0f), Point2D(67.25f, 40.0f)),
                RoadBoundary(Point2D(60.75f, 52.0f), Point2D(71.25f, 52.0f)),
                RoadBoundary(Point2D(36.75f, 66.0f), Point2D(47.25f, 66.0f)),
                RoadBoundary(Point2D(30.75f, 78.0f), Point2D(41.25f, 78.0f)),
                RoadBoundary(Point2D(50.75f, 90.0f), Point2D(61.25f, 90.0f)),
                RoadBoundary(Point2D(44.75f, 100.0f), Point2D(55.25f, 100.0f))
            )
        ),

        // Level 6: Midnight Expressway (16.5 m/s)
        LevelData(
            levelId = "level_6",
            title = "Midnight Expressway",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 10.5f,
            thiefSpeedMps = 16.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(62.0f, 10.0f),
                Point2D(70.0f, 22.0f),
                Point2D(54.0f, 36.0f),
                Point2D(36.0f, 48.0f),
                Point2D(42.0f, 60.0f),
                Point2D(66.0f, 72.0f),
                Point2D(68.0f, 84.0f),
                Point2D(52.0f, 92.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(44.75f, 0.0f), Point2D(55.25f, 0.0f)),
                RoadBoundary(Point2D(56.75f, 10.0f), Point2D(67.25f, 10.0f)),
                RoadBoundary(Point2D(64.75f, 22.0f), Point2D(75.25f, 22.0f)),
                RoadBoundary(Point2D(48.75f, 36.0f), Point2D(59.25f, 36.0f)),
                RoadBoundary(Point2D(30.75f, 48.0f), Point2D(41.25f, 48.0f)),
                RoadBoundary(Point2D(36.75f, 60.0f), Point2D(47.25f, 60.0f)),
                RoadBoundary(Point2D(60.75f, 72.0f), Point2D(71.25f, 72.0f)),
                RoadBoundary(Point2D(62.75f, 84.0f), Point2D(73.25f, 84.0f)),
                RoadBoundary(Point2D(46.75f, 92.0f), Point2D(57.25f, 92.0f)),
                RoadBoundary(Point2D(44.75f, 100.0f), Point2D(55.25f, 100.0f))
            )
        ),

        // Level 7: Neon Metropolis (18.0 m/s)
        LevelData(
            levelId = "level_7",
            title = "Neon Metropolis",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 10.0f,
            thiefSpeedMps = 18.0f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(42.0f, 12.0f),
                Point2D(38.0f, 24.0f),
                Point2D(55.0f, 38.0f),
                Point2D(68.0f, 50.0f),
                Point2D(65.0f, 62.0f),
                Point2D(42.0f, 74.0f),
                Point2D(36.0f, 86.0f),
                Point2D(48.0f, 94.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.0f, 0.0f), Point2D(55.0f, 0.0f)),
                RoadBoundary(Point2D(37.0f, 12.0f), Point2D(47.0f, 12.0f)),
                RoadBoundary(Point2D(33.0f, 24.0f), Point2D(43.0f, 24.0f)),
                RoadBoundary(Point2D(50.0f, 38.0f), Point2D(60.0f, 38.0f)),
                RoadBoundary(Point2D(63.0f, 50.0f), Point2D(73.0f, 50.0f)),
                RoadBoundary(Point2D(60.0f, 62.0f), Point2D(70.0f, 62.0f)),
                RoadBoundary(Point2D(37.0f, 74.0f), Point2D(47.0f, 74.0f)),
                RoadBoundary(Point2D(31.0f, 86.0f), Point2D(41.0f, 86.0f)),
                RoadBoundary(Point2D(43.0f, 94.0f), Point2D(53.0f, 94.0f)),
                RoadBoundary(Point2D(45.0f, 100.0f), Point2D(55.0f, 100.0f))
            )
        ),

        // Level 8: Thunder Valley (19.5 m/s)
        LevelData(
            levelId = "level_8",
            title = "Thunder Valley",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 10.0f,
            thiefSpeedMps = 19.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(65.0f, 15.0f),
                Point2D(34.0f, 32.0f),
                Point2D(68.0f, 52.0f),
                Point2D(32.0f, 72.0f),
                Point2D(60.0f, 88.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.0f, 0.0f), Point2D(55.0f, 0.0f)),
                RoadBoundary(Point2D(60.0f, 15.0f), Point2D(70.0f, 15.0f)),
                RoadBoundary(Point2D(29.0f, 32.0f), Point2D(39.0f, 32.0f)),
                RoadBoundary(Point2D(63.0f, 52.0f), Point2D(73.0f, 52.0f)),
                RoadBoundary(Point2D(27.0f, 72.0f), Point2D(37.0f, 72.0f)),
                RoadBoundary(Point2D(55.0f, 88.0f), Point2D(65.0f, 88.0f)),
                RoadBoundary(Point2D(45.0f, 100.0f), Point2D(55.0f, 100.0f))
            )
        ),

        // Level 9: Inferno Ridge (21.0 m/s)
        LevelData(
            levelId = "level_9",
            title = "Inferno Ridge",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 9.5f,
            thiefSpeedMps = 21.0f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(32.0f, 14.0f),
                Point2D(68.0f, 28.0f),
                Point2D(28.0f, 44.0f),
                Point2D(72.0f, 60.0f),
                Point2D(35.0f, 75.0f),
                Point2D(62.0f, 88.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.25f, 0.0f), Point2D(54.75f, 0.0f)),
                RoadBoundary(Point2D(27.25f, 14.0f), Point2D(36.75f, 14.0f)),
                RoadBoundary(Point2D(63.25f, 28.0f), Point2D(72.75f, 28.0f)),
                RoadBoundary(Point2D(23.25f, 44.0f), Point2D(32.75f, 44.0f)),
                RoadBoundary(Point2D(67.25f, 60.0f), Point2D(76.75f, 60.0f)),
                RoadBoundary(Point2D(30.25f, 75.0f), Point2D(39.75f, 75.0f)),
                RoadBoundary(Point2D(57.25f, 88.0f), Point2D(66.75f, 88.0f)),
                RoadBoundary(Point2D(45.25f, 100.0f), Point2D(54.75f, 100.0f))
            )
        ),

        // Level 10: The Final Gauntlet (22.5 m/s)
        LevelData(
            levelId = "level_10",
            title = "The Final Gauntlet",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 9.0f,
            thiefSpeedMps = 22.5f,
            initialGapMeters = 16.0f,
            startPositionMeters = 0.0f,
            finishPositionMeters = 100.0f,
            roadPath = listOf(
                Point2D(50.0f, 0.0f),
                Point2D(66.0f, 12.0f),
                Point2D(30.0f, 25.0f),
                Point2D(72.0f, 38.0f),
                Point2D(26.0f, 50.0f),
                Point2D(70.0f, 64.0f),
                Point2D(28.0f, 78.0f),
                Point2D(64.0f, 90.0f),
                Point2D(50.0f, 100.0f)
            ),
            boundaries = listOf(
                RoadBoundary(Point2D(45.5f, 0.0f), Point2D(54.5f, 0.0f)),
                RoadBoundary(Point2D(61.5f, 12.0f), Point2D(70.5f, 12.0f)),
                RoadBoundary(Point2D(25.5f, 25.0f), Point2D(34.5f, 25.0f)),
                RoadBoundary(Point2D(67.5f, 38.0f), Point2D(76.5f, 38.0f)),
                RoadBoundary(Point2D(21.5f, 50.0f), Point2D(30.5f, 50.0f)),
                RoadBoundary(Point2D(65.5f, 64.0f), Point2D(74.5f, 64.0f)),
                RoadBoundary(Point2D(23.5f, 78.0f), Point2D(32.5f, 78.0f)),
                RoadBoundary(Point2D(59.5f, 90.0f), Point2D(68.5f, 90.0f)),
                RoadBoundary(Point2D(45.5f, 100.0f), Point2D(54.5f, 100.0f))
            )
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
