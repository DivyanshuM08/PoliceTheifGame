package com.example.policetheifgame.game.model

/**
 * High-level state of the game loop.
 */
enum class GameStatus {
    READY,
    PLAYING,
    POLICE_WON,
    THIEF_WON
}

/**
 * Specific reason why the game ended.
 */
enum class GameOverReason {
    NONE,
    CAUGHT_THIEF,
    OFF_ROAD,
    THIEF_ESCAPED
}

/**
 * Complete immutable snapshot of the game state exposed to the UI layer.
 */
data class GameState(
    val status: GameStatus = GameStatus.READY,
    val reason: GameOverReason = GameOverReason.NONE,
    val levelIndex: Int = 0,
    val levelTitle: String = "Sunny Highway",
    val totalLevels: Int = 6,
    val unlockedLevelIndex: Int = 0,
    val policeDistanceMeters: Float = 0f,
    val policePosition: Point2D = Point2D(50f, 0f),
    val policeHeadingDeg: Float = 0f,
    val thiefDistanceMeters: Float = 20f,
    val thiefPosition: Point2D = Point2D(52f, 20f),
    val thiefHeadingDeg: Float = 0f,
    val policeSpeedMps: Float = 0f,
    val thiefSpeedMps: Float = 10f,
    val gapMeters: Float = 20f,
    val roadLengthMeters: Float = 100f,
    val cameraCenter: Point2D = Point2D(50f, 15f)
) {
    val isFinalLevel: Boolean get() = levelIndex >= totalLevels - 1
    val displayLevelNumber: Int get() = levelIndex + 1
}

