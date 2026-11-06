package com.simpleride.rideos.data.source

import com.simpleride.rideos.data.model.VehicleData
import kotlinx.coroutines.flow.Flow

/** Abstraction over wherever vehicle data comes from (simulator today, CAN/VHAL later). */
interface VehicleDataSource {
    fun observeVehicleData(): Flow<VehicleData>
}
