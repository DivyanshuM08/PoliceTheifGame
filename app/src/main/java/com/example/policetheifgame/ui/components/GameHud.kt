package com.example.policetheifgame.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import java.util.Locale

@Composable
fun GameHud(
    gameState: GameState,
    onRestart: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xDD121212) // Translucent dark surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0x33FFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Status bar row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when (gameState.status) {
                                    GameStatus.READY -> Color(0xFFFFA000)
                                    GameStatus.PLAYING -> Color(0xFF00E676)
                                    GameStatus.POLICE_WON -> Color(0xFF2979FF)
                                    GameStatus.THIEF_WON -> Color(0xFFFF1744)
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (gameState.status) {
                            GameStatus.READY -> "READY - TAP START CHASE"
                            GameStatus.PLAYING -> "PURSUIT IN PROGRESS"
                            GameStatus.POLICE_WON -> "THIEF CAUGHT!"
                            GameStatus.THIEF_WON -> "PURSUIT FAILED"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (gameState.status == GameStatus.PLAYING && onRestart != null) {
                        TextButton(
                            onClick = onRestart,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF9800)),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = "↺ RESET",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        Text(
                            text = "SPEED: 10 m/s",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFFB0BEC5)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Distance Telemetry
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryItem(
                    label = "POLICE",
                    value = String.format(Locale.US, "%.1f m", gameState.policeDistanceMeters),
                    accentColor = Color(0xFF2979FF)
                )

                TelemetryItem(
                    label = "GAP",
                    value = String.format(Locale.US, "%.1f m", gameState.gapMeters),
                    accentColor = Color(0xFFFFD600)
                )

                TelemetryItem(
                    label = "THIEF",
                    value = String.format(Locale.US, "%.1f m", gameState.thiefDistanceMeters),
                    accentColor = Color(0xFFFF3D00)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mini Track Progress Bar (0 to 100m) - Rendered safely with Canvas
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

                    // 1. Track Base
                    drawRoundRect(
                        color = Color(0xFF263238),
                        cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                    )

                    // 2. Thief pursuit track segment
                    if (thiefProgress > 0f) {
                        drawRoundRect(
                            color = Color(0x44FF3D00),
                            size = Size(barWidth * thiefProgress, barHeight),
                            cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                        )
                    }

                    // 3. Police pursuit track segment
                    if (policeProgress > 0f) {
                        drawRoundRect(
                            color = Color(0x662979FF),
                            size = Size(barWidth * policeProgress, barHeight),
                            cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
                        )
                    }

                    // 4. Finish line flag at far right
                    drawLine(
                        color = Color.White,
                        start = Offset(barWidth - 4f, 2f),
                        end = Offset(barWidth - 4f, barHeight - 2f),
                        strokeWidth = 3f
                    )

                    // 5. Thief Marker Pin (Red with white border)
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

                    // 6. Police Marker Pin (Blue with white border)
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
        }
    }
}

@Composable
private fun TelemetryItem(
    label: String,
    value: String,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = accentColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color.White
        )
    }
}
