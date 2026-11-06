package com.simpleride.rideos.data.source

import com.simpleride.rideos.data.model.VehicleData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Emits a looping scripted ride, one sample per [intervalMillis].
 *
 * Default script (as required by the assignment):
 *  Parked -> Ready -> Riding -> Accelerating -> Motor disabled -> (repeat)
 *
 * @param scenarios the script to play; overridable for tests or demos.
 * @param clock     time source used to stamp samples; injectable for tests.
 */
class SimulatedVehicleDataSource(
    private val scenarios: List<Scenario> = DEFAULT_SCENARIOS,
    private val intervalMillis: Long = DEFAULT_INTERVAL_MILLIS,
    private val clock: () -> Long = System::currentTimeMillis
) : VehicleDataSource {

    init {
        require(scenarios.isNotEmpty()) { "At least one scenario is required" }
        require(intervalMillis > 0) { "intervalMillis must be positive" }
    }

    data class Scenario(
        val name: String,
        val speedKmh: Float?,
        val motorOn: Boolean?,
        val killSwitchActive: Boolean?,
        val batteryPercent: Int? = 100
    )

    override fun observeVehicleData(): Flow<VehicleData> = flow {
        while (true) {
            for (scenario in scenarios) {
                emit(
                    VehicleData(
                        speedKmh = scenario.speedKmh,
                        motorOn = scenario.motorOn,
                        killSwitchActive = scenario.killSwitchActive,
                        timestampMillis = clock(),
                        batteryPercent = scenario.batteryPercent
                    )
                )
                delay(intervalMillis)
            }
        }
    }

    companion object {
        const val DEFAULT_INTERVAL_MILLIS = 1_000L

        val DEFAULT_SCENARIOS = listOf(
            Scenario("Parked", speedKmh = 0f, motorOn = false, killSwitchActive = false),
            Scenario("Ready", speedKmh = 0f, motorOn = true, killSwitchActive = false),
            Scenario("Riding", speedKmh = 25f, motorOn = true, killSwitchActive = false),
            Scenario("Accelerating", speedKmh = 50f, motorOn = true, killSwitchActive = false),
            Scenario("Motor disabled", speedKmh = 0f, motorOn = false, killSwitchActive = true)
        )
    }
}
