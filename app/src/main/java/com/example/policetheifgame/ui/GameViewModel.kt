package com.example.policetheifgame.ui

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.policetheifgame.game.engine.GameEngine
import com.example.policetheifgame.game.geometry.GameViewport
import com.example.policetheifgame.game.geometry.LevelData
import com.example.policetheifgame.game.geometry.LevelRepository
import com.example.policetheifgame.game.geometry.RoadGeometry
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel managing game lifecycle, multi-level progression, and onboarding tutorial.
 */
class GameViewModel @JvmOverloads constructor(
    initialLevelIndex: Int = 0
) : ViewModel() {

    var currentLevelIndex: Int = initialLevelIndex.coerceIn(0, LevelRepository.totalLevels - 1)
        private set

    var unlockedLevelIndex: Int = currentLevelIndex
        private set

    private var currentLevel: LevelData = LevelRepository.getLevel(currentLevelIndex)

    val gameEngine = GameEngine(
        roadGeometry = RoadGeometry(currentLevel),
        thiefSpeedMps = currentLevel.thiefSpeedMps,
        initialThiefDistanceMeters = currentLevel.initialGapMeters
    )

    private val _uiState = MutableStateFlow(createEnrichedSnapshot())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val _showTutorial = MutableStateFlow(true) // Show onboarding on first land
    val showTutorial: StateFlow<Boolean> = _showTutorial.asStateFlow()

    private var gameLoopJob: Job? = null

    init {
        publishSnapshot()
    }

    fun openTutorial() {
        _showTutorial.value = true
    }

    fun dismissTutorial() {
        _showTutorial.value = false
    }

    /**
     * Starts the chase and begins the coroutine update loop.
     */
    fun startGame() {
        gameEngine.start()
        publishSnapshot()
        startGameLoop()
    }

    /**
     * Restarts the current level from initial positions.
     */
    fun restartGame() {
        gameLoopJob?.cancel()
        gameEngine.restart()
        publishSnapshot()
        startGameLoop()
    }

    /**
     * Advances to the next level.
     */
    fun nextLevel() {
        if (currentLevelIndex < LevelRepository.totalLevels - 1) {
            selectLevel(currentLevelIndex + 1)
        } else {
            // Replay from level 1 or restart final
            restartGame()
        }
    }

    /**
     * Selects and loads an unlocked level.
     */
    fun selectLevel(index: Int) {
        gameLoopJob?.cancel()
        currentLevelIndex = index.coerceIn(0, LevelRepository.totalLevels - 1)
        currentLevel = LevelRepository.getLevel(currentLevelIndex)
        gameEngine.loadLevel(currentLevel)
        publishSnapshot()
    }

    /**
     * Handles user dragging the police car on the screen.
     */
    fun onPoliceDrag(screenOffset: Offset, viewport: GameViewport) {
        val worldPoint = viewport.screenToWorld(screenOffset)
        gameEngine.onPoliceDragged(worldPoint)
        publishSnapshot()

        if (gameEngine.status == GameStatus.PLAYING && (gameLoopJob == null || gameLoopJob?.isActive == false)) {
            startGameLoop()
        }
    }

    private fun startGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = viewModelScope.launch(Dispatchers.Default) {
            var lastTimeNanos = System.nanoTime()
            val targetFrameTimeMs = 16L // ~60 FPS

            while (isActive && gameEngine.status == GameStatus.PLAYING) {
                val nowNanos = System.nanoTime()
                val deltaSeconds = ((nowNanos - lastTimeNanos) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastTimeNanos = nowNanos

                gameEngine.tick(deltaSeconds)
                publishSnapshot()

                delay(targetFrameTimeMs)
            }
            publishSnapshot()
        }
    }

    private fun createEnrichedSnapshot(): GameState {
        val base = gameEngine.getSnapshot()
        if (base.status == GameStatus.POLICE_WON) {
            val nextLvl = (currentLevelIndex + 1).coerceAtMost(LevelRepository.totalLevels - 1)
            if (nextLvl > unlockedLevelIndex) {
                unlockedLevelIndex = nextLvl
            }
        }
        return base.copy(
            levelIndex = currentLevelIndex,
            levelTitle = currentLevel.title,
            totalLevels = LevelRepository.totalLevels,
            unlockedLevelIndex = unlockedLevelIndex
        )
    }

    private fun publishSnapshot() {
        _uiState.value = createEnrichedSnapshot()
    }

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
    }
}
