package com.simpleride.rideos.presentation.model

/** High level ride state derived from the raw signals. */
enum class RideState(val label: String) {
    UNKNOWN("--"),
    PARKED("PARKED"),
    READY("READY"),
    RIDING("RIDING"),
    MOTOR_DISABLED("MOTOR DISABLED")
}

/** Health of the incoming vehicle signal. */
enum class SignalStatus {
    /** No sample received yet. */
    WAITING,

    /** Latest sample is fresh and every signal is valid. */
    OK,

    /** Latest sample is fresh but at least one signal was missing or invalid. */
    DEGRADED,

    /** No fresh sample within the timeout, or the sample itself was too old. */
    STALE,

    /** The data source failed; the ViewModel is retrying. */
    ERROR
}

/**
 * Everything the Home/Ride screen needs to draw itself. Null means "unknown",
 * which the UI renders as "--" rather than a misleading value.
 */
data class VehicleUiState(
    val speedKmh: Int? = null,
    val motorOn: Boolean? = null,
    val killSwitchActive: Boolean? = null,
    val batteryPercent: Int? = null,
    val rideState: RideState = RideState.UNKNOWN,
    val signalStatus: SignalStatus = SignalStatus.WAITING,
    val warningMessage: String? = null,
    val lastUpdatedMillis: Long? = null,
    val rapidBrakeActive: Boolean = false,
    // Static values: not part of the simulated signal set, shown for the dashboard layout.
    val rideMode: String = "ECO",
    val rangeKm: Int = 62,
    val odoKm: Int = 1324,
    val speedLimitKmh: Int = 80
) {
    val speedExceeded: Boolean get() = (speedKmh ?: 0) > speedLimitKmh
    val superHoldActive: Boolean get() = rideState == RideState.READY
}
