package com.simpleride.rideos.data.repository

import com.simpleride.rideos.data.model.VehicleData
import com.simpleride.rideos.data.source.VehicleDataSource
import kotlinx.coroutines.flow.Flow

/** Single entry point the presentation layer uses to get vehicle data. */
class VehicleRepository(
    private val dataSource: VehicleDataSource
) {
    fun observeVehicleData(): Flow<VehicleData> = dataSource.observeVehicleData()
}
