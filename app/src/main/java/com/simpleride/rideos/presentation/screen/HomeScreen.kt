package com.simpleride.rideos.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simpleride.rideos.presentation.components.BottomNavigation
import com.simpleride.rideos.presentation.components.HmiColors
import com.simpleride.rideos.presentation.components.LeftParkingTab
import com.simpleride.rideos.presentation.components.NavigationIcons
import com.simpleride.rideos.presentation.components.RightStatusStack
import com.simpleride.rideos.presentation.components.Speedometer
import com.simpleride.rideos.presentation.components.StatusAlertRow
import com.simpleride.rideos.presentation.components.TopConsoleHeader
import com.simpleride.rideos.presentation.components.VehicleControlIcons
import com.simpleride.rideos.presentation.components.VehicleStatusPills
import com.simpleride.rideos.presentation.model.RideState
import com.simpleride.rideos.presentation.model.SignalStatus
import com.simpleride.rideos.presentation.model.VehicleUiState

/**
 * Stateless Home/Ride dashboard: everything it shows comes from [state].
 *
 * The activity requests landscape, but Android 16+ ignores orientation locks on
 * large screens and in multi-window, so a portrait layout is provided as well.
 * The layout is chosen from the available space, not the device orientation.
 */
@Composable
fun HomeScreen(
    state: VehicleUiState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(HmiColors.Background)
            .safeDrawingPadding()
    ) {
        if (maxHeight > maxWidth) {
            PortraitDashboard(state, screenWidth = maxWidth)
        } else {
            LandscapeDashboard(state)
        }
    }
}

@Composable
private fun LandscapeDashboard(state: VehicleUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        LeftParkingTab(modifier = Modifier.align(Alignment.CenterStart))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 44.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopConsoleHeader(
                mode = state.rideMode,
                rangeKm = state.rangeKm,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mirrors the right stack's width so the speed stays centred.
                Spacer(Modifier.width(150.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Speedometer(
                        speedKmh = state.speedKmh,
                        stateLabel = state.rideState.label,
                        stateColor = state.rideState.color(),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RightStatusStack(
                    rapidBrakeActive = state.rapidBrakeActive,
                    superHoldActive = state.superHoldActive,
                    speedExceeded = state.speedExceeded,
                    speedLimit = state.speedLimitKmh,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            OdometerRow(odoKm = state.odoKm)

            BottomNavigation(
                batteryPercentage = state.batteryPercent,
                motorOn = state.motorOn,
                killSwitchActive = state.killSwitchActive,
                modifier = Modifier.fillMaxWidth()
            )
        }

        SignalWarningBanner(
            status = state.signalStatus,
            message = state.warningMessage,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 52.dp, top = 12.dp)
        )
    }
}

/**
 * Portrait: one centred column. The right-hand alert stack becomes a row under the
 * speed, and the bottom bar splits into status pills plus a row of shortcut icons.
 * The parking slider is dropped because there is no side space for it.
 */
@Composable
private fun PortraitDashboard(state: VehicleUiState, screenWidth: Dp) {
    // Scale the speed read-out with the screen so it does not look tiny on tablets.
    val speedFontSize = (screenWidth.value * 0.26f).coerceIn(86f, 200f).sp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignalWarningBanner(
            status = state.signalStatus,
            message = state.warningMessage,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TopConsoleHeader(
            mode = state.rideMode,
            rangeKm = state.rangeKm
        )

        Speedometer(
            speedKmh = state.speedKmh,
            stateLabel = state.rideState.label,
            stateColor = state.rideState.color(),
            speedFontSize = speedFontSize,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )

        StatusAlertRow(
            rapidBrakeActive = state.rapidBrakeActive,
            superHoldActive = state.superHoldActive,
            speedExceeded = state.speedExceeded,
            speedLimit = state.speedLimitKmh,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        OdometerRow(odoKm = state.odoKm)

        VehicleStatusPills(
            batteryPercentage = state.batteryPercent,
            motorOn = state.motorOn,
            killSwitchActive = state.killSwitchActive,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcons()
            VehicleControlIcons()
        }
    }
}

@Composable
private fun OdometerRow(odoKm: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Text(text = "‹", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = "ODO",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$odoKm km", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = "›", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

/** Shown whenever the signal is not healthy, so "--" values are explained to the rider. */
@Composable
private fun SignalWarningBanner(
    status: SignalStatus,
    message: String?,
    modifier: Modifier = Modifier
) {
    val visible = status != SignalStatus.OK
    val text = when (status) {
        SignalStatus.WAITING -> "Waiting for vehicle data…"
        else -> message ?: "Vehicle signal unavailable"
    }
    val color = when (status) {
        SignalStatus.WAITING, SignalStatus.DEGRADED -> HmiColors.Amber
        SignalStatus.STALE, SignalStatus.ERROR -> HmiColors.Red
        SignalStatus.OK -> Color.Transparent
    }

    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut(), modifier = modifier) {
        Row(
            modifier = Modifier
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "⚠", color = color, fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text(text = text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun RideState.color(): Color = when (this) {
    RideState.UNKNOWN -> HmiColors.Muted
    RideState.PARKED -> Color.LightGray
    RideState.READY -> HmiColors.Green
    RideState.RIDING -> HmiColors.Cyan
    RideState.MOTOR_DISABLED -> HmiColors.Red
}

@Preview(name = "Riding", widthDp = 915, heightDp = 412)
@Composable
private fun HomeScreenRidingPreview() {
    HomeScreen(
        state = VehicleUiState(
            speedKmh = 50,
            motorOn = true,
            killSwitchActive = false,
            batteryPercent = 100,
            rideState = RideState.RIDING,
            signalStatus = SignalStatus.OK
        )
    )
}

@Preview(name = "Motor disabled", widthDp = 915, heightDp = 412)
@Composable
private fun HomeScreenKillSwitchPreview() {
    HomeScreen(
        state = VehicleUiState(
            speedKmh = 0,
            motorOn = false,
            killSwitchActive = true,
            batteryPercent = 100,
            rideState = RideState.MOTOR_DISABLED,
            signalStatus = SignalStatus.OK,
            rapidBrakeActive = true
        )
    )
}

@Preview(name = "Portrait - riding", widthDp = 411, heightDp = 891)
@Composable
private fun HomeScreenPortraitPreview() {
    HomeScreen(
        state = VehicleUiState(
            speedKmh = 50,
            motorOn = true,
            killSwitchActive = false,
            batteryPercent = 100,
            rideState = RideState.RIDING,
            signalStatus = SignalStatus.OK
        )
    )
}

@Preview(name = "Signal lost", widthDp = 915, heightDp = 412)
@Composable
private fun HomeScreenSignalLostPreview() {
    HomeScreen(
        state = VehicleUiState(
            signalStatus = SignalStatus.STALE,
            warningMessage = "Vehicle signal lost"
        )
    )
}
