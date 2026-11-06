package com.simpleride.rideos.data.source

import com.simpleride.rideos.data.model.VehicleData
import kotlinx.coroutines.flow.Flow


interface VehicleDataSource {
    fun observeVehicleData(): Flow<VehicleData>
}