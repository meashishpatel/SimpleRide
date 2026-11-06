package com.simpleride.rideos.data.model

data class VehicleData(
    val speed: Float,
    val motorOn: Boolean,
    val killSwitchActive: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val batteryState: Int = 100
)