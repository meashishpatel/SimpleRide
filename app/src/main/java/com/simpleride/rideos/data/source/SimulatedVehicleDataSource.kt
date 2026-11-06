package com.simpleride.rideos.data.source

import com.simpleride.rideos.data.model.VehicleData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SimulatedVehicleDataSource : VehicleDataSource {

    private val vehicleScenarios = listOf(
        // Parked
        VehicleData(
            speed = 0f,
            motorOn = false,
            killSwitchActive = false
        ),

        // Ready
        VehicleData(
            speed = 0f,
            motorOn = true,
            killSwitchActive = false
        ),

        // Riding
        VehicleData(
            speed = 25f,
            motorOn = true,
            killSwitchActive = false
        ),

        // Accelerating
        VehicleData(
            speed = 50f,
            motorOn = true,
            killSwitchActive = false
        ),

        // Motor disabled
        VehicleData(
            speed = 0f,
            motorOn = false,
            killSwitchActive = true
        )
    )

    override fun observeVehicleData(): Flow<VehicleData> = flow {

        while (true) {
            vehicleScenarios.forEach { scenario ->
                emit(
                    scenario.copy(
                        timestamp = System.currentTimeMillis()
                    )
                )

                delay(1000)
            }
        }
    }
}