package com.simpleride.rideos.presentation.model

import com.simpleride.rideos.data.model.VehicleData
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Pure functions that turn raw vehicle samples into [VehicleUiState].
 *
 * Kept free of Android and coroutine types so the validation rules can be unit
 * tested directly and reused by any ViewModel.
 */
object VehicleStateReducer {

    /** Anything above this is treated as a corrupt reading, not a real speed. */
    const val MAX_VALID_SPEED_KMH = 250f

    /** A drop of at least this much between two consecutive samples flags a rapid brake. */
    const val RAPID_BRAKE_DROP_KMH = 20

    fun onSample(
        previous: VehicleUiState,
        data: VehicleData,
        nowMillis: Long,
        staleThresholdMillis: Long
    ): VehicleUiState {
        // A sample that is too old (or stamped too far in the future) cannot be trusted.
        if (abs(nowMillis - data.timestampMillis) > staleThresholdMillis) {
            return onSignalLost(previous, SignalStatus.STALE, "Vehicle data is stale")
        }

        val problems = mutableListOf<String>()

        val speed = validateSpeed(data.speedKmh, problems)
        if (data.motorOn == null) problems += "Motor signal missing"
        if (data.killSwitchActive == null) problems += "Kill switch signal missing"
        val battery = data.batteryPercent?.takeIf { it in 0..100 }
        if (data.batteryPercent != null && battery == null) problems += "Invalid battery signal"

        val previousSpeed = previous.speedKmh
        val rapidBrake = previousSpeed != null && speed != null &&
            previousSpeed - speed >= RAPID_BRAKE_DROP_KMH

        return previous.copy(
            speedKmh = speed,
            motorOn = data.motorOn,
            killSwitchActive = data.killSwitchActive,
            batteryPercent = battery,
            rideState = deriveRideState(speed, data.motorOn, data.killSwitchActive),
            signalStatus = if (problems.isEmpty()) SignalStatus.OK else SignalStatus.DEGRADED,
            warningMessage = problems.takeIf { it.isNotEmpty() }?.joinToString(" · "),
            lastUpdatedMillis = data.timestampMillis,
            rapidBrakeActive = rapidBrake
        )
    }

    /**
     * Clears every live value so the UI shows "--" instead of a frozen reading that
     * may no longer be true.
     */
    fun onSignalLost(
        previous: VehicleUiState,
        status: SignalStatus,
        message: String
    ): VehicleUiState = previous.copy(
        speedKmh = null,
        motorOn = null,
        killSwitchActive = null,
        batteryPercent = null,
        rideState = RideState.UNKNOWN,
        signalStatus = status,
        warningMessage = message,
        rapidBrakeActive = false
    )

    /**
     * Note: motor OFF does not imply zero speed (the scooter can coast), so the
     * state is derived from speed first and motor second.
     */
    fun deriveRideState(speedKmh: Int?, motorOn: Boolean?, killSwitchActive: Boolean?): RideState =
        when {
            killSwitchActive == true -> RideState.MOTOR_DISABLED
            speedKmh == null || motorOn == null || killSwitchActive == null -> RideState.UNKNOWN
            speedKmh > 0 -> RideState.RIDING
            motorOn -> RideState.READY
            else -> RideState.PARKED
        }

    private fun validateSpeed(raw: Float?, problems: MutableList<String>): Int? = when {
        raw == null -> {
            problems += "Speed signal missing"
            null
        }

        !raw.isFinite() || raw < 0f || raw > MAX_VALID_SPEED_KMH -> {
            problems += "Invalid speed signal"
            null
        }

        else -> raw.roundToInt()
    }
}
