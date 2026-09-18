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
        publishSnapshot()
    }

    /**
     * Starts the chase and begins the coroutine update loop and siren audio.
     */
    fun startGame() {
        gameEngine.start()
        sirenManager?.play()
        publishSnapshot()
        startGameLoop()
    }

    /**
     * Pauses the active game and siren sound.
     */
    fun pauseGame() {
        if (gameEngine.status == GameStatus.PLAYING) {
            gameEngine.pause()
            sirenManager?.pause()
            gameLoopJob?.cancel()
            publishSnapshot()
        }
    }

    /**
     * Resumes a paused game and restores siren sound.
     */
    fun resumeGame() {
        if (gameEngine.status == GameStatus.PAUSED) {
            gameEngine.resume()
            sirenManager?.play()
            publishSnapshot()
            startGameLoop()
        }
    }

    /**
     * Restarts the current level from initial positions.
     */
    fun restartGame() {
        gameLoopJob?.cancel()
        gameEngine.restart()
        sirenManager?.play()
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
        sirenManager?.stop()
        currentLevelIndex = index.coerceIn(0, LevelRepository.totalLevels - 1)
        preferences?.currentLevelIndex = currentLevelIndex
        currentLevel = LevelRepository.getLevel(currentLevelIndex)
        gameEngine.loadLevel(currentLevel, currentLevelIndex)
        publishSnapshot()
    }

    /**
     * Handles user dragging the police car on the screen.
     */
    fun onPoliceDrag(screenOffset: Offset, viewport: GameViewport) {
        if (gameEngine.status == GameStatus.PAUSED) return

        val wasReady = gameEngine.status == GameStatus.READY
        val worldPoint = viewport.screenToWorld(screenOffset)
        gameEngine.onPoliceDragged(worldPoint)

        if (wasReady && gameEngine.status == GameStatus.PLAYING) {
            sirenManager?.play()
        }

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

                if (gameEngine.status == GameStatus.POLICE_WON || gameEngine.status == GameStatus.THIEF_WON) {
                    sirenManager?.stop()
                }

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
                preferences?.unlockedLevelIndex = nextLvl
            }
        }
        return base.copy(
            levelIndex = currentLevelIndex,
            levelTitle = currentLevel.title,
            totalLevels = LevelRepository.totalLevels,
            unlockedLevelIndex = unlockedLevelIndex,
            isSirenMuted = _isSirenMuted.value,
            policeSpeedMultiplier = gameEngine.policeSpeedMultiplier
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
