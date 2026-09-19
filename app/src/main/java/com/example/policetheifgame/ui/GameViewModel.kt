package com.example.policetheifgame.ui

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.policetheifgame.game.audio.SirenSoundManager
import com.example.policetheifgame.game.data.GamePreferences
import com.example.policetheifgame.game.engine.GameEngine
import com.example.policetheifgame.game.geometry.GameViewport
import com.example.policetheifgame.game.geometry.LevelData
import com.example.policetheifgame.game.geometry.LevelRepository
import com.example.policetheifgame.game.geometry.RoadGeometry
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import com.example.policetheifgame.game.model.Point2D
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel managing game lifecycle, multi-level progression, persistence, siren audio, and onboarding tutorial.
 */
class GameViewModel @JvmOverloads constructor(
    initialLevelIndex: Int = 0,
    private var preferences: GamePreferences? = null,
    private var sirenManager: SirenSoundManager? = null
) : ViewModel() {

    var currentLevelIndex: Int = (preferences?.currentLevelIndex ?: initialLevelIndex)
        .coerceIn(0, LevelRepository.totalLevels - 1)
        private set

    var unlockedLevelIndex: Int = (preferences?.unlockedLevelIndex ?: currentLevelIndex)
        .coerceIn(0, LevelRepository.totalLevels - 1)
        private set

    private var currentLevel: LevelData = LevelRepository.getLevel(currentLevelIndex)

    val gameEngine = GameEngine(
        roadGeometry = RoadGeometry(currentLevel),
        thiefSpeedMps = currentLevel.thiefSpeedMps,
        initialThiefDistanceMeters = currentLevel.initialGapMeters
    )

    private val _showTutorial = MutableStateFlow(preferences?.hasSeenOnboarding != true)
    val showTutorial: StateFlow<Boolean> = _showTutorial.asStateFlow()

    private val _isSirenMuted = MutableStateFlow(preferences?.isSirenMuted ?: false)
    val isSirenMuted: StateFlow<Boolean> = _isSirenMuted.asStateFlow()

    private val _uiState = MutableStateFlow(createEnrichedSnapshot())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var gameLoopJob: Job? = null
    var isActivelyChasing: Boolean = false
        private set
    private var lastDragTimestampMs: Long = 0L

    init {
        gameEngine.loadLevel(currentLevel, currentLevelIndex)
        publishSnapshot()
    }

    /**
     * Initializes persistence and audio managers from application Context.
     * Safe for Compose runtime and no-op if already injected.
     */
    fun initDependencies(context: Context) {
        var needsSnapshot = false
        if (preferences == null) {
            val prefs = GamePreferences(context.applicationContext)
            preferences = prefs
            _showTutorial.value = !prefs.hasSeenOnboarding
            _isSirenMuted.value = prefs.isSirenMuted

            val savedLevel = prefs.currentLevelIndex.coerceIn(0, LevelRepository.totalLevels - 1)
            if (savedLevel != currentLevelIndex && gameEngine.status == GameStatus.READY) {
                currentLevelIndex = savedLevel
                currentLevel = LevelRepository.getLevel(currentLevelIndex)
                gameEngine.loadLevel(currentLevel, currentLevelIndex)
            }
            if (prefs.unlockedLevelIndex > unlockedLevelIndex) {
                unlockedLevelIndex = prefs.unlockedLevelIndex
            }
            needsSnapshot = true
        }

        if (sirenManager == null) {
            sirenManager = SirenSoundManager(initialMuted = _isSirenMuted.value)
            needsSnapshot = true
        }

        if (needsSnapshot) {
            publishSnapshot()
        }
    }

    fun openTutorial() {
        _showTutorial.value = true
    }

    fun dismissTutorial() {
        _showTutorial.value = false
        preferences?.hasSeenOnboarding = true
    }

    fun toggleMute() {
        val newMuted = sirenManager?.toggleMute() ?: !_isSirenMuted.value
        _isSirenMuted.value = newMuted
        preferences?.isSirenMuted = newMuted
        if (newMuted || !isActivelyChasing || gameEngine.status != GameStatus.PLAYING) {
            sirenManager?.stop()
        } else {
            sirenManager?.play()
        }
        publishSnapshot()
    }

    /**
     * Starts the chase and begins the coroutine update loop.
     * Siren remains stopped until the player actively drags the police car in pursuit.
     */
    fun startGame() {
        gameEngine.start()
        isActivelyChasing = false
        sirenManager?.stop()
        publishSnapshot()
        startGameLoop()
    }

    /**
     * Pauses the active game and stops siren sound.
     */
    fun pauseGame() {
        if (gameEngine.status == GameStatus.PLAYING) {
            gameEngine.pause()
            isActivelyChasing = false
            sirenManager?.stop()
            gameLoopJob?.cancel()
            publishSnapshot()
        }
    }

    /**
     * Resumes a paused game. Siren resumes only when user drags in pursuit.
     */
    fun resumeGame() {
        if (gameEngine.status == GameStatus.PAUSED) {
            gameEngine.resume()
            isActivelyChasing = false
            sirenManager?.stop()
            publishSnapshot()
            startGameLoop()
        }
    }

    /**
     * Restarts the current level from initial positions.
     */
    fun restartGame() {
        gameLoopJob?.cancel()
        isActivelyChasing = false
        sirenManager?.stop()
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
        isActivelyChasing = false
        sirenManager?.stop()
        currentLevelIndex = index.coerceIn(0, LevelRepository.totalLevels - 1)
        preferences?.currentLevelIndex = currentLevelIndex
        currentLevel = LevelRepository.getLevel(currentLevelIndex)
        gameEngine.loadLevel(currentLevel, currentLevelIndex)
        publishSnapshot()
    }

    /**
     * Handles user starting a drag on the police car.
     */
    fun onPoliceDragStart(screenOffset: Offset, viewport: GameViewport) {
        if (gameEngine.status == GameStatus.PAUSED) return
        val worldPoint = viewport.screenToWorld(screenOffset)
        handlePoliceDrag(worldPoint)
    }

    /**
     * Handles user actively dragging the police car on the screen.
     */
    fun onPoliceDrag(screenOffset: Offset, viewport: GameViewport) {
        if (gameEngine.status == GameStatus.PAUSED) return
        val worldPoint = viewport.screenToWorld(screenOffset)
        handlePoliceDrag(worldPoint)
    }

    private fun handlePoliceDrag(worldPoint: Point2D) {
        gameEngine.onPoliceDragged(worldPoint)

        if (gameEngine.status == GameStatus.PLAYING) {
            isActivelyChasing = true
            lastDragTimestampMs = System.currentTimeMillis()
            if (!_isSirenMuted.value) {
                sirenManager?.play()
            }
        } else {
            // e.g. Crashed off-road or caught thief immediately
            isActivelyChasing = false
            sirenManager?.stop()
        }

        publishSnapshot()

        if (gameEngine.status == GameStatus.PLAYING && (gameLoopJob == null || gameLoopJob?.isActive == false)) {
            startGameLoop()
        }
    }

    /**
     * Handles finger release or drag cancellation: immediately stops pursuit siren.
     */
    fun onPoliceDragEnd() {
        isActivelyChasing = false
        sirenManager?.stop()
        publishSnapshot()
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

                // Drag idle detection: if user hasn't dragged for >350ms, the car is idle -> stop siren
                if (isActivelyChasing && (System.currentTimeMillis() - lastDragTimestampMs > 350L)) {
                    isActivelyChasing = false
                    sirenManager?.stop()
                }

                if (gameEngine.status == GameStatus.POLICE_WON || gameEngine.status == GameStatus.THIEF_WON) {
                    isActivelyChasing = false
                    sirenManager?.stop()
                }

                publishSnapshot()
                delay(targetFrameTimeMs)
            }
            isActivelyChasing = false
            sirenManager?.stop()
            publishSnapshot()
        }
    }

    private fun createEnrichedSnapshot(): GameState {
        val base = gameEngine.getSnapshot()
        if (base.status == GameStatus.POLICE_WON) {
            val nextLvl = (currentLevelIndex + 1).coerceAtMost(LevelRepository.totalLevels - 1)
            if (nextLvl > unlockedLevelIndex) {
                unlockedLevelIndex = nextLvl
                preferences?.unlockedLevelIndex = nextLvl
            }
        }
        return base.copy(
            levelIndex = currentLevelIndex,
            levelTitle = currentLevel.title,
            totalLevels = LevelRepository.totalLevels,
            unlockedLevelIndex = unlockedLevelIndex,
            isSirenMuted = _isSirenMuted.value,
            policeSpeedMultiplier = gameEngine.policeSpeedMultiplier,
            isPoliceChasing = isActivelyChasing
        )
    }

    private fun publishSnapshot() {
        _uiState.value = createEnrichedSnapshot()
    }

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
        sirenManager?.release()
        sirenManager = null
    }
}
