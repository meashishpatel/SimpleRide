package com.simpleride.rideos.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val RedAlert = Color(0xFFFF3B30)
private val NeonCyan = Color(0xFF00F0FF)
private val NeonGreen = Color(0xFF21E68A)

/**
 * Right-hand alert stack. Alerts are only drawn when active so the rider is not
 * shown warnings that do not apply (e.g. "Speed Exceeded" while parked).
 */
@Composable
fun RightStatusStack(
    modifier: Modifier = Modifier,
    rapidBrakeActive: Boolean = false,
    superHoldActive: Boolean = false,
    speedExceeded: Boolean = false,
    speedLimit: Int = 80
) {
    Column(
        modifier = modifier.width(150.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AnimatedVisibility(visible = rapidBrakeActive, enter = fadeIn(), exit = fadeOut()) {
            RapidBrakeWidget()
        }
        AnimatedVisibility(visible = superHoldActive, enter = fadeIn(), exit = fadeOut()) {
            SuperHoldWidget()
        }
        SpeedExceededWidget(active = speedExceeded, limit = speedLimit)
    }
}

/** Portrait variant of [RightStatusStack]: the same alerts in a centred, wrapping row. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatusAlertRow(
    modifier: Modifier = Modifier,
    rapidBrakeActive: Boolean = false,
    superHoldActive: Boolean = false,
    speedExceeded: Boolean = false,
    speedLimit: Int = 80
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (rapidBrakeActive) RapidBrakeWidget()
        if (superHoldActive) SuperHoldWidget()
        SpeedExceededWidget(active = speedExceeded, limit = speedLimit)
    }
}

@Composable
fun RapidBrakeWidget(active: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        // Red Double Lightning / Chevron Icon
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // Double bolt icon
            val path1 = Path().apply {
                moveTo(w * 0.45f, 0f)
                lineTo(w * 0.15f, h * 0.55f)
                lineTo(w * 0.45f, h * 0.55f)
                lineTo(w * 0.30f, h)
                lineTo(w * 0.65f, h * 0.45f)
                lineTo(w * 0.35f, h * 0.45f)
                close()
            }
            drawPath(path = path1, color = RedAlert)

            val path2 = Path().apply {
                moveTo(w * 0.80f, 0f)
                lineTo(w * 0.50f, h * 0.55f)
                lineTo(w * 0.80f, h * 0.55f)
                lineTo(w * 0.65f, h)
                lineTo(w * 0.98f, h * 0.45f)
                lineTo(w * 0.68f, h * 0.45f)
                close()
            }
            drawPath(path = path2, color = RedAlert)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = "Rapid",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 13.sp
            )
            Text(
                text = "Brake",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
fun SuperHoldWidget(active: Boolean = true) {
    Box(
        modifier = Modifier
            .background(
                Color(0xFF071F1E),
                RoundedCornerShape(24.dp)
            )
            .border(1.5.dp, NeonCyan, RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Arcs
            Text(
                text = "(((",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .background(NeonCyan, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SUPER",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Hold",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Right Arcs
            Text(
                text = ")))",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SpeedExceededWidget(active: Boolean = true, limit: Int = 80) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle Speed Badge
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.Black, CircleShape)
                .border(2.dp, if (active) RedAlert else Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$limit",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 11.sp
                )
                Text(
                    text = "km/h",
                    color = Color.Gray,
                    fontSize = 7.sp,
                    lineHeight = 7.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = "Speed",
                color = if (active) Color.White else Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 12.sp
            )
            Text(
                text = if (active) "Exceeded" else "Limit",
                color = if (active) RedAlert else Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun CruiseControlWidget(
    active: Boolean = true,
    targetSpeed: Int = 72
) {
    Box(
        modifier = Modifier
            .background(
                Color(0xFF0B1713),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, Color(0xFF193B2D), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cruise Gauge Icon
            Canvas(modifier = Modifier.size(24.dp)) {
                drawArc(
                    color = NeonGreen,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                // Gauge Needle
                drawLine(
                    color = NeonGreen,
                    start = Offset(size.width / 2f, size.height / 2f),
                    end = Offset(size.width * 0.25f, size.height * 0.25f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = if (active) "Active" else "Inactive",
                    color = NeonGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 11.sp
                )
                Text(
                    text = "$targetSpeed km/h",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun RightStatusStackPreview() {
    RightStatusStack()
}
