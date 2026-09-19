package com.example.policetheifgame.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.policetheifgame.game.model.GameState
import java.util.Locale

/**
 * Modal dialog displayed when the player taps the Info (ℹ️) icon.
 * Presents course telemetry, thief speed stats, track progress, and tutorial controls.
 */
@Composable
fun InfoDialog(
    gameState: GameState,
    onDismiss: () -> Unit,
    onOpenTutorial: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16181A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E5FF).copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "ℹ️", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "PURSUIT INTEL",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = Color(0xFF00E5FF)
                )

                Text(
                    text = "LEVEL ${gameState.displayLevelNumber}/${gameState.totalLevels} • ${gameState.levelTitle.uppercase()}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFFECEFF1)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF212529))
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("POLICE", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m", gameState.policeDistanceMeters),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF2979FF)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("GAP", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m", gameState.gapMeters),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFFD54F)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("THIEF", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m", gameState.thiefDistanceMeters),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFF3D00)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("THIEF SPEED", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m/s", gameState.thiefSpeedMps),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFF8A80)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mini Track Progress Bar (0 to 100m)
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                ) {
                    val totalLength = gameState.roadLengthMeters.coerceAtLeast(1f)
                    val policeProgress = (gameState.policeDistanceMeters / totalLength).coerceIn(0f, 1f)
                    val thiefProgress = (gameState.thiefDistanceMeters / totalLength).coerceIn(0f, 1f)

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barWidth = size.width
                        val barHeight = size.height

                        // Track Base
                        drawRoundRect(
                            color = Color(0xFF263238),
                            cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                        )

                        // Thief progress track
                        if (thiefProgress > 0f) {
                            drawRoundRect(
                                color = Color(0x44FF3D00),
                                size = Size(barWidth * thiefProgress, barHeight),
                                cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                            )
                        }

                        // Police progress track
                        if (policeProgress > 0f) {
                            drawRoundRect(
                                color = Color(0x662979FF),
                                size = Size(barWidth * policeProgress, barHeight),
                                cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                            )
                        }

                        // Finish line flag at far right
                        drawLine(
                            color = Color.White,
                            start = Offset(barWidth - 4f, 2f),
                            end = Offset(barWidth - 4f, barHeight - 2f),
                            strokeWidth = 3f
                        )

                        // Thief Pin
                        val thiefX = (barWidth * thiefProgress).coerceIn(8f, barWidth - 8f)
                        drawCircle(
                            color = Color(0xFFFF3D00),
                            radius = 6.5f,
                            center = Offset(thiefX, barHeight / 2f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 6.5f,
                            center = Offset(thiefX, barHeight / 2f),
                            style = Stroke(width = 2f)
                        )

                        // Police Pin
                        val policeX = (barWidth * policeProgress).coerceIn(8f, barWidth - 8f)
                        drawCircle(
                            color = Color(0xFF2979FF),
                            radius = 7.5f,
                            center = Offset(policeX, barHeight / 2f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 7.5f,
                            center = Offset(policeX, barHeight / 2f),
                            style = Stroke(width = 2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (gameState.isPuzzle)
                        "Controls: Drag your police cruiser through the maze corridors to corner the suspect before they escape through the checkered exit gate! You can make U-turns and take shortcuts.\n\n⚠️ Stay inside corridor pathways — colliding with maze walls will crash your cruiser!"
                    else
                        "Controls: Drag your police cruiser directly along the road to chase the thief. Police speed matches your finger dragging speed.\n\n⚠️ Stay within road boundaries — driving into the grass will fail the chase!",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFB0BEC5),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenTutorial()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("📖 How to Play (Tutorial)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "BACK TO CHASE ❯",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
