package com.example.policetheifgame.game.geometry

import android.content.Context
import com.example.policetheifgame.game.model.Point2D

/**
 * Repository providing the collection of playable game levels.
 */
object LevelRepository {

    /**
     * Built-in definitions of all 6 levels, ensuring deterministic operation
     * in both Android runtime and JVM unit tests.
     */
    private val builtInLevels: List<LevelData> = listOf(
        // Level 1: Sunny Highway
        LevelData.createDefaultLevel1(),

        // Level 2: Winding Canyon (Double S-curve slalom)
        LevelData(
            levelId = "level_2",
            title = "Winding Canyon",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 7.5f,
            thiefSpeedMps = 10.5f,
            initialGapMeters = 22.0f,
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
                RoadBoundary(Point2D(46.25f, 0.0f), Point2D(53.75f, 0.0f)),
                RoadBoundary(Point2D(42.25f, 10.0f), Point2D(49.75f, 10.0f)),
                RoadBoundary(Point2D(36.25f, 22.0f), Point2D(43.75f, 22.0f)),
                RoadBoundary(Point2D(34.25f, 32.0f), Point2D(41.75f, 32.0f)),
                RoadBoundary(Point2D(41.25f, 45.0f), Point2D(48.75f, 45.0f)),
                RoadBoundary(Point2D(52.25f, 58.0f), Point2D(59.75f, 58.0f)),
                RoadBoundary(Point2D(60.25f, 70.0f), Point2D(67.75f, 70.0f)),
                RoadBoundary(Point2D(58.25f, 82.0f), Point2D(65.75f, 82.0f)),
                RoadBoundary(Point2D(51.25f, 92.0f), Point2D(58.75f, 92.0f)),
                RoadBoundary(Point2D(46.25f, 100.0f), Point2D(53.75f, 100.0f))
            )
        ),

        // Level 3: Hairpin Ridge (Sharp switchback chicane)
        LevelData(
            levelId = "level_3",
            title = "Hairpin Ridge",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 7.0f,
            thiefSpeedMps = 11.0f,
            initialGapMeters = 24.0f,
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
                RoadBoundary(Point2D(46.5f, 0.0f), Point2D(53.5f, 0.0f)),
                RoadBoundary(Point2D(46.5f, 12.0f), Point2D(53.5f, 12.0f)),
                RoadBoundary(Point2D(31.5f, 26.0f), Point2D(38.5f, 26.0f)),
                RoadBoundary(Point2D(30.5f, 38.0f), Point2D(37.5f, 38.0f)),
                RoadBoundary(Point2D(61.5f, 56.0f), Point2D(68.5f, 56.0f)),
                RoadBoundary(Point2D(63.5f, 68.0f), Point2D(70.5f, 68.0f)),
                RoadBoundary(Point2D(34.5f, 84.0f), Point2D(41.5f, 84.0f)),
                RoadBoundary(Point2D(44.5f, 92.0f), Point2D(51.5f, 92.0f)),
                RoadBoundary(Point2D(46.5f, 100.0f), Point2D(53.5f, 100.0f))
            )
        ),

        // Level 4: Mountain Pass (Long sweeping turns)
        LevelData(
            levelId = "level_4",
            title = "Mountain Pass",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 6.5f,
            thiefSpeedMps = 11.5f,
            initialGapMeters = 25.0f,
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
                RoadBoundary(Point2D(46.75f, 0.0f), Point2D(53.25f, 0.0f)),
                RoadBoundary(Point2D(54.75f, 14.0f), Point2D(61.25f, 14.0f)),
                RoadBoundary(Point2D(64.75f, 28.0f), Point2D(71.25f, 28.0f)),
                RoadBoundary(Point2D(68.75f, 42.0f), Point2D(75.25f, 42.0f)),
                RoadBoundary(Point2D(60.75f, 58.0f), Point2D(67.25f, 58.0f)),
                RoadBoundary(Point2D(38.75f, 74.0f), Point2D(45.25f, 74.0f)),
                RoadBoundary(Point2D(34.75f, 86.0f), Point2D(41.25f, 86.0f)),
                RoadBoundary(Point2D(44.75f, 100.0f), Point2D(51.25f, 100.0f))
            )
        ),

        // Level 5: Coastal Serpent (Rapid multi S-curve near water)
        LevelData(
            levelId = "level_5",
            title = "Coastal Serpent",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 6.0f,
            thiefSpeedMps = 12.0f,
            initialGapMeters = 26.0f,
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
                RoadBoundary(Point2D(47.0f, 0.0f), Point2D(53.0f, 0.0f)),
                RoadBoundary(Point2D(35.0f, 14.0f), Point2D(41.0f, 14.0f)),
                RoadBoundary(Point2D(39.0f, 26.0f), Point2D(45.0f, 26.0f)),
                RoadBoundary(Point2D(59.0f, 40.0f), Point2D(65.0f, 40.0f)),
                RoadBoundary(Point2D(63.0f, 52.0f), Point2D(69.0f, 52.0f)),
                RoadBoundary(Point2D(39.0f, 66.0f), Point2D(45.0f, 66.0f)),
                RoadBoundary(Point2D(33.0f, 78.0f), Point2D(39.0f, 78.0f)),
                RoadBoundary(Point2D(53.0f, 90.0f), Point2D(59.0f, 90.0f)),
                RoadBoundary(Point2D(47.0f, 100.0f), Point2D(53.0f, 100.0f))
            )
        ),

        // Level 6: Midnight Expressway (Master tier - narrow, ultra-fast apexes)
        LevelData(
            levelId = "level_6",
            title = "Midnight Expressway",
            roadLengthMeters = 100.0f,
            roadWidthMeters = 5.8f,
            thiefSpeedMps = 12.5f,
            initialGapMeters = 28.0f,
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
                RoadBoundary(Point2D(47.1f, 0.0f), Point2D(52.9f, 0.0f)),
                RoadBoundary(Point2D(59.1f, 10.0f), Point2D(64.9f, 10.0f)),
                RoadBoundary(Point2D(67.1f, 22.0f), Point2D(72.9f, 22.0f)),
                RoadBoundary(Point2D(51.1f, 36.0f), Point2D(56.9f, 36.0f)),
                RoadBoundary(Point2D(33.1f, 48.0f), Point2D(38.9f, 48.0f)),
                RoadBoundary(Point2D(39.1f, 60.0f), Point2D(44.9f, 60.0f)),
                RoadBoundary(Point2D(63.1f, 72.0f), Point2D(68.9f, 72.0f)),
                RoadBoundary(Point2D(65.1f, 84.0f), Point2D(70.9f, 84.0f)),
                RoadBoundary(Point2D(49.1f, 92.0f), Point2D(54.9f, 92.0f)),
                RoadBoundary(Point2D(47.1f, 100.0f), Point2D(52.9f, 100.0f))
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
