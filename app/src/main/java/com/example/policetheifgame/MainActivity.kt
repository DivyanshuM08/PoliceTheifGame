package com.example.policetheifgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.policetheifgame.ui.AppScreen
import com.example.policetheifgame.ui.GameScreen
import com.example.policetheifgame.ui.GameViewModel
import com.example.policetheifgame.ui.LevelMapScreen
import com.example.policetheifgame.ui.theme.PoliceTheifGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoliceTheifGameTheme {
                val viewModel: GameViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()

                // Intercept back button when in gameplay to smoothly return to the Level Map
                BackHandler(enabled = (currentScreen == AppScreen.GAMEPLAY)) {
                    viewModel.returnToLevelMap()
                }

                Crossfade(
                    targetState = currentScreen,
                    animationSpec = tween(300),
                    label = "screen_crossfade"
                ) { screen ->
                    when (screen) {
                        AppScreen.LEVEL_MAP -> {
                            LevelMapScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppScreen.GAMEPLAY -> {
                            GameScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}