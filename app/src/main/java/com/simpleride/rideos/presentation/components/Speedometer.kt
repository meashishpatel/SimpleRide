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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TrackColor = Color(0xFF252B30)
private val GreenColor = Color(0xFF21E68A)

@Composable
fun Speedometer(
    speed: Float?,
    maxSpeed: Float = 100f,
    modifier: Modifier = Modifier
) {
    val targetSpeed = speed?.coerceIn(0f, maxSpeed) ?: 0f

    val animatedSpeed = animateFloatAsState(
        targetValue = targetSpeed,
        animationSpec = tween(700),
        label = "speed"
    )

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val gaugeSize = maxHeight.coerceAtMost(maxWidth)

        Box(
            modifier = Modifier.size(gaugeSize),
            contentAlignment = Alignment.Center
        ) {

            Canvas(modifier = Modifier.fillMaxSize()) {

                val diameter = size.minDimension * 0.88f
                val strokeWidth = diameter * 0.035f

                val topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                )

                val arcSize = Size(diameter, diameter)

                // Background arc
                drawArc(
                    color = TrackColor,
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )

                // Dynamic speed arc
                drawArc(
                    color = GreenColor,
                    startAngle = 150f,
                    sweepAngle = 240f * (
                            animatedSpeed.value / maxSpeed
                            ),
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )

                // Gauge markings
                val center = Offset(
                    size.width / 2,
                    size.height / 2
                )

                val radius = diameter / 2

                for (i in 0..20) {
                    val angle = 150f + i * 12f

                    rotate(
                        degrees = angle,
                        pivot = center
                    ) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.7f),
                            start = Offset(
                                center.x,
                                center.y - radius * 0.95f
                            ),
                            end = Offset(
                                center.x,
                                center.y - radius * 0.85f
                            ),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = speed?.let {
                        animatedSpeed.value.toInt().toString()
                    } ?: "--",
                    color = Color.White,
                    fontSize = 76.sp,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "km/h",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (speed != null) "LIVE SPEED" else "SIGNAL UNAVAILABLE",
                    color = GreenColor,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}