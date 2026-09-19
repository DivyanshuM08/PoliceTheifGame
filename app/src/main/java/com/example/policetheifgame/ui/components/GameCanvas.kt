package com.example.policetheifgame.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import com.example.policetheifgame.game.geometry.GameViewport
import com.example.policetheifgame.game.geometry.RoadGeometry
import com.example.policetheifgame.game.model.GameState
import com.example.policetheifgame.game.model.GameStatus
import com.example.policetheifgame.game.model.Point2D

@Composable
fun GameCanvas(
    gameState: GameState,
    roadGeometry: RoadGeometry,
    onDrag: (Offset, GameViewport) -> Unit,
    onDragStart: (Offset, GameViewport) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Pulsing animation for siren and drag ring
    val transition = rememberInfiniteTransition(label = "siren_pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val sirenFlip by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "siren_flip"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(gameState.status) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        val viewport = GameViewport.createOverview(
                            screenSize = Size(size.width.toFloat(), size.height.toFloat()),
                            roadGeometry = roadGeometry
                        )
                        onDragStart(startOffset, viewport)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val viewport = GameViewport.createOverview(
                            screenSize = Size(size.width.toFloat(), size.height.toFloat()),
                            roadGeometry = roadGeometry
                        )
                        onDrag(change.position, viewport)
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDragCancel = {
                        onDragEnd()
                    }
                )
            }
    ) {
        // Create static overview viewport showing the entire road (0 to 100m) at once
        val viewport = GameViewport.createOverview(
            screenSize = size,
            roadGeometry = roadGeometry
        )

        // 1. Draw Terrain Background (Lush green grass)
        drawTerrainBackground(viewport)

        // 2. Draw Road Asphalt Surface from RoadGeometry boundaries
        drawRoadSurface(roadGeometry, viewport)

        // 3. Draw Road Kerbs (Rumble strips) along the boundaries
        drawRoadKerbs(roadGeometry, viewport)

        // 4. Draw Centerline Dashes
        drawCenterline(roadGeometry, viewport)

        // 5. Draw Start Line (at 0m) and Finish Line (at 100m)
        drawStartAndFinishLines(roadGeometry, viewport)

        // 6. Draw Thief Car
        drawThiefCar(gameState.thiefPosition, gameState.thiefHeadingDeg, viewport)

        // 7. Draw Police Car with Siren & Drag Halo
        drawPoliceCar(
            position = gameState.policePosition,
            headingDeg = gameState.policeHeadingDeg,
            viewport = viewport,
            pulseAlpha = pulseAlpha,
            sirenFlip = sirenFlip,
            isChasing = gameState.isPoliceChasing
        )
    }
}

/**
 * Draws roadside grass terrain.
 */
private fun DrawScope.drawTerrainBackground(viewport: GameViewport) {
    drawRect(color = Color(0xFF2E6B34)) // Forest green

    // Draw subtle ground striping
    val stripeSpacingPx = maxOf(viewport.metersToPixels(8f), 50f)
    var y = 0f
    while (y < size.height) {
        drawRect(
            color = Color(0xFF285E2E),
            topLeft = Offset(0f, y),
            size = Size(size.width, stripeSpacingPx * 0.45f)
        )
        y += stripeSpacingPx
    }
}

/**
 * Fills the road polygon created from the geometry boundaries.
 */
private fun DrawScope.drawRoadSurface(roadGeometry: RoadGeometry, viewport: GameViewport) {
    val boundaries = roadGeometry.boundaries
    if (boundaries.size < 2) return

    val roadPath = Path()
    val firstLeft = viewport.worldToScreen(boundaries.first().left)
    roadPath.moveTo(firstLeft.x, firstLeft.y)

    // Trace left side upwards (from start to finish)
    for (i in 1 until boundaries.size) {
        val pt = viewport.worldToScreen(boundaries[i].left)
        roadPath.lineTo(pt.x, pt.y)
    }

    // Connect to right side and trace downwards (from finish to start)
    for (i in boundaries.indices.reversed()) {
        val pt = viewport.worldToScreen(boundaries[i].right)
        roadPath.lineTo(pt.x, pt.y)
    }
    roadPath.close()

    // Draw dark asphalt base
    drawPath(path = roadPath, color = Color(0xFF2B2D2F), style = Fill)

    // Draw road edge border outline
    drawPath(path = roadPath, color = Color(0xFF1E2022), style = Stroke(width = 3f))
}

/**
 * Draws red and white alternating kerbs / rumble strips along the left and right road edges.
 */
private fun DrawScope.drawRoadKerbs(roadGeometry: RoadGeometry, viewport: GameViewport) {
    val boundaries = roadGeometry.boundaries
    if (boundaries.size < 2) return

    val kerbWidth = maxOf(viewport.metersToPixels(0.7f), 4f)

    for (i in 0 until boundaries.size - 1) {
        val b0 = boundaries[i]
        val b1 = boundaries[i + 1]

        val kerbColor = if (i % 2 == 0) Color(0xFFE53935) else Color(0xFFEEEEEE)

        // Left kerb segment
        val l0 = viewport.worldToScreen(b0.left)
        val l1 = viewport.worldToScreen(b1.left)
        drawLine(
            color = kerbColor,
            start = l0,
            end = l1,
            strokeWidth = kerbWidth
        )

        // Right kerb segment
        val r0 = viewport.worldToScreen(b0.right)
        val r1 = viewport.worldToScreen(b1.right)
        drawLine(
            color = kerbColor,
            start = r0,
            end = r1,
            strokeWidth = kerbWidth
        )
    }
}

/**
 * Draws dashed yellow centerline along the entire road path.
 */
private fun DrawScope.drawCenterline(roadGeometry: RoadGeometry, viewport: GameViewport) {
    val pathPoints = roadGeometry.pathPoints
    if (pathPoints.size < 2) return

    val centerPath = Path()
    val start = viewport.worldToScreen(pathPoints.first())
    centerPath.moveTo(start.x, start.y)

    for (i in 1 until pathPoints.size) {
        val pt = viewport.worldToScreen(pathPoints[i])
        centerPath.lineTo(pt.x, pt.y)
    }

    val dashLengthPx = maxOf(viewport.metersToPixels(3.0f), 12f)
    val dashGapPx = maxOf(viewport.metersToPixels(2.5f), 10f)
    val strokeWidth = maxOf(viewport.metersToPixels(0.5f), 3f)

    drawPath(
        path = centerPath,
        color = Color(0xFFFDD835), // Road yellow
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLengthPx, dashGapPx), 0f)
        )
    )
}

/**
 * Draws the Start Line (Y=0m) and Finish Line (checkered, Y=100m).
 */
private fun DrawScope.drawStartAndFinishLines(roadGeometry: RoadGeometry, viewport: GameViewport) {
    // 1. Start line
    val (startLeft, startRight) = roadGeometry.getRoadBoundariesAtDistance(roadGeometry.startPositionMeters)
    val sL = viewport.worldToScreen(startLeft)
    val sR = viewport.worldToScreen(startRight)
    val startStroke = maxOf(viewport.metersToPixels(1.0f), 6f)
    drawLine(
        color = Color.White,
        start = sL,
        end = sR,
        strokeWidth = startStroke
    )

    // 2. Finish line (Checkered racing pattern)
    val (finishLeft, finishRight) = roadGeometry.getRoadBoundariesAtDistance(roadGeometry.finishPositionMeters)
    val fL = viewport.worldToScreen(finishLeft)
    val fR = viewport.worldToScreen(finishRight)

    val numBlocks = 10
    val dx = (fR.x - fL.x) / numBlocks
    val dy = (fR.y - fL.y) / numBlocks
    val thickness = maxOf(viewport.metersToPixels(2.2f), 14f)

    for (k in 0 until numBlocks) {
        val color = if (k % 2 == 0) Color.White else Color.Black
        val p1 = Offset(fL.x + k * dx, fL.y + k * dy)
        val p2 = Offset(fL.x + (k + 1) * dx, fL.y + (k + 1) * dy)
        drawLine(
            color = color,
            start = p1,
            end = p2,
            strokeWidth = thickness
        )
    }
}

/**
 * Renders the getaway Thief Car.
 */
private fun DrawScope.drawThiefCar(position: Point2D, headingDeg: Float, viewport: GameViewport) {
    val center = viewport.worldToScreen(position)
    val carWidth = maxOf(viewport.metersToPixels(2.6f), 24f)
    val carLength = maxOf(viewport.metersToPixels(5.2f), 48f)

    rotate(degrees = headingDeg, pivot = center) {
        // Shadow
        drawRect(
            color = Color(0x55000000),
            topLeft = Offset(center.x - carWidth / 2f + 3f, center.y - carLength / 2f + 4f),
            size = Size(carWidth, carLength)
        )

        // Tires
        val tireW = carWidth * 0.22f
        val tireL = carLength * 0.26f
        val tireColor = Color(0xFF1B1B1B)
        drawRect(tireColor, Offset(center.x - carWidth / 2f - tireW * 0.2f, center.y - carLength * 0.4f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x + carWidth / 2f - tireW * 0.8f, center.y - carLength * 0.4f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x - carWidth / 2f - tireW * 0.2f, center.y + carLength * 0.15f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x + carWidth / 2f - tireW * 0.8f, center.y + carLength * 0.15f), Size(tireW, tireL))

        // Body (Hot Rod Red)
        drawRect(
            color = Color(0xFFC62828),
            topLeft = Offset(center.x - carWidth / 2f, center.y - carLength / 2f),
            size = Size(carWidth, carLength)
        )

        // Racing Stripe
        val stripeW = carWidth * 0.18f
        drawRect(
            color = Color(0xFF212121),
            topLeft = Offset(center.x - stripeW / 2f, center.y - carLength / 2f),
            size = Size(stripeW, carLength)
        )

        // Windshield
        drawRect(
            color = Color(0xFF1565C0),
            topLeft = Offset(center.x - carWidth * 0.35f, center.y - carLength * 0.22f),
            size = Size(carWidth * 0.7f, carLength * 0.26f)
        )

        // Rear window
        drawRect(
            color = Color(0xFF1565C0),
            topLeft = Offset(center.x - carWidth * 0.3f, center.y + carLength * 0.18f),
            size = Size(carWidth * 0.6f, carLength * 0.16f)
        )

        // Headlights
        val lightRadius = carWidth * 0.12f
        drawCircle(Color(0xFFFFF59D), lightRadius, Offset(center.x - carWidth * 0.32f, center.y - carLength / 2f + lightRadius))
        drawCircle(Color(0xFFFFF59D), lightRadius, Offset(center.x + carWidth * 0.32f, center.y - carLength / 2f + lightRadius))

        // Taillights
        drawRect(Color(0xFFFF1744), Offset(center.x - carWidth * 0.4f, center.y + carLength / 2f - 3f), Size(carWidth * 0.25f, 3f))
        drawRect(Color(0xFFFF1744), Offset(center.x + carWidth * 0.15f, center.y + carLength / 2f - 3f), Size(carWidth * 0.25f, 3f))
    }
}

/**
 * Renders the top-down Police Cruiser with flashing siren bar and touch indicator.
 */
private fun DrawScope.drawPoliceCar(
    position: Point2D,
    headingDeg: Float,
    viewport: GameViewport,
    pulseAlpha: Float,
    sirenFlip: Float,
    isChasing: Boolean
) {
    val center = viewport.worldToScreen(position)
    val carWidth = maxOf(viewport.metersToPixels(2.8f), 26f)
    val carLength = maxOf(viewport.metersToPixels(5.6f), 52f)

    // Touch Drag Target / Halo Indicator (Generous touch target)
    val haloRadius = maxOf(viewport.metersToPixels(8.5f), 65f)
    drawCircle(
        color = Color(0x332196F3),
        radius = haloRadius,
        center = center
    )
    drawCircle(
        color = Color(0xFF2196F3).copy(alpha = pulseAlpha),
        radius = haloRadius,
        center = center,
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
        )
    )

    rotate(degrees = headingDeg, pivot = center) {
        // Shadow
        drawRect(
            color = Color(0x55000000),
            topLeft = Offset(center.x - carWidth / 2f + 3f, center.y - carLength / 2f + 4f),
            size = Size(carWidth, carLength)
        )

        // Tires
        val tireW = carWidth * 0.22f
        val tireL = carLength * 0.26f
        val tireColor = Color(0xFF1B1B1B)
        drawRect(tireColor, Offset(center.x - carWidth / 2f - tireW * 0.2f, center.y - carLength * 0.4f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x + carWidth / 2f - tireW * 0.8f, center.y - carLength * 0.4f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x - carWidth / 2f - tireW * 0.2f, center.y + carLength * 0.15f), Size(tireW, tireL))
        drawRect(tireColor, Offset(center.x + carWidth / 2f - tireW * 0.8f, center.y + carLength * 0.15f), Size(tireW, tireL))

        // Body: Police Black base
        drawRect(
            color = Color(0xFF212121),
            topLeft = Offset(center.x - carWidth / 2f, center.y - carLength / 2f),
            size = Size(carWidth, carLength)
        )
        // White Roof & Middle Section
        drawRect(
            color = Color(0xFFECEFF1),
            topLeft = Offset(center.x - carWidth / 2f, center.y - carLength * 0.25f),
            size = Size(carWidth, carLength * 0.5f)
        )

        // Windshield
        drawRect(
            color = Color(0xFF455A64),
            topLeft = Offset(center.x - carWidth * 0.35f, center.y - carLength * 0.2f),
            size = Size(carWidth * 0.7f, carLength * 0.22f)
        )

        // Flashing Siren Bar on Roof (Flashes brightly only during active chase pursuit)
        val sirenW = carWidth * 0.65f
        val sirenH = maxOf(carLength * 0.14f, 7f)
        val sirenY = center.y - carLength * 0.05f
        val leftSirenColor = if (isChasing) {
            if (sirenFlip > 0.5f) Color(0xFFFF1744) else Color(0xFF00E5FF)
        } else {
            Color(0xFF7F0000)
        }
        val rightSirenColor = if (isChasing) {
            if (sirenFlip > 0.5f) Color(0xFF00E5FF) else Color(0xFFFF1744)
        } else {
            Color(0xFF002244)
        }

        drawRect(
            color = leftSirenColor,
            topLeft = Offset(center.x - sirenW / 2f, sirenY),
            size = Size(sirenW / 2f, sirenH)
        )
        drawRect(
            color = rightSirenColor,
            topLeft = Offset(center.x, sirenY),
            size = Size(sirenW / 2f, sirenH)
        )

        // Siren glow halo active only during chase
        if (isChasing) {
            drawCircle(
                color = leftSirenColor.copy(alpha = 0.35f),
                radius = carWidth * 0.8f,
                center = Offset(center.x - sirenW * 0.25f, sirenY + sirenH / 2f)
            )
            drawCircle(
                color = rightSirenColor.copy(alpha = 0.35f),
                radius = carWidth * 0.8f,
                center = Offset(center.x + sirenW * 0.25f, sirenY + sirenH / 2f)
            )
        }

        // Front Push Bumper
        drawRect(
            color = Color(0xFF37474F),
            topLeft = Offset(center.x - carWidth * 0.35f, center.y - carLength / 2f - 3f),
            size = Size(carWidth * 0.7f, 5f)
        )
    }
}
