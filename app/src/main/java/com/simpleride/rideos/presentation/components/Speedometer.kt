package com.simpleride.rideos.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.simpleride.rideos.R
import androidx.compose.ui.res.painterResource
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

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                R.drawable.bg_speedometer
            ),
            contentDescription = "Speedometer background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
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
