package com.simpleride.rideos.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private val OrangeGrillColor = Color(0xFFFF6A00)
private val BrightOrangeGlow = Color(0xFFFF9400)
private val DarkCowlColor = Color(0xFF1E2228)
private val CowlTopGradient = Color(0xFF323842)
/** Cowl is at most 1 / this ratio as tall as it is wide. */
private const val MAX_COWL_HEIGHT_RATIO = 2.2f
private val RedTipColor = Color(0xFFFF2B4A)

/**
 * Large speed read-out over the cowl graphic.
 *
 * @param speedKmh  current speed, or null when unknown/invalid (rendered as "--").
 * @param stateLabel short ride state shown under the unit, e.g. "READY".
 */
@Composable
fun Speedometer(
    speedKmh: Int?,
    modifier: Modifier = Modifier,
    stateLabel: String? = null,
    stateColor: Color = Color.LightGray,
    maxSpeed: Float = 100f,
    speedFontSize: TextUnit = 86.sp
) {
    val targetSpeed = speedKmh?.toFloat()?.coerceIn(0f, maxSpeed) ?: 0f

    val animatedSpeed = animateFloatAsState(
        targetValue = targetSpeed,
        animationSpec = tween(500),
        label = "speed"
    )
    // Grill glow scales with speed: dim when stopped, full brightness at max speed.
    val glow = 0.35f + 0.65f * (animatedSpeed.value / maxSpeed)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // Speed display number
            Text(
                text = if (speedKmh != null) animatedSpeed.value.roundToInt().toString() else "--",
                color = Color.White,
                fontSize = speedFontSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-2).sp
            )

            // km/h label
            Text(
                text = "km / h",
                color = Color.LightGray.copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )

            if (stateLabel != null) {
                Text(
                    text = stateLabel,
                    color = stateColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Futuristic Cowl Graphic Canvas.
            // Height is capped relative to width so the cowl keeps its wide shape on
            // tall (portrait) screens instead of stretching vertically.
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                contentAlignment = Alignment.TopCenter
            ) {
                val cowlHeight = minOf(maxHeight, maxWidth / MAX_COWL_HEIGHT_RATIO)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cowlHeight)
                ) {
                    val w = size.width
                    val h = size.height

                    val centerX = w / 2f

                    // 1. Perspective Road / Light Beams extending downwards
                    val roadPath = Path().apply {
                        moveTo(centerX - w * 0.15f, h * 0.65f)
                        lineTo(centerX - w * 0.45f, h)
                        lineTo(centerX - w * 0.25f, h)
                        lineTo(centerX - w * 0.08f, h * 0.65f)
                        close()
                    }
                    drawPath(
                        path = roadPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x2211151A), Color(0x66080A0C))
                        )
                    )

                    val roadPath2 = Path().apply {
                        moveTo(centerX + w * 0.15f, h * 0.65f)
                        lineTo(centerX + w * 0.45f, h)
                        lineTo(centerX + w * 0.25f, h)
                        lineTo(centerX + w * 0.08f, h * 0.65f)
                        close()
                    }
                    drawPath(
                        path = roadPath2,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x2211151A), Color(0x66080A0C))
                        )
                    )

                    // 2. Main Cowl Body Polygon Path
                    val cowlY = h * 0.15f
                    val cowlHeight = h * 0.50f

                    val wingLeftX = w * 0.05f
                    val wingRightX = w * 0.95f

                    val cowlPath = Path().apply {
                        moveTo(centerX, cowlY) // Top center dome
                        cubicTo(
                            centerX - w * 0.15f, cowlY - 10f,
                            centerX - w * 0.30f, cowlY + 25f,
                            wingLeftX, cowlY + cowlHeight * 0.35f
                        )
                        lineTo(wingLeftX - 10f, cowlY + cowlHeight * 0.55f)
                        lineTo(centerX - w * 0.25f, cowlY + cowlHeight)
                        lineTo(centerX, cowlY + cowlHeight * 0.85f)
                        lineTo(centerX + w * 0.25f, cowlY + cowlHeight)
                        lineTo(wingRightX + 10f, cowlY + cowlHeight * 0.55f)
                        lineTo(wingRightX, cowlY + cowlHeight * 0.35f)
                        cubicTo(
                            centerX + w * 0.30f, cowlY + 25f,
                            centerX + w * 0.15f, cowlY - 10f,
                            centerX, cowlY
                        )
                        close()
                    }

                    // Draw cowl body gradient fill
                    drawPath(
                        path = cowlPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(CowlTopGradient, DarkCowlColor, Color(0xFF090B0D))
                        )
                    )

                    // Outline / rim highlight of the cowl
                    drawPath(
                        path = cowlPath,
                        color = Color.White.copy(alpha = 0.15f),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Air vents / perforated dot pattern on cowl wings
                    val numDots = 8
                    for (i in 0 until numDots) {
                        val leftX = wingLeftX + i * 14.dp.toPx()
                        val dotY = cowlY + cowlHeight * 0.38f + (i * 2.dp.toPx())
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.6f),
                            radius = 2.dp.toPx(),
                            center = Offset(leftX, dotY)
                        )

                        val rightX = wingRightX - i * 14.dp.toPx()
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.6f),
                            radius = 2.dp.toPx(),
                            center = Offset(rightX, dotY)
                        )
                    }

                    // 3. Wingtip Red LED Lights
                    drawRect(
                        color = RedTipColor,
                        topLeft = Offset(wingLeftX + 20.dp.toPx(), cowlY + cowlHeight * 0.18f),
                        size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 6.dp.toPx())
                    )
                    drawRect(
                        color = RedTipColor,
                        topLeft = Offset(wingRightX - 44.dp.toPx(), cowlY + cowlHeight * 0.18f),
                        size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 6.dp.toPx())
                    )

                    // 4. Glowing Orange Illuminated Grill Strip across the front wing
                    val grillPath = Path().apply {
                        moveTo(wingLeftX + 35.dp.toPx(), cowlY + cowlHeight * 0.52f)
                        lineTo(centerX - w * 0.15f, cowlY + cowlHeight * 0.68f)
                        lineTo(centerX, cowlY + cowlHeight * 0.60f)
                        lineTo(centerX + w * 0.15f, cowlY + cowlHeight * 0.68f)
                        lineTo(wingRightX - 35.dp.toPx(), cowlY + cowlHeight * 0.52f)
                        lineTo(wingRightX - 35.dp.toPx(), cowlY + cowlHeight * 0.68f)
                        lineTo(centerX + w * 0.15f, cowlY + cowlHeight * 0.85f)
                        lineTo(centerX, cowlY + cowlHeight * 0.77f)
                        lineTo(centerX - w * 0.15f, cowlY + cowlHeight * 0.85f)
                        lineTo(wingLeftX + 35.dp.toPx(), cowlY + cowlHeight * 0.68f)
                        close()
                    }

                    // Glowing Orange Fill
                    drawPath(
                        path = grillPath,
                        brush = Brush.horizontalGradient(
                            colors = listOf(OrangeGrillColor, BrightOrangeGlow, OrangeGrillColor)
                        ),
                        alpha = glow.coerceIn(0f, 1f)
                    )

                    // Vertical Rib Texture inside Orange Grill
                    val ribs = 36
                    val startX = wingLeftX + 38.dp.toPx()
                    val totalW = (wingRightX - 38.dp.toPx()) - startX
                    val step = totalW / ribs
                    for (r in 0..ribs) {
                        val rx = startX + r * step
                        val topY = cowlY + cowlHeight * 0.58f
                        val botY = cowlY + cowlHeight * 0.72f
                        drawLine(
                            color = Color.Black.copy(alpha = 0.45f),
                            start = Offset(rx, topY),
                            end = Offset(rx, botY),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Orange Glow outline overlay
                    drawPath(
                        path = grillPath,
                        color = BrightOrangeGlow.copy(alpha = 0.8f),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 500,
    heightDp = 300
)
@Composable
fun SpeedometerPreview() {
    Speedometer(speedKmh = 74, stateLabel = "RIDING")
}
