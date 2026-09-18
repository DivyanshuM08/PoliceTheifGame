package com.example.policetheifgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.policetheifgame.game.model.GameStatus
import com.example.policetheifgame.ui.components.GameCanvas
import com.example.policetheifgame.ui.components.GameHud
import com.example.policetheifgame.ui.components.GameOverDialog
import com.example.policetheifgame.ui.components.OnboardingTooltip
import java.util.Locale

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.uiState.collectAsState()
    val showTutorial by viewModel.showTutorial.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Playable Game Canvas (World rendering & gesture handling)
        GameCanvas(
            gameState = gameState,
            roadGeometry = viewModel.gameEngine.roadGeometry,
            onDrag = { screenOffset, viewport ->
                viewModel.onPoliceDrag(screenOffset, viewport)
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Telemetry HUD Overlay (positioned at top with system bars inset)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .align(Alignment.TopCenter)
        ) {
            GameHud(
                gameState = gameState,
                onRestart = { viewModel.restartGame() },
                onOpenTutorial = { viewModel.openTutorial() }
            )
        }

        // 3. Ready / Start Overlay
        if (gameState.status == GameStatus.READY) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x77000000))
                    .systemBarsPadding()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B1B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "LEVEL ${gameState.displayLevelNumber}: ${gameState.levelTitle.uppercase()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ),
                            color = Color(0xFF00E5FF)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "POLICE VS THIEF",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            ),
                            color = Color(0xFF2979FF)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "The suspect is fleeing at ${String.format(Locale.US, "%.1f", gameState.thiefSpeedMps)} m/s!\n\nDrag your police cruiser along the road to chase and intercept them before the 100m finish line.\n\n⚠️ Stay within road boundaries — driving off the road ends the pursuit!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFCFD8DC),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.startGame() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "START CHASE",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 4. Game Over Dialog (Win / Loss with Next Level & Restart)
        GameOverDialog(
            gameState = gameState,
            onRestart = { viewModel.restartGame() },
            onNextLevel = { viewModel.nextLevel() }
        )

        // 5. Onboarding Tutorial Dialog / Popups
        if (showTutorial) {
            OnboardingTooltip(
                onDismiss = { viewModel.dismissTutorial() }
            )
        }
    }
}
