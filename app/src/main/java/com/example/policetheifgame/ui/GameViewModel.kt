package com.example.policetheifgame.ui

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.policetheifgame.game.engine.GameEngine
import com.example.policetheifgame.game.geometry.GameViewport
import com.example.policetheifgame.game.geometry.LevelData
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
 * ViewModel managing the Police vs Thief game lifecycle and state exposure.
 */
class GameViewModel @JvmOverloads constructor(
    initialLevel: LevelData = LevelData.createDefaultLevel1()
) : ViewModel() {

    private val roadGeometry = RoadGeometry(initialLevel)
    val gameEngine = GameEngine(roadGeometry)

    private val _uiState = MutableStateFlow(gameEngine.getSnapshot())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var gameLoopJob: Job? = null

    init {
        _uiState.value = gameEngine.getSnapshot()
    }

    /**
     * Starts the game and begins the coroutine update loop.
     */
    fun startGame() {
        gameEngine.start()
        _uiState.value = gameEngine.getSnapshot()
        startGameLoop()
    }

    /**
     * Restarts the game from initial positions.
     */
    fun restartGame() {
        gameLoopJob?.cancel()
        gameEngine.restart()
        _uiState.value = gameEngine.getSnapshot()
        startGameLoop()
    }

    /**
     * Handles user dragging the police car on the screen.
     */
    fun onPoliceDrag(screenOffset: Offset, viewport: GameViewport) {
        val worldPoint = viewport.screenToWorld(screenOffset)
        gameEngine.onPoliceDragged(worldPoint)
        _uiState.value = gameEngine.getSnapshot()

        // Ensure game loop is active if drag started the game
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
                _uiState.value = gameEngine.getSnapshot()

                delay(targetFrameTimeMs)
            }
            // Emit final state when loop exits
            _uiState.value = gameEngine.getSnapshot()
        }
    }

    /**
     * Loads a new level geometry without changing game logic.
     */
    fun loadLevel(levelData: LevelData) {
        gameLoopJob?.cancel()
        val newGeometry = RoadGeometry(levelData)
        gameEngine.roadGeometry = newGeometry
        gameEngine.reset()
        _uiState.value = gameEngine.getSnapshot()
    }

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
    }
}
