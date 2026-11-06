package com.simpleride.rideos.presentation.components

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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NeonGreen = Color(0xFF21E68A)
private val AmberYellow = Color(0xFFFFB300)
private val ButtonBg = Color(0xFF1E2328)

/**
 * Landscape bottom bar: navigation icons | vehicle controls | status pills, in one row.
 * In portrait the same three groups are laid out separately by the HomeScreen.
 */
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    batteryPercentage: Int? = 100,
    motorOn: Boolean? = true,
    killSwitchActive: Boolean? = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationIcons()
        VehicleControlIcons()
        VehicleStatusPills(
            batteryPercentage = batteryPercentage,
            motorOn = motorOn,
            killSwitchActive = killSwitchActive
        )
    }
}

/** Apps, navigation and music shortcuts. */
@Composable
fun NavigationIcons(modifier: Modifier = Modifier) {
    // Left Action Icons (Apps, Navigation, Music)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Grid Icon
        IconButtonCircle {
            Canvas(modifier = Modifier.size(16.dp)) {
                val s = size.width * 0.4f
                drawRect(Color.White, Offset(0f, 0f), Size(s, s))
                drawRect(Color.White, Offset(s * 1.4f, 0f), Size(s, s))
                drawRect(Color.White, Offset(0f, s * 1.4f), Size(s, s))
                drawRect(Color.White, Offset(s * 1.4f, s * 1.4f), Size(s, s))
            }
        }

        // Location Navigation Arrow
        IconButtonCircle {
            Text(
                text = "▲",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Music Note
        IconButtonCircle {
            Text(
                text = "♫",
                color = Color.White,
                fontSize = 17.sp
            )
        }
    }
}

/** Traction control and tyre pressure shortcuts. */
@Composable
fun VehicleControlIcons(modifier: Modifier = Modifier) {
    // Center Action Icons (Traction Control, Tire Pressure)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Traction Control (T/C) Amber Button
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color(0xFF2B220B), CircleShape)
                .border(1.dp, AmberYellow.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "(T/C)",
                    color = AmberYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 11.sp
                )
            }
        }

        // Tire Pressure Button
        IconButtonCircle {
            Canvas(modifier = Modifier.size(20.dp)) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.8f),
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.8f),
                    radius = size.width * 0.25f,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }
    }
}

/** Battery, kill switch and motor status pills. Wraps onto a second line on narrow screens. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VehicleStatusPills(
    modifier: Modifier = Modifier,
    batteryPercentage: Int?,
    motorOn: Boolean?,
    killSwitchActive: Boolean?
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Battery Status Pill
        Box(
            modifier = Modifier
                .background(ButtonBg, RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 9.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BatteryIcon(color = NeonGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = batteryPercentage?.let { "$it%" } ?: "--%",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        KillSwitchPill(killSwitchActive = killSwitchActive)

        MotorStatusPill(motorOn = motorOn)
    }
}

@Composable
private fun IconButtonCircle(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(ButtonBg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 800)
@Composable
fun PreviewBottomNavigation() {
    BottomNavigation()
}
