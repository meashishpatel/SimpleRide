package com.simpleride.rideos.presentation.model

import com.simpleride.rideos.data.model.VehicleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class VehicleStateReducerTest {

    private val now = 1_000_000L
    private val threshold = 3_000L

    private fun reduce(data: VehicleData, previous: VehicleUiState = VehicleUiState()) =
        VehicleStateReducer.onSample(previous, data, now, threshold)

    private fun data(
        speed: Float? = 20f,
        motorOn: Boolean? = true,
        killSwitch: Boolean? = false,
        timestamp: Long = now,
        battery: Int? = 80
    ) = VehicleData(speed, motorOn, killSwitch, timestamp, battery)

    @Test
    fun `sample older than the threshold is treated as stale`() {
        val state = reduce(data(timestamp = now - threshold - 1))

        assertEquals(SignalStatus.STALE, state.signalStatus)
        assertNull(state.speedKmh)
    }

    @Test
    fun `sample from the future is treated as stale`() {
        val state = reduce(data(timestamp = now + threshold + 1))

        assertEquals(SignalStatus.STALE, state.signalStatus)
    }

    @Test
    fun `implausibly high and infinite speeds are rejected`() {
        assertNull(reduce(data(speed = 999f)).speedKmh)
        assertNull(reduce(data(speed = Float.POSITIVE_INFINITY)).speedKmh)
        assertEquals(SignalStatus.DEGRADED, reduce(data(speed = 999f)).signalStatus)
    }

    @Test
    fun `missing signals are reported and ride state is unknown`() {
        val state = reduce(data(speed = null, motorOn = null))

        assertEquals(SignalStatus.DEGRADED, state.signalStatus)
        assertEquals(RideState.UNKNOWN, state.rideState)
        assertEquals("Speed signal missing · Motor signal missing", state.warningMessage)
    }

    @Test
    fun `out of range battery is dropped`() {
        val state = reduce(data(battery = 140))

        assertNull(state.batteryPercent)
        assertEquals("Invalid battery signal", state.warningMessage)
    }

    @Test
    fun `motor off while moving is coasting, not parked`() {
        assertEquals(
            RideState.RIDING,
            VehicleStateReducer.deriveRideState(speedKmh = 15, motorOn = false, killSwitchActive = false)
        )
    }

    @Test
    fun `kill switch wins over every other signal`() {
        assertEquals(
            RideState.MOTOR_DISABLED,
            VehicleStateReducer.deriveRideState(speedKmh = null, motorOn = true, killSwitchActive = true)
        )
    }

    @Test
    fun `gradual slowdown is not a rapid brake`() {
        val previous = VehicleUiState(speedKmh = 30)
        assertFalse(reduce(data(speed = 20f), previous).rapidBrakeActive)
    }
}
