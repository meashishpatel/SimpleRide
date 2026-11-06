package com.simpleride.rideos.data.repository

import com.simpleride.rideos.data.model.VehicleData
import com.simpleride.rideos.data.source.VehicleDataSource
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val dataSource: VehicleDataSource
) {
    fun observeVehicleData(): Flow<VehicleData> {
        return dataSource.observeVehicleData()
    }
}