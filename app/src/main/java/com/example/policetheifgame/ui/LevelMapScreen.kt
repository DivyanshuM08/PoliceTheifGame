package com.example.policetheifgame.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.policetheifgame.game.geometry.LevelData
import com.example.policetheifgame.game.geometry.LevelRepository
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Candy Crush-style progression ladder displaying all 20 levels.
 *
 * Visual Features:
 * - Winding asphalt road ascending vertically from Level 1 at the bottom to Level 20 at the top.
 * - Completed levels (golden badge, stars, unlocked and replayable).
 * - Current level (pulsing neon cruiser badge, "YOU ARE HERE" tag, auto-centered).
 * - Locked levels (dark badge, padlock 🔒 icon, cannot be entered until previous is cleared).
 * - Mission briefing dialog on tapping any unlocked level.
 */
@Composable
fun LevelMapScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initDependencies(context)
    }

    val gameState by viewModel.uiState.collectAsState()
    val unlockedLevelIndex = gameState.unlockedLevelIndex
    val totalLevels = LevelRepository.totalLevels

    var selectedLevelForBriefing by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    // Row height in dp for each level on the ladder
    val rowHeightDp = 120.dp
    val topPaddingDp = 180.dp
    val bottomPaddingDp = 120.dp
    val totalContentHeightDp = topPaddingDp + (rowHeightDp * totalLevels) + bottomPaddingDp

    // Auto-scroll to current unlocked level on initial display
    LaunchedEffect(unlockedLevelIndex) {
        val targetRowFromTop = (totalLevels - 1) - unlockedLevelIndex
        val targetYPx = with(density) {
            (topPaddingDp + (rowHeightDp * targetRowFromTop) - 200.dp).toPx().coerceAtLeast(0f)
        }
        scrollState.animateScrollTo(targetYPx.roundToInt())
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Dynamic, animated atmospheric background (Cyber Skyline, Searchlights & Glowing Particles)
        AnimatedLevelMapBackground(modifier = Modifier.fillMaxSize())

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidthPx = constraints.maxWidth.toFloat()
            val rowHeightPx = with(density) { rowHeightDp.toPx() }
            val topPaddingPx = with(density) { topPaddingDp.toPx() }
            val totalHeightPx = with(density) { totalContentHeightDp.toPx() }

            // Calculate (X, Y) coordinates for all 20 nodes
            // Level 20 is at index 19 (near top, row 0), Level 1 is at index 0 (near bottom, row 19)
            val nodeCoords = remember(screenWidthPx, rowHeightPx, topPaddingPx) {
                (0 until totalLevels).map { levelIdx ->
                    val rowFromTop = (totalLevels - 1) - levelIdx
                    val y = topPaddingPx + (rowFromTop * rowHeightPx) + (rowHeightPx / 2f)
                    val levelNumber = levelIdx + 1
                    // Serpentine sine wave horizontal position between 22% and 78% screen width
                    val xFraction = 0.5f + 0.28f * sin(levelNumber * 0.85f)
                    val x = screenWidthPx * xFraction
                    Offset(x, y)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .height(totalContentHeightDp)
            ) {
                // 1. Serpentine Winding Road Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(totalContentHeightDp)
                ) {
                    val roadPath = Path()
                    if (nodeCoords.isNotEmpty()) {
                        roadPath.moveTo(nodeCoords[0].x, nodeCoords[0].y)
                        for (i in 0 until nodeCoords.size - 1) {
                            val p1 = nodeCoords[i]
                            val p2 = nodeCoords[i + 1]
                            val midY = (p1.y + p2.y) / 2f
                            // Smooth S-curve transition between nodes
                            roadPath.cubicTo(
                                p1.x, midY,
                                p2.x, midY,
                                p2.x, p2.y
                            )
                        }
                    }

                    // Road Border / Shoulder
                    drawPath(
                        path = roadPath,
                        color = Color(0xFF455A64),
                        style = Stroke(
                            width = with(density) { 46.dp.toPx() },
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // Road Surface (Dark Asphalt)
                    drawPath(
                        path = roadPath,
                        color = Color(0xFF212121),
                        style = Stroke(
                            width = with(density) { 38.dp.toPx() },
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // Road Center Dashed Line (Bright Yellow)
                    drawPath(
                        path = roadPath,
                        color = Color(0xFFFFD54F),
                        style = Stroke(
                            width = with(density) { 3.dp.toPx() },
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
                        )
                    )
                }

                // 2. Interactive Level Nodes
                nodeCoords.forEachIndexed { levelIdx, coord ->
                    val isUnlocked = levelIdx <= unlockedLevelIndex
                    val isCurrent = levelIdx == unlockedLevelIndex
                    val isCompleted = levelIdx < unlockedLevelIndex
                    val nodeSize = 72.dp
                    val halfSizePx = with(density) { (nodeSize / 2).toPx() }

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (coord.x - halfSizePx).roundToInt(),
                                    y = (coord.y - halfSizePx).roundToInt()
                                )
                            }
                            .size(nodeSize)
                    ) {
                        LadderLevelNode(
                            levelIndex = levelIdx,
                            isUnlocked = isUnlocked,
                            isCurrent = isCurrent,
                            isCompleted = isCompleted,
                            onClick = {
                                if (isUnlocked) {
                                    selectedLevelForBriefing = levelIdx
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "🔒 Level ${levelIdx + 1} is locked! Clear Level $levelIdx first.",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // 3. Fixed Top Header (Academy Status & Telemetry)
        LevelMapHeader(
            unlockedCount = (unlockedLevelIndex + 1).coerceAtMost(totalLevels),
            totalLevels = totalLevels,
            isMuted = gameState.isSirenMuted,
            onToggleMute = { viewModel.toggleMute() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .systemBarsPadding()
        )

        // 4. Mission Briefing Dialog (Candy Crush style pop-up when tapping an unlocked level)
        selectedLevelForBriefing?.let { levelIdx ->
            val levelData = LevelRepository.getLevel(levelIdx)
            MissionBriefingDialog(
                levelIndex = levelIdx,
                levelData = levelData,
                isCompleted = levelIdx < unlockedLevelIndex,
                onDismiss = { selectedLevelForBriefing = null },
                onStartPlay = {
                    selectedLevelForBriefing = null
                    viewModel.openLevelFromMap(levelIdx)
                }
            )
        }

        // Snackbar for locked level feedback
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

/**
 * Individual circular Level Node along the Candy Crush ladder.
 */
@Composable
private fun LadderLevelNode(
    levelIndex: Int,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val levelNumber = levelIndex + 1

    // Pulsing animation for the current active level
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val currentScale = if (isCurrent) pulseScale else 1.0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scale(currentScale),
        contentAlignment = Alignment.Center
    ) {
        // "YOU ARE HERE" Banner floating above current level node
        if (isCurrent) {
            Box(
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .offset(y = (-40).dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2979FF), Color(0xFF00E5FF))
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚔 ", fontSize = 11.sp)
                    Text(
                        text = "HERE",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Main Node Circle
        val backgroundBrush = when {
            isCurrent -> Brush.radialGradient(
                listOf(Color(0xFF00E5FF), Color(0xFF2979FF))
            )
            isCompleted -> Brush.radialGradient(
                listOf(Color(0xFFFFD54F), Color(0xFFFF8F00))
            )
            else -> Brush.radialGradient(
                listOf(Color(0xFF2E3842), Color(0xFF1B2026))
            )
        }

        val borderColor = when {
            isCurrent -> Color(0xFFFFFFFF)
            isCompleted -> Color(0xFFFFF9C4)
            else -> Color(0xFF455A64)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(if (isUnlocked) 8.dp else 2.dp, CircleShape)
                .clip(CircleShape)
                .background(backgroundBrush)
                .border(3.dp, borderColor, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$levelNumber",
                        color = if (isCurrent) Color.Black else Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )

                    // 3 Completion Stars for cleared levels
                    if (isCompleted) {
                        Text(
                            text = "⭐⭐⭐",
                            fontSize = 8.sp,
                            lineHeight = 8.sp
                        )
                    }
                }
            } else {
                // Locked State
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🔒", fontSize = 20.sp)
                    Text(
                        text = "$levelNumber",
                        color = Color(0xFF78909C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Fixed header at the top of the Level Map screen.
 */
@Composable
private fun LevelMapHeader(
    unlockedCount: Int,
    totalLevels: Int,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xDD12161A),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3300E5FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "POLICE PATROL ACADEMY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = Color(0xFF00E5FF)
                    )
                    Text(
                        text = "Cleared $unlockedCount of $totalLevels Courses",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCFD8DC)
                    )
                }

                // Audio Mute toggle button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF263238))
                        .border(
                            1.dp,
                            if (isMuted) Color(0xFFFF5252) else Color(0x6600E5FF),
                            CircleShape
                        )
                ) {
                    TextButton(
                        onClick = onToggleMute,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = if (isMuted) "🔇" else "🔊",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progression Bar
            val progress = (unlockedCount.toFloat() / totalLevels.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF00E5FF),
                trackColor = Color(0xFF37474F),
            )
        }
    }
}

/**
 * Mission briefing modal dialog when player taps an unlocked level.
 */
@Composable
private fun MissionBriefingDialog(
    levelIndex: Int,
    levelData: LevelData,
    isCompleted: Boolean,
    onDismiss: () -> Unit,
    onStartPlay: () -> Unit
) {
    val levelNumber = levelIndex + 1
    val isPuzzle = levelData.isPuzzle

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D20))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(if (isPuzzle) Color(0x337C4DFF) else Color(0x3300E5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPuzzle) "🧩" else "🏎️",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "LEVEL $levelNumber",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = if (isPuzzle) Color(0xFFB388FF) else Color(0xFF00E5FF)
                )

                Text(
                    text = levelData.title.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isPuzzle) "High-Complexity Labyrinth Maze" else "High-Speed Highway Pursuit",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Intel Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF263238),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Speed", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                            Text(
                                String.format(Locale.US, "%.1f m/s", levelData.thiefSpeedMps),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFFF5252)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isPuzzle) "Corridors" else "Track Length",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF90A4AE)
                            )
                            Text(
                                text = if (isPuzzle) "${levelData.corridors.size}" else "${levelData.roadLengthMeters.toInt()}m",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF00E5FF)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Status", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                            Text(
                                text = if (isCompleted) "CLEARED ⭐" else "CURRENT 🚨",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isCompleted) Color(0xFFFFD54F) else Color(0xFF2979FF)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action button (Play / Replay)
                Button(
                    onClick = onStartPlay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) Color(0xFFFF9100) else Color(0xFF2979FF)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isCompleted) "REPLAY CHASE ▶" else "START CHASE ▶",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE", color = Color(0xFFB0BEC5))
                }
            }
        }
    }
}

/**
 * Dynamic animated background for the Level Map ladder.
 *
 * Features:
 * - Deep vertical atmosphere gradient from Cyber Midnight to Outskirts Forest
 * - Distant Cyber City skyline silhouette with glowing illuminated windows
 * - Rolling terrain ridge in the lower half
 * - Two sweeping volumetric police searchlights (simulating patrol helicopters scanning the city)
 * - 32 drifting ambient light motes / city bokeh particles floating upward with soft sinusoidal drift
 * - Subtle ambient pulsing horizon glow
 */
@Composable
fun AnimatedLevelMapBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "map_bg_anim")

    // Slow sweeping police searchlight 1 (from top-left)
    val searchlightAngle1 by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "searchlight1"
    )

    // Slow sweeping police searchlight 2 (from top-right)
    val searchlightAngle2 by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(8500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "searchlight2"
    )

    // Continuous floating particle time
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleProgress"
    )

    // Breathing glow for ambient horizon
    val horizonGlow by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "horizonGlow"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Rich Deep Sky & Sector Terrain Vertical Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF070B14), // Cyber Midnight Sky (Top / Maze levels)
                    Color(0xFF0C1626), // Deep Indigo City Atmosphere
                    Color(0xFF11242E), // Mid-sky Teal / Slate
                    Color(0xFF0F261B), // Dark Forest Outskirts (Bottom / Linear levels)
                    Color(0xFF08140E)  // Deep Terrain Floor
                ),
                startY = 0f,
                endY = h
            )
        )

        // 2. Distant Cyber City Skyline Silhouettes (in upper half of screen)
        val skylineBaseY = h * 0.45f
        val buildingPath = Path()
        buildingPath.moveTo(0f, skylineBaseY)

        val buildingWidths = floatArrayOf(
            w * 0.12f, w * 0.08f, w * 0.14f, w * 0.10f, w * 0.16f,
            w * 0.09f, w * 0.15f, w * 0.10f, w * 0.12f
        )
        val buildingHeights = floatArrayOf(
            110f, 160f, 130f, 200f, 140f, 180f, 120f, 170f, 100f
        )

        var curX = 0f
        for (idx in buildingWidths.indices) {
            val bW = buildingWidths[idx]
            val bH = buildingHeights[idx]
            val topY = skylineBaseY - bH
            buildingPath.lineTo(curX, topY)
            buildingPath.lineTo(curX + bW, topY)
            curX += bW
        }
        buildingPath.lineTo(w, skylineBaseY)
        buildingPath.lineTo(w, h)
        buildingPath.lineTo(0f, h)
        buildingPath.close()

        drawPath(
            path = buildingPath,
            color = Color(0x33000000)
        )

        // Draw scattered tiny glowing skyline windows
        curX = 0f
        for (idx in buildingWidths.indices) {
            val bW = buildingWidths[idx]
            val bH = buildingHeights[idx]
            val topY = skylineBaseY - bH
            val numWindows = 3
            for (winRow in 1..numWindows) {
                val winY = topY + (winRow * (bH / (numWindows + 1)))
                val winX1 = curX + bW * 0.3f
                val winX2 = curX + bW * 0.7f
                val winColor = if ((idx + winRow) % 3 == 0) Color(0x6600E5FF) else Color(0x55FFD54F)
                drawCircle(color = winColor, radius = 2.5f, center = Offset(winX1, winY))
                drawCircle(color = winColor, radius = 2.5f, center = Offset(winX2, winY))
            }
            curX += bW
        }

        // 3. Distant Rolling Terrain Ridge (Lower half)
        val ridgePath = Path()
        val ridgeBaseY = h * 0.65f
        ridgePath.moveTo(0f, ridgeBaseY)
        ridgePath.cubicTo(
            w * 0.25f, ridgeBaseY - 40f,
            w * 0.40f, ridgeBaseY + 30f,
            w * 0.70f, ridgeBaseY - 35f
        )
        ridgePath.cubicTo(
            w * 0.85f, ridgeBaseY - 60f,
            w * 0.95f, ridgeBaseY - 10f,
            w, ridgeBaseY
        )
        ridgePath.lineTo(w, h)
        ridgePath.lineTo(0f, h)
        ridgePath.close()

        drawPath(
            path = ridgePath,
            color = Color(0x220A1F13)
        )

        // 4. Volumetric Police Searchlight 1 (sweeps from top-left)
        val beam1Origin = Offset(w * 0.15f, -20f)
        val beam1Length = h * 0.85f
        val rad1 = Math.toRadians((90.0 + searchlightAngle1.toDouble()))
        val dir1X = cos(rad1).toFloat()
        val dir1Y = sin(rad1).toFloat()
        val beam1Center = Offset(beam1Origin.x + dir1X * beam1Length, beam1Origin.y + dir1Y * beam1Length)
        val beam1Spread = 160f
        val perp1X = -dir1Y
        val perp1Y = dir1X

        val beam1Path = Path().apply {
            moveTo(beam1Origin.x, beam1Origin.y)
            lineTo(beam1Center.x + perp1X * beam1Spread, beam1Center.y + perp1Y * beam1Spread)
            lineTo(beam1Center.x - perp1X * beam1Spread, beam1Center.y - perp1Y * beam1Spread)
            close()
        }

        drawPath(
            path = beam1Path,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x3300E5FF),
                    Color(0x1800E5FF),
                    Color.Transparent
                ),
                center = beam1Origin,
                radius = beam1Length
            )
        )

        // 5. Volumetric Police Searchlight 2 (sweeps from top-right)
        val beam2Origin = Offset(w * 0.85f, -20f)
        val beam2Length = h * 0.80f
        val rad2 = Math.toRadians((90.0 + searchlightAngle2.toDouble()))
        val dir2X = cos(rad2).toFloat()
        val dir2Y = sin(rad2).toFloat()
        val beam2Center = Offset(beam2Origin.x + dir2X * beam2Length, beam2Origin.y + dir2Y * beam2Length)
        val beam2Spread = 180f
        val perp2X = -dir2Y
        val perp2Y = dir2X

        val beam2Path = Path().apply {
            moveTo(beam2Origin.x, beam2Origin.y)
            lineTo(beam2Center.x + perp2X * beam2Spread, beam2Center.y + perp2Y * beam2Spread)
            lineTo(beam2Center.x - perp2X * beam2Spread, beam2Center.y - perp2Y * beam2Spread)
            close()
        }

        drawPath(
            path = beam2Path,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x282979FF),
                    Color(0x102979FF),
                    Color.Transparent
                ),
                center = beam2Origin,
                radius = beam2Length
            )
        )

        // 6. Floating Light Motes / City Bokeh (32 particles)
        val numParticles = 32
        for (i in 0 until numParticles) {
            val seed = i * 37 + 11
            val speedFactor = 0.6f + ((seed % 7) * 0.12f)
            val pProgress = (particleProgress * speedFactor + (i.toFloat() / numParticles)) % 1.0f

            val pY = (1.0f - pProgress) * h
            val baseX = ((seed * 19) % 1000) / 1000f * w
            val waveX = sin((pProgress * 4 * PI + i).toFloat()) * 24f
            val pX = (baseX + waveX).coerceIn(0f, w)

            val baseRadius = 2.0f + (seed % 4) * 1.2f
            val alphaFade = when {
                pProgress < 0.15f -> pProgress / 0.15f
                pProgress > 0.85f -> (1.0f - pProgress) / 0.15f
                else -> 1.0f
            }

            val pColor = if (i % 3 == 0) {
                Color(0xFF00E5FF).copy(alpha = 0.45f * alphaFade)
            } else if (i % 3 == 1) {
                Color(0xFFFFD54F).copy(alpha = 0.35f * alphaFade)
            } else {
                Color(0xFF80D8FF).copy(alpha = 0.40f * alphaFade)
            }

            drawCircle(
                color = pColor,
                radius = baseRadius,
                center = Offset(pX, pY)
            )
        }

        // 7. Ambient Horizon Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00E5FF).copy(alpha = 0.08f * horizonGlow),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}
