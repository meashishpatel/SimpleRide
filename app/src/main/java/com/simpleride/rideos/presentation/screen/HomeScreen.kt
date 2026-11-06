package com.simpleride.rideos.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simpleride.rideos.presentation.components.*
import com.simpleride.rideos.presentation.model.VehicleUiState
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text

private val DashboardBlack = Color(0xFF050607)

@Composable
fun HomeScreen(
    state: VehicleUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DashboardBlack)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {

        // Top section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RideModeIndicator()

            BatteryIndicator(
                batteryPercentage = 100,
                range = 62
            )
        }

        // Main dashboard
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Left section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                MotorStatus(
                    isMotorOn = state.motorOn
                )

                KillSwitchStatus(
                    isActive = state.killSwitchActive
                )
            }

            // Center section
            Box(
                modifier = Modifier
                    .weight(2.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Speedometer(
                    speed = state.speed,
                    maxSpeed = 100f,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Right section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.End
            ) {
                StatusCard(
                    title = "VEHICLE CONNECTION",
                    value = if (state.isDataAvailable) "CONNECTED" else "DISCONNECTED",
                    active = state.isDataAvailable
                )

                StatusCard(
                    title = "MOTOR",
                    value = if (state.motorOn) "READY" else "DISABLED",
                    active = state.motorOn
                )

                StatusCard(
                    title = "KILL SWITCH",
                    value = if (state.killSwitchActive) "ACTIVE" else "INACTIVE",
                    active = !state.killSwitchActive
                )
            }
        }

        // Bottom section
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
        )
    }
}


@Composable
private fun StatusCard(
    title: String,
    value: String,
    active: Boolean
) {
    val statusColor = if (active) Color(0xFF28D985)
    else Color(0xFFFF5252)

    Column(
        modifier = Modifier
            .widthIn(min = 130.dp)
            .background(
                color = Color(0xFF111719),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 10.sp
        )

        Text(
            text = value,
            color = statusColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}