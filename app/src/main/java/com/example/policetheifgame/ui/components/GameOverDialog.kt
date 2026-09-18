package com.example.policetheifgame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.policetheifgame.game.model.GameOverReason
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import java.util.Locale

@Composable
fun GameOverDialog(
    gameState: GameState,
    onRestart: () -> Unit
) {
    if (gameState.status != GameStatus.POLICE_WON && gameState.status != GameStatus.THIEF_WON) {
        return
    }

    val isWin = gameState.status == GameStatus.POLICE_WON

    val title = when {
        isWin -> "THIEF CAUGHT!"
        gameState.reason == GameOverReason.OFF_ROAD -> "OFF-ROAD CRASH!"
        gameState.reason == GameOverReason.THIEF_ESCAPED -> "THIEF ESCAPED!"
        else -> "GAME OVER"
    }

    val description = when {
        isWin -> "Brilliant pursuit! You intercepted the getaway vehicle before it reached the finish line."
        gameState.reason == GameOverReason.OFF_ROAD -> "Your patrol car drove off the road boundaries into the ditch! Keep the car strictly on the road."
        gameState.reason == GameOverReason.THIEF_ESCAPED -> "The suspect vehicle reached the 100m finish line and escaped into the city!"
        else -> "Better luck next time!"
    }

    val primaryColor = when {
        isWin -> Color(0xFF00E676)
        gameState.reason == GameOverReason.OFF_ROAD -> Color(0xFFFF1744)
        else -> Color(0xFFFF9100)
    }

    val iconText = when {
        isWin -> "🏆"
        gameState.reason == GameOverReason.OFF_ROAD -> "💥"
        else -> "🚨"
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge with emoji icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFB0BEC5)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF263238))
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Police Distance", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m", gameState.policeDistanceMeters),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Thief Distance", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        Text(
                            String.format(Locale.US, "%.1f m", gameState.thiefDistanceMeters),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "RESTART CHASE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
