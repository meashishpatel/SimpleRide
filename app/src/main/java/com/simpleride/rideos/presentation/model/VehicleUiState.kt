package com.simpleride.rideos.presentation.model
data class VehicleUiState(
    val speed: Float? = null,
    val motorOn: Boolean = false,
    val killSwitchActive: Boolean = false,
    val isDataAvailable: Boolean = false,
    val lastUpdated: Long? = null,
    val errorMessage: String? = null
)