package com.example.policetheifgame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus

/**
 * Minimalist floating header showing only Info (ℹ️), Mute (🔊/🔇), and Pause (⏸) icons.
 * Leaves the entire screen unobstructed for the road and chase.
 */
@Composable
fun GameHud(
    gameState: GameState,
    onPause: () -> Unit,
    onOpenInfo: () -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Info button (ℹ️)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xCC1A1C1E))
                .border(1.5.dp, Color(0x6600E5FF), CircleShape)
        ) {
            TextButton(
                onClick = onOpenInfo,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "ℹ️",
                    fontSize = 20.sp
                )
            }
        }

        // Right side: Sound Mute toggle & Pause button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Sound Mute / Unmute Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xCC1A1C1E))
                    .border(
                        1.5.dp,
                        if (gameState.isSirenMuted) Color(0x66FF5252) else Color(0x44FFFFFF),
                        CircleShape
                    )
            ) {
                TextButton(
                    onClick = onToggleMute,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = if (gameState.isSirenMuted) "🔇" else "🔊",
                        fontSize = 18.sp
                    )
                }
            }

            // Pause Button (Only enabled when playing or ready)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xCC1A1C1E))
                    .border(1.5.dp, Color(0x6629B6F6), CircleShape)
            ) {
                TextButton(
                    onClick = onPause,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "⏸",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF29B6F6)
                    )
                }
            }
        }
    }
}
