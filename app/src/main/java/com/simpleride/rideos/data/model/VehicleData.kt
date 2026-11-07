package com.simpleride.rideos.data.model

/**
 * One raw sample from the vehicle bus (simulated here).
 * @property speedKmh   Vehicle speed in km/h. Null when the signal is missing.
 * @property motorOn    Motor state. Null when the signal is missing.
 * @property killSwitchActive Kill switch state. Null when the signal is missing.
 * @property timestampMillis  Time the sample was produced (epoch millis).
 * @property batteryPercent   Battery state of charge, 0..100. Null when missing.
 */
data class  VehicleData(
    val speedKmh: Float?,
    val motorOn: Boolean?,
    val killSwitchActive: Boolean?,
    val timestampMillis: Long,
    val batteryPercent: Int? = null
)
