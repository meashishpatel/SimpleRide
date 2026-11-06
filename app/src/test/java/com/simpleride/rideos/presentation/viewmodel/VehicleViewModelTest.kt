package com.simpleride.rideos.presentation.viewmodel

import com.simpleride.rideos.data.model.VehicleData
import com.simpleride.rideos.data.repository.VehicleRepository
import com.simpleride.rideos.data.source.SimulatedVehicleDataSource
import com.simpleride.rideos.data.source.VehicleDataSource
import com.simpleride.rideos.presentation.model.RideState
import com.simpleride.rideos.presentation.model.SignalStatus
import com.simpleride.rideos.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val clock: () -> Long = { mainRule.dispatcher.scheduler.currentTime }

    /** Data source the test pushes samples into by hand. */
    private class FakeDataSource : VehicleDataSource {
        val samples = MutableSharedFlow<VehicleData>()
        override fun observeVehicleData(): Flow<VehicleData> = samples
    }

    private fun createViewModel(source: VehicleDataSource) = VehicleViewModel(
        repository = VehicleRepository(source),
        clock = clock,
        signalTimeoutMillis = 3_000L,
        retryDelayMillis = 2_000L
    )

    private fun sample(speed: Float?, motorOn: Boolean?, killSwitch: Boolean?) = VehicleData(
        speedKmh = speed,
        motorOn = motorOn,
        killSwitchActive = killSwitch,
        timestampMillis = clock(),
        batteryPercent = 100
    )

    @Test
    fun `initial state waits for data and shows no values`() = runTest(mainRule.dispatcher) {
        val viewModel = createViewModel(FakeDataSource())

        val state = viewModel.uiState.value
        assertEquals(SignalStatus.WAITING, state.signalStatus)
        assertNull(state.speedKmh)
        assertNull(state.motorOn)
        assertNull(state.killSwitchActive)
    }

    @Test
    fun `simulated ride updates state through all five scenarios once per second`() =
        runTest(mainRule.dispatcher) {
            // The simulator loops forever; cap it so the test scheduler can go idle.
            val simulator = SimulatedVehicleDataSource(clock = clock)
            val finiteSource = object : VehicleDataSource {
                override fun observeVehicleData() = simulator.observeVehicleData().take(6)
            }
            val viewModel = createViewModel(finiteSource)

            data class Expected(val speed: Int, val motor: Boolean, val kill: Boolean, val ride: RideState)

            val expected = listOf(
                Expected(0, motor = false, kill = false, ride = RideState.PARKED),
                Expected(0, motor = true, kill = false, ride = RideState.READY),
                Expected(25, motor = true, kill = false, ride = RideState.RIDING),
                Expected(50, motor = true, kill = false, ride = RideState.RIDING),
                Expected(0, motor = false, kill = true, ride = RideState.MOTOR_DISABLED)
            )

            expected.forEachIndexed { index, e ->
                if (index > 0) advanceTimeBy(1_000L)
                runCurrent()
                val state = viewModel.uiState.value
                assertEquals("speed at step $index", e.speed, state.speedKmh)
                assertEquals("motor at step $index", e.motor, state.motorOn)
                assertEquals("kill switch at step $index", e.kill, state.killSwitchActive)
                assertEquals("ride state at step $index", e.ride, state.rideState)
                assertEquals(SignalStatus.OK, state.signalStatus)
            }

            // 50 -> 0 km/h in one second is flagged as a rapid brake.
            assertTrue(viewModel.uiState.value.rapidBrakeActive)

            // The script loops back to Parked.
            advanceTimeBy(1_000L)
            runCurrent()
            assertEquals(RideState.PARKED, viewModel.uiState.value.rideState)
        }

    @Test
    fun `negative speed is rejected without crashing and other signals still update`() =
        runTest(mainRule.dispatcher) {
            val source = FakeDataSource()
            val viewModel = createViewModel(source)

            source.samples.emit(sample(speed = -12f, motorOn = true, killSwitch = false))

            val state = viewModel.uiState.value
            assertNull(state.speedKmh)
            assertEquals(true, state.motorOn)
            assertEquals(false, state.killSwitchActive)
            assertEquals(SignalStatus.DEGRADED, state.signalStatus)
            assertEquals("Invalid speed signal", state.warningMessage)
        }

    @Test
    fun `recovers to OK when a valid sample follows an invalid one`() = runTest(mainRule.dispatcher) {
        val source = FakeDataSource()
        val viewModel = createViewModel(source)

        source.samples.emit(sample(speed = Float.NaN, motorOn = true, killSwitch = false))
        assertEquals(SignalStatus.DEGRADED, viewModel.uiState.value.signalStatus)

        source.samples.emit(sample(speed = 30f, motorOn = true, killSwitch = false))
        assertEquals(30, viewModel.uiState.value.speedKmh)
        assertEquals(SignalStatus.OK, viewModel.uiState.value.signalStatus)
        assertNull(viewModel.uiState.value.warningMessage)
    }

    @Test
    fun `signal is marked stale when no data arrives within the timeout`() =
        runTest(mainRule.dispatcher) {
            val source = FakeDataSource()
            val viewModel = createViewModel(source)

            source.samples.emit(sample(speed = 40f, motorOn = true, killSwitch = false))
            assertEquals(40, viewModel.uiState.value.speedKmh)

            // Just before the timeout the value is still trusted.
            advanceTimeBy(2_999L)
            assertEquals(SignalStatus.OK, viewModel.uiState.value.signalStatus)

            advanceTimeBy(2L)
            val state = viewModel.uiState.value
            assertEquals(SignalStatus.STALE, state.signalStatus)
            assertNull("stale speed must not be shown", state.speedKmh)
            assertNull(state.motorOn)
            assertEquals(RideState.UNKNOWN, state.rideState)
            assertNotNull(state.warningMessage)

            // A new sample clears the stale state.
            source.samples.emit(sample(speed = 10f, motorOn = true, killSwitch = false))
            assertEquals(SignalStatus.OK, viewModel.uiState.value.signalStatus)
            assertEquals(10, viewModel.uiState.value.speedKmh)
        }

    @Test
    fun `source failure shows an error and collection is retried`() = runTest(mainRule.dispatcher) {
        var attempts = 0
        val flakySource = object : VehicleDataSource {
            override fun observeVehicleData(): Flow<VehicleData> = flow {
                attempts++
                if (attempts == 1) throw IOException("bus disconnected")
                emit(sample(speed = 25f, motorOn = true, killSwitch = false))
                awaitCancellation()
            }
        }

        val viewModel = createViewModel(flakySource)

        assertEquals(SignalStatus.ERROR, viewModel.uiState.value.signalStatus)
        assertNull(viewModel.uiState.value.speedKmh)

        advanceTimeBy(2_001L)

        assertEquals(2, attempts)
        assertEquals(SignalStatus.OK, viewModel.uiState.value.signalStatus)
        assertEquals(25, viewModel.uiState.value.speedKmh)
    }
}
